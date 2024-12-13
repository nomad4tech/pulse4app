package tech.nomad4;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
@ComponentScan(basePackages = "tech.nomad4")
public @interface EnablePulseService {

    String listenerUrl() default "";
    int bitDelaySeconds() default 60;
    String checkAppId() default "";
    int alertTries() default 1;
    int checkTries() default 1;
    int checkSilentSeconds() default 60;

}
