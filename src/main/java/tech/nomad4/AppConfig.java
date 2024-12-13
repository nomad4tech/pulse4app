package tech.nomad4;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ToString
@Validated
@Configuration
@Valid
@ConfigurationProperties("pulse4app")
public class AppConfig {

    @NotNull(message = "bitDelaySeconds cannot be null")
    @Min(value = 10, message = "bitDelaySeconds must be greater than or equal to 10 seconds")
    private int bitDelaySeconds = 10;

    @NotEmpty(message = "listenerUrl cannot be blank")
    @Pattern(regexp = "^(http|https)://.*$", message = "listenerUrl must start with 'http://' or 'https://'")
    private String listenerUrl;

    @NotBlank(message = "checkAppId cannot be blank")
    private String checkAppId;

    @Min(value = 1, message = "checkTries must be greater than or equal to 1")
    private int checkTries;

    @Min(value = 1, message = "alertTries must be greater than or equal to 1")
    private int alertTries;

    @Min(value = 10, message = "checkSilentSeconds must be greater than or equal to 10 seconds")
    private int checkSilentSeconds;

    // TODO clarify with validation issue in main application (annotation don't work)
    @PostConstruct
    public void validateConfig() {
        if (bitDelaySeconds < 10)
            throw new IllegalArgumentException("bitDelaySeconds must be greater than or equal to 10 seconds");

        if (listenerUrl == null || listenerUrl.isEmpty())
            throw new IllegalArgumentException("listenerUrl cannot be blank");

        if (!listenerUrl.matches("^(http|https)://.*$"))
            throw new IllegalArgumentException("listenerUrl must start with 'http://' or 'https://'");

        if (checkAppId == null || checkAppId.trim().isEmpty())
            throw new IllegalArgumentException("checkAppId cannot be blank");

        if (checkTries < 1)
            throw new IllegalArgumentException("checkTries must be greater than or equal to 1");

        if (alertTries < 1)
            throw new IllegalArgumentException("alertTries must be greater than or equal to 1");

        if (checkSilentSeconds < 10)
            throw new IllegalArgumentException("checkSilentSeconds must be greater than or equal to 10 seconds");
    }

}
