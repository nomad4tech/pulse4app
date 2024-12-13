package tech.nomad4.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import tech.nomad4.AppConfig;

import java.time.Duration;
import java.util.concurrent.Executors;

@Component
@Slf4j
@RequiredArgsConstructor
public class PulseBit implements ApplicationRunner {

    private final PulseService pulseService;

    private final AppConfig appConfig;

    @Override
    public void run(ApplicationArguments args) {
        Executors.newSingleThreadExecutor(new CustomizableThreadFactory("pulse-bit-")).execute(this::execute);
    }

    private void execute() {

        while (true) {
            pulseService.pulse();
            pause();
        }
    }

    // TODO clarify how to run in independent scheduler
    private void pause() {
        try {
            Thread.sleep(Duration.ofSeconds(appConfig.getBitDelaySeconds()).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
