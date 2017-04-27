package com.example.conf;

import com.codahale.metrics.MetricRegistry;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;

@SpringBootConfiguration
public class AppBeans {

    @Bean
    public Filter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(5120);
        filter.setIncludeHeaders(true);
        return filter;
    }


    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }
}
