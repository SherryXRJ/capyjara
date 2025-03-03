package com.capy.capyjara.starter.mvc;

import com.capy.capyjara.common.exception.BusinessException;
import com.capy.capyjara.common.response.CommonResultStatus;
import com.capy.capyjara.starter.MvcSecurityStarterProperties;
import com.capy.capyjara.starter.security.InternalRequestAuthorizationManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class FeignConfig {

    private final MvcSecurityStarterProperties mvcSecurityStarterProperties;

    @ConditionalOnMissingBean
    @Bean
    public RequestInterceptor authInterceptor(){
        return new DefaultRequestInterceptor(InternalRequestAuthorizationManager.DEFAULT_INTERNAL_AUTH_HEADER, mvcSecurityStarterProperties.getInternalRequestSecretKey());
    }

    @LoadBalanced
    @ConditionalOnClass
    @Bean
    @ConditionalOnMissingBean(name = "restTemplate")
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @ConditionalOnMissingBean
    @Bean
    public ErrorDecoder feignErrorDecoder(){
        return new FeignErrorDecoder();
    }

    static class DefaultRequestInterceptor implements RequestInterceptor {

        private final static String AUTHORIZATION_HEADER = "Authorization";

        private final String secretKeyHeader;

        private final String secretKey;

        public DefaultRequestInterceptor(String secretKeyHeader, String secretKey) {
            this.secretKeyHeader = secretKeyHeader;
            this.secretKey = secretKey;
        }

        @Override
        public void apply(RequestTemplate requestTemplate) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            requestTemplate.header(AUTHORIZATION_HEADER, request.getHeader(AUTHORIZATION_HEADER));

            //  添加Feign RPC请求头秘钥信息
            requestTemplate.header(secretKeyHeader, secretKey);
        }
    }

    /**
     * Feign异常处理
     */
    static class FeignErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            log.error("=> Feign调用异常 method:{}, msg:{}", methodKey, response.toString());
            return HttpStatus.SERVICE_UNAVAILABLE.value() == response.status() ?
                    new BusinessException(CommonResultStatus.SERVICE_UNAVAILABLE) :
                    new BusinessException(CommonResultStatus.INTERNAL_ERROR);
        }
    }
}
