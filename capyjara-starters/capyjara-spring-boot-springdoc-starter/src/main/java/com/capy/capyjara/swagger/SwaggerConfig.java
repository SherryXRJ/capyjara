package com.capy.capyjara.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ConditionalOnProperty(prefix = "springdoc", name = "swagger-ui.enabled", havingValue = "true")
public class SwaggerConfig {

    public static final String[] SWAGGER_URLS = new String[]{
            "/doc.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**"
    };

    @Bean
    public OpenAPI openAPI(Info springDocInfo, SpringDocConfigProperties springDocConfigProperties){
        //  添加Authorization请求头
        Components components = new Components();
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.type(SecurityScheme.Type.APIKEY).scheme("Bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER).name("Authorization").description("JWT 令牌");
        components.addSecuritySchemes("Authorization", securityScheme);
        return new OpenAPI()
                .info(springDocInfo)
                .components(components)
                .security(Collections.singletonList(new SecurityRequirement().addList("Authorization")))
                ;
    }

    @Bean
    @ConfigurationProperties(prefix = "springdoc.info")
    public Info springDocInfo() {
        return new Info();
    }

}
