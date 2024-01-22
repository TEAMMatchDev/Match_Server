package com.example.matchapi.config;


import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.annotation.DisableSecurity;
import com.example.matchcommon.dto.ErrorReason;
import com.example.matchcommon.exception.errorcode.BaseErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.method.HandlerMethod;
import org.springdoc.core.customizers.OperationCustomizer;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SwaggerConfig {
    //jwt 토큰 인증을 위한 버튼까지 포함
    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(AuthenticationPrincipal.class, CookieValue.class);
    }


    @Value("${spring.config.activate.on-profile}")
    private String profile;
    @Bean
    public GroupedOpenApi v1ApiDocs() {
        String[] paths = {"/**"};
        String[] excludesPath = {"/v2/**", "/admin/**"};
        return GroupedOpenApi.builder()
                .group("API Version 1")
                .pathsToMatch(paths)
                .addOperationCustomizer(customize())
                .pathsToExclude(excludesPath)
                .build();
    }

    @Bean
    public GroupedOpenApi v2ApiDocs() {
        String[] paths = { "/v2/**" };

        return GroupedOpenApi.builder()
                .group("API Version 2")
                .pathsToMatch(paths)
                .addOperationCustomizer(customize())
                .build();
    }

    @Bean
    public GroupedOpenApi adminApiDocs(){
        String[] paths = { "/admin/**" };
        String[] excludesPath = {"/admin/v2/**"};


        return GroupedOpenApi.builder()
                .group("Docs for ADMIN API")
                .pathsToMatch(paths)
                .addOperationCustomizer(customize())
                .build();
    }

    @Bean
    public GroupedOpenApi adminV2ApiDocs(){
        String[] paths = { "/admin/v2/**" };

        return GroupedOpenApi.builder()
            .group("Docs for ADMIN V2 API")
            .pathsToMatch(paths)
            .addOperationCustomizer(customize())
            .build();
    }

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title(profile + " 환경 Match Rest API 문서") // 타이틀
                .version("0.0.1") // 문서 버전
                .description("잘못된 부분이나 오류 발생 시 바로 말씀해주세요.\n" +
                        "우측 상단 Select a definition 클릭시 v1 v2 API 버전 분리") // 문서 설명
                .contact(new Contact() // 연락처
                        .name("임현우")
                        .email("gusdn8926@naver.com"));

        // Security 스키마 설정 (Header Authentication with X-AUTH-TOKEN)
        SecurityScheme headerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .name("X-AUTH-TOKEN")
                .in(SecurityScheme.In.HEADER);

        // Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("X-AUTH-TOKEN");

        return new OpenAPI()
                // Security 인증 컴포넌트 설정
                .components(new Components().addSecuritySchemes("X-AUTH-TOKEN", headerAuth))
                // API 마다 Security 인증 컴포넌트 설정
                .addSecurityItem(addSecurityItem)
                .info(info);
    }

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    private void generateErrorCodeResponseExample(
            Operation operation, Class<? extends BaseErrorCode>[] errorCodeList) {
        ApiResponses responses = operation.getResponses();
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = new HashMap<>();

        for (Class<? extends BaseErrorCode> type : errorCodeList) {
            BaseErrorCode[] errorCodes = type.getEnumConstants();
            // 400, 401, 404 등 에러코드의 상태코드들로 리스트로 모읍니다.
            // 400 같은 상태코드에 여러 에러코드들이 있을 수 있습니다.
            List<ExampleHolder> exampleHolders =
                    Arrays.stream(errorCodes)
                            .map(
                                    baseErrorCode -> {
                                        try {
                                            ErrorReason errorReason = baseErrorCode.getErrorReasonHttpStatus();
                                            ErrorReason errorReasonToView = baseErrorCode.getErrorReason();
                                            return ExampleHolder.builder()
                                                    .holder(
                                                            getSwaggerExample(
                                                                    baseErrorCode.getExplainError(), errorReasonToView))
                                                    .code(errorReason.getHttpStatus().value())
                                                    .name(errorReason.getCode())
                                                    .build();
                                        } catch (NoSuchFieldException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                            .collect(Collectors.toList());

            // statusWithExampleHolders에 현재 루프의 결과를 추가합니다.
            exampleHolders.forEach(
                    exampleHolder ->
                            statusWithExampleHolders
                                    .computeIfAbsent(exampleHolder.getCode(), k -> new ArrayList<>())
                                    .add(exampleHolder));
        }

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(String value, ErrorReason errorReason) {
        Example example = new Example();
        example.description(value);
        example.setValue(errorReason);
        return example;
    }

    private void addExamplesToResponses(
            ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();
                    v.forEach(
                            exampleHolder -> {
                                mediaType.addExamples(
                                        exampleHolder.getName(), exampleHolder.getHolder());
                            });
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(status.toString(), apiResponse);
                });
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeExample apiErrorCodeExample =
                    handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);
            DisableSecurity methodAnnotation =
                    handlerMethod.getMethodAnnotation(DisableSecurity.class);
            if (apiErrorCodeExample != null) {
                Class<? extends BaseErrorCode>[] errorCodes = apiErrorCodeExample.value();
                generateErrorCodeResponseExample(operation, errorCodes);
            }
            if(methodAnnotation !=null){
                operation.setSecurity(Collections.emptyList());
            }
            return operation;
        };

    }


}