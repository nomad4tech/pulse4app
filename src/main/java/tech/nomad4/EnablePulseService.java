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
    int bitDelaySeconds() default 10;
    String name() default "";
    int alertTries() default 1;
    int maxPulseDelaySec() default 60;
    int alertDelaySec() default 60;
    boolean reportPulse() default false;
    String message() default "";

}
