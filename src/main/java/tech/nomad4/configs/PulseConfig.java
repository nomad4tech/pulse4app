package tech.nomad4.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.nomad4.app.PulseSender;

import java.net.http.HttpClient;

@Configuration
public class PulseConfig {

    @Bean(name = "defaultPulseSender")
    @ConditionalOnMissingBean(PulseSender.class)
    public PulseSender pulseSender() {
        return new PulseSender(HttpClient.newBuilder().build());
    }

}
