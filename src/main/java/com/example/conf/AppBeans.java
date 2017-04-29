package com.example.conf;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootConfiguration
public class AppBeans {

    @Value("${app.metrics.graphite.hostname}")
    private String hostname;

    @Value("${app.metrics.graphite.port}")
    private Integer port;

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

    @Bean
    public Graphite graphite() {
        return new Graphite(hostname, port);
    }

    @Bean
    public GraphiteReporter graphiteReporter(MetricRegistry metricRegistry, Graphite graphite) throws UnknownHostException {
        String hostname = InetAddress.getLocalHost().getHostName();
        return GraphiteReporter
                .forRegistry(metricRegistry)
                .prefixedWith("services.api." + hostname)
                .build(graphite);
    }

}
