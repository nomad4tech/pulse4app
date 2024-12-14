package tech.nomad4;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("pulse4app")
public class AppConfig {

    private Integer bitDelaySec;

    private String listenerUrl;

    private String name;

    private Integer alertTries;

    private Integer maxPulseDelaySec;

    private Integer alertDelaySec;

    private Boolean reportPulse;

    @PostConstruct
    public void validateConfig() {
        handleAnnotation();
        validate();
    }

    private void validate() {
        if (bitDelaySec < 10)
            throw new IllegalArgumentException("bitDelaySeconds must be greater than or equal to 10 seconds. " +
                    "Set it in annotation property @EnablePulseService(bitDelaySeco) " +
                    "or in 'application.properties' (pulse4app.bit-delay-sec).");

        if (listenerUrl == null || listenerUrl.isEmpty())
            throw new IllegalArgumentException("listenerUrl cannot be blank. " +
                    "Set it in annotation property @EnablePulseService(listenerUrl) " +
                    "or in 'application.properties' (pulse4app.listener-url).");

        if (!listenerUrl.matches("^(http|https)://.*$"))
            throw new IllegalArgumentException("listenerUrl must start with 'http://' or 'https://'. " +
                    "Set it in annotation property @EnablePulseService(listenerUrl) " +
                    "or in 'application.properties' (pulse4app.listener-url).");

        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("name cannot be blank. " +
                    "Set it in annotation property @EnablePulseService(name) " +
                    "or in 'application.properties' (pulse4app.name).");

        if (alertTries < 1)
            throw new IllegalArgumentException("alertTries must be greater than or equal to 1. " +
                    "Set it in annotation property @EnablePulseService(alertTries) " +
                    "or in 'application.properties' (pulse4app.alert-tries).");

        if (maxPulseDelaySec < bitDelaySec + 30)
            throw new IllegalArgumentException("maxPulseDelaySec must be greater than bitDelaySec (by def = 10) by 30 sec min. " +
                    "Set it in annotation property @EnablePulseService(maxPulseDelaySec) " +
                    "or in 'application.properties' (pulse4app.max-pulse-delay-sec).");

        if (alertDelaySec < 60)
            throw new IllegalArgumentException("alertDelaySec must be greater than or equal to 60 seconds. " +
                    "Set it in annotation property @EnablePulseService(alertDelaySec) " +
                    "or in 'application.properties' (pulse4app.alert-delay-sec).");
    }


    private void handleAnnotation() {
        Reflections reflections = new Reflections("tech.nomad4");

        Set<Class<?>> cwa = reflections.getTypesAnnotatedWith(EnablePulseService.class);

        if (cwa.size() > 1)
            throw new IllegalStateException("The application cannot have more than " +
                    "one class annotated with @EnablePulseService.");


        if (!cwa.isEmpty()) {
            Class<?> clazz = cwa.iterator().next();
            if (clazz.isAnnotationPresent(EnablePulseService.class)) {
                EnablePulseService annotation = clazz.getAnnotation(EnablePulseService.class);
                if (StringUtils.isBlank(listenerUrl))
                    listenerUrl = annotation.listenerUrl();
                if (StringUtils.isBlank(name))
                    name = annotation.name();
                if (alertTries == null)
                    alertTries = annotation.alertTries();
                if (bitDelaySec == null)
                    bitDelaySec = annotation.bitDelaySeconds();
                if (alertDelaySec == null)
                    alertDelaySec = annotation.alertDelaySec();
                if (maxPulseDelaySec == null)
                    maxPulseDelaySec = annotation.maxPulseDelaySec();
                if (reportPulse == null)
                    reportPulse = annotation.reportMe();
            }
        }
    }

}
