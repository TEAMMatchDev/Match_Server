package com.example.matchapi.common;

import com.example.matchcommon.exception.BaseDynamicException;
import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.discord.service.DiscordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
    private final DiscordService discordService;




    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity onConstraintValidationException(ConstraintViolationException exception, @AuthenticationPrincipal User user, HttpServletRequest request) {
        Map<String, String> errors = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                .reduce((first, second) -> second)
                                .get().toString(),
                        ConstraintViolation::getMessage
                ));
        getExceptionStackTrace(exception, user, request, errors);
        return new ResponseEntity<>(CommonResponse.onFailure("REQUEST_ERROR", "요청 형식 에러 result 확인해주세요", errors), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity onMethodArgumentNotValidException(MethodArgumentNotValidException exception, @AuthenticationPrincipal User user, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {

            String fieldName = fieldError.getField();
            String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
            if (errors.containsKey(fieldName)) {
                String existingErrorMessage = errors.get(fieldName);
                errorMessage = existingErrorMessage + ", " + errorMessage;
            }

            errors.put(fieldName, errorMessage);
        }

        getExceptionStackTrace(exception, user, request, errors);

        return new ResponseEntity<>(CommonResponse.onFailure("REQUEST_ERROR", "요청 형식 에러 result 확인해주세요", errors), null, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity onKnownException(BaseException baseException,
                                           @AuthenticationPrincipal User user, HttpServletRequest request) {
        getExceptionStackTrace(baseException, user, request, null);

        return new ResponseEntity<>(CommonResponse.onFailure(baseException.getErrorReasonHttpStatus().getCode(), baseException.getErrorReasonHttpStatus().getMessage(), baseException.getErrorReasonHttpStatus().getResult()),
                null, baseException.getErrorReasonHttpStatus().getHttpStatus());
    }

    @ExceptionHandler(value = BaseDynamicException.class)
    public ResponseEntity onKnownDynamicException(BaseDynamicException baseDynamicException, @AuthenticationPrincipal User user,
                                      HttpServletRequest request) {
        getExceptionStackTrace(baseDynamicException, user, request, null);
        return new ResponseEntity<>(CommonResponse.onFailure(baseDynamicException.getStatus().getErrorReason().getCode(), baseDynamicException.getStatus().getErrorReason().getMessage(), baseDynamicException.getData()), null,
                baseDynamicException.getStatus().getErrorReasonHttpStatus().getHttpStatus());
    }


/*
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity onException(Exception exception, @AuthenticationPrincipal User user,
                                      HttpServletRequest request) {
        getExceptionStackTrace(exception, user, request, null);
        if(user==null){
            discordService.sendUnKnownMessage("로그인 되지 않은 유저", exception, request);
        }
        else{
            discordService.sendUnKnownMessage(user.getUsername(), exception, request);
        }

        return new ResponseEntity<>(CommonResponse.onFailure("500", exception.getMessage(), null), null,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    private void getExceptionStackTrace(Exception e, User user, HttpServletRequest request, Map<String, String> errors) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.append("\n==========================!!!ERROR TRACE!!!==========================\n");
        if(user != null) {
            pw.append("USER_ID : " + user.getId()).append("\n");
        }
        pw.append("REQUEST_URI : ").append(request.getRequestURI()).append(" ").append(request.getMethod()).append("\n");
        if (e instanceof BaseException) {
            pw.append("ERROR_CODE : ").append(((BaseException) e).getErrorReason().getCode()).append("\n");
            pw.append("ERROR_MESSAGE : ").append(((BaseException) e).getErrorReason().getMessage()).append("\n");
        } else if (e instanceof BaseDynamicException) {
            pw.append("ERROR_CODE : ").append(((BaseDynamicException) e).getStatus().getErrorReason().getCode()).append("\n");
            pw.append("ERROR_MESSAGE : ").append(((BaseDynamicException) e).getStatus().getErrorReason().getMessage()).append("\n");
        } else if(e instanceof MethodArgumentNotValidException | e instanceof ConstraintViolationException){
            pw.append("ERROR_CODE : ").append("REQUEST_ERROR").append("\n");
            pw.append("ERROR_MESSAGE : ").append(errors.toString()).append("\n");
        }
        else {
            pw.append("ERROR_MESSAGE : ").append(e.getMessage()).append("\n");
            pw.append("ERROR_MESSAGE : ").append(e.getLocalizedMessage()).append("\n");
        }
        pw.append("=====================================================================");
        log.error(sw.toString());
    }



}
