package com.example.matchcommon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    /*
    private void getExceptionStackTrace(Exception e, @AuthenticationPrincipal User user,
                                        HttpServletRequest request) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.append("\n==========================!!!ERROR TRACE!!!==========================\n");
        pw.append("uri: " + request.getRequestURI() + " " + request.getMethod() + "\n");
        if (user != null) {
            pw.append("uid: " + user.getId() + "\n");
        }
        pw.append(e.getMessage());
        pw.append("\n==================================================================\n");
        log.error(sw.toString());
    }


    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity onKnownException(BaseException baseException,
                                           @AuthenticationPrincipal User user, HttpServletRequest request) {
        getExceptionStackTrace(baseException, user, request);
        return new ResponseEntity<>(CommonResponse.onFailure(baseException.getStatus().getCode(), baseException.getResponseMessage(), baseException.getData()),
                null, baseException.getHttpStatus());
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity onException(Exception exception, @AuthenticationPrincipal User user,
                                      HttpServletRequest request) {
        getExceptionStackTrace(exception, user, request);
        return new ResponseEntity<>(CommonResponse.onFailure("500", exception.getMessage(), null), null,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

     */

}
