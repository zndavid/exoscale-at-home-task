package com.a1.exoscaleathometask.config;

import com.a1.exoscaleathometask.interceptor.ExoscaleV2AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private final ExoscaleApiProperties apiProperties;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors
                = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new ExoscaleV2AuthInterceptor(apiProperties));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

}
