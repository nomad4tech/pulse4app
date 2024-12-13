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

    private Integer bitDelaySeconds;

    private String listenerUrl;

    private String checkAppId;

    private Integer checkTries;

    private Integer alertTries;

    private Integer checkSilentSeconds;

    // TODO clarify with validation issue in main application (annotation don't work)
    @PostConstruct
    public void validateConfig() {
        handleAnnotation();
        validate();
    }

    private void validate() {
        if (bitDelaySeconds < 10)
            throw new IllegalArgumentException("bitDelaySeconds must be greater than or equal to 10 seconds. " +
                    "Set it in annotation property @EnablePulseService(bitDelaySeconds) " +
                    "or in 'application.properties' (pulse4app.bit-delay-seconds).");

        if (listenerUrl == null || listenerUrl.isEmpty())
            throw new IllegalArgumentException("listenerUrl cannot be blank. " +
                    "Set it in annotation property @EnablePulseService(listenerUrl) " +
                    "or in 'application.properties' (pulse4app.listener-url).");

        if (!listenerUrl.matches("^(http|https)://.*$"))
            throw new IllegalArgumentException("listenerUrl must start with 'http://' or 'https://'. " +
                    "Set it in annotation property @EnablePulseService(listenerUrl) " +
                    "or in 'application.properties' (pulse4app.listener-url).");

        if (checkAppId == null || checkAppId.trim().isEmpty())
            throw new IllegalArgumentException("checkAppId cannot be blank. " +
                    "Set it in annotation property @EnablePulseService(checkAppId) " +
                    "or in 'application.properties' (pulse4app.check-app-id).");

        if (checkTries < 1)
            throw new IllegalArgumentException("checkTries must be greater than or equal to 1. " +
                    "Set it in annotation property @EnablePulseService(checkTries) " +
                    "or in 'application.properties' (pulse4app.check-tries).");

        if (alertTries < 1)
            throw new IllegalArgumentException("alertTries must be greater than or equal to 1. " +
                    "Set it in annotation property @EnablePulseService(alertTries) " +
                    "or in 'application.properties' (pulse4app.alert-tries).");

        if (checkSilentSeconds < 10)
            throw new IllegalArgumentException("checkSilentSeconds must be greater than or equal to 10 seconds. " +
                    "Set it in annotation property @EnablePulseService(checkSilentSeconds) " +
                    "or in 'application.properties' (pulse4app.check-silent-seconds).");
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
                if (StringUtils.isBlank(checkAppId))
                    checkAppId = annotation.checkAppId();
                if (checkTries == null)
                    checkTries = annotation.checkTries();
                if (alertTries == null)
                    alertTries = annotation.alertTries();
                if (checkSilentSeconds == null)
                    checkSilentSeconds = annotation.checkSilentSeconds();
                if (bitDelaySeconds == null)
                    bitDelaySeconds = annotation.bitDelaySeconds();
            }
        }
    }

}
