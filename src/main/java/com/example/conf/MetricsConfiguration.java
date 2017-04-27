package com.example.conf;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfiguration {

    private final MetricRegistry metricRegistry;

    @Value("${app.metrics.graphite.hostname}")
    private String hostname;

    @Value("${app.metrics.graphite.port}")
    private Integer port;

    @Value("${app.metrics.graphite.period}")
    private Integer period;

    @Autowired
    public MetricsConfiguration(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @PostConstruct
    public void startGraphiteReporter() throws UnknownHostException {
        String hostname = InetAddress.getLocalHost().getHostName();

        Graphite graphite = new Graphite(hostname, port);
        GraphiteReporter reporter = GraphiteReporter
                .forRegistry(metricRegistry)
                .prefixedWith("services.api." + hostname)
                .build(graphite);
        reporter.start(period, TimeUnit.SECONDS);
    }

    @PostConstruct
    public void registerJvmMetrics() {
        registerAll("gc", new GarbageCollectorMetricSet(), metricRegistry);
        registerAll("memory", new MemoryUsageGaugeSet(), metricRegistry);
    }

    private void registerAll(String prefix, MetricSet metricSet, MetricRegistry registry) {
        for (Map.Entry<String, Metric> entry : metricSet.getMetrics().entrySet()) {
            if (entry.getValue() instanceof MetricSet) {
                registerAll(prefix + "." + entry.getKey(), (MetricSet) entry.getValue(), registry);
            } else {
                registry.register(prefix + "." + entry.getKey(), entry.getValue());
            }
        }
    }
}
