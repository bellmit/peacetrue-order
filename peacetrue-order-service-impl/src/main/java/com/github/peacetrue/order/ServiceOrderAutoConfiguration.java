package com.github.peacetrue.order;

import com.github.peacetrue.spring.core.io.support.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Objects;

/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ServiceOrderProperties.class)
@ComponentScan(basePackageClasses = ServiceOrderAutoConfiguration.class)
@PropertySource(value = "classpath:/application-order-service.yml", factory = YamlPropertySourceFactory.class)
public class ServiceOrderAutoConfiguration {

    private ServiceOrderProperties properties;

    public ServiceOrderAutoConfiguration(ServiceOrderProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

}
