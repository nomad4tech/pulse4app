package tech.nomad4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.nomad4.app.IPChecker;
import tech.nomad4.app.PulseSender;
import tech.nomad4.app.SystemStatsFetcher;
import tech.nomad4.AppConfig;
import tech.nomad4.model.Pulse;

@Service
@RequiredArgsConstructor
public class PulseService {

    private final AppConfig appConfig;

    private final PulseSender pulseSender;
    private final IPChecker ipChecker = new IPChecker();
    SystemStatsFetcher statsFetcher = new SystemStatsFetcher();


    public void pulse() {
        Pulse pulse = new Pulse();
        pulse.setMessage(appConfig.getMessage());
        pulse.setName(appConfig.getName());
        pulse.setAlertTries(appConfig.getAlertTries());
        pulse.setMaxPulseDelaySec(appConfig.getMaxPulseDelaySec());
        pulse.setAlertDelaySec(appConfig.getAlertDelaySec());
        pulse.setReportPulse(appConfig.getReportPulse());
        pulse.setSystemStats(statsFetcher.gatherSystemMetrics());
        pulseSender.pulse(appConfig.getListenerUrl(), pulse);
    }



}
