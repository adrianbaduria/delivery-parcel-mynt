package com.mynt.deliveryparcel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${voucher.external.api.url}")
    private String webServiceUrl;

    @Value("${voucher.external.api.apikey}")
    private String webServiceApiKey;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(webServiceUrl)
                .defaultHeader("apikey", webServiceApiKey)
                .build();
    }
}
