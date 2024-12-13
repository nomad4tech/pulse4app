package tech.nomad4.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pulse {

    private String message;

    private String ip;

    private String checkAppId;

    private int checkTries;

    private int alertTries;

    private int checkSilentSeconds;

    private SystemStats systemStats;

}
