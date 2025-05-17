package web.api;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;


@Path("/metrics")
public class MetricsResource {

    private final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public MetricsResource() {
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new ClassLoaderMetrics().bindTo(registry);

        new JmxMeterBinder(
            "web.api.check:type=HitStatistics",
            "total_points",
            "TotalPoints",
            List.of("HitStatistics", "total_points")
        ).bindTo(registry);

        new JmxMeterBinder(
            "web.api.check:type=HitStatistics",
            "missed_points",
            "MissedPoints",
            List.of("HitStatistics", "missed_points")
        ).bindTo(registry);

        new JmxMeterBinder(
            "web.api.check:type=HitStatistics",
            "hit_percentage",
            "HitPercentage",
            List.of("HitStatistics", "hit_percentage")
        ).bindTo(registry);

        new JmxMeterBinder(
            "web.api.check:type=ConsecutiveMisses",
            "consecutive_misses",
            "ConsecutiveMisses",
            List.of("ConsecutiveMisses", "consecutive_misses")
        ).bindTo(registry);
    }

    @GET
    @Produces("text/plain")
    public String getMetrics() {
        return registry.scrape();
    }
}