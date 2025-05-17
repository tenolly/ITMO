package web.api;

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

public class JmxMeterBinder implements MeterBinder {
    private final String objectName;
    private final String metricName;
    private final String attribute;
    private final List<String> labelNames;

    public JmxMeterBinder(String objectName, String metricName, String attribute, List<String> labelNames) {
        this.objectName = objectName;
        this.metricName = metricName;
        this.attribute = attribute;
        this.labelNames = labelNames;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName mBeanName = new ObjectName(objectName);

            Gauge.builder(metricName, mBeanServer, server -> {
                try {
                    Object value = server.getAttribute(mBeanName, attribute);
                    if (value instanceof Number) {
                        return ((Number) value).doubleValue();
                    }
                    return Double.NaN;
                } catch (Exception e) {
                    System.err.println(e);
                    return 1;
                }
            })
            .tags(labelNames.toArray(new String[0]))
            .register(registry);

        } catch (MalformedObjectNameException e) {
            throw new RuntimeException("Invalid MBean name: " + objectName, e);
        }
    }
}