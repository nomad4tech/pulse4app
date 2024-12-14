package tech.nomad4.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the state message (Pulse) that the application sends.
 * The Pulse class is used to encapsulate information about the application's status,
 * including the message, metadata, and associated system statistics.
 */
@Getter
@Setter
public class Pulse {

    /**
     * The message describing the current state of the application.
     * Not used in listener
     */
    private String message;

    // TODO will be great validate as unique in every application on host
    /**
     * The name of the application or service sending the pulse.
     */
    private String name;

    /**
     * The number of times listener will attempt to send alerts in case of pulse loss.
     */
    private int alertTries;

    /**
     * The maximum allowable time between pulses in seconds.
     * This defines how long listener will wait for the next pulse before triggering alert
     * or taking another specified action.
     */
    private int maxPulseDelaySec;

    /**
     * The delay in seconds between consecutive alerts sent by the listener.
     */
    private int alertDelaySec;

    /**
     * If true, the listener will send report for each received pulse.
     * If false, only significant updates are reported.
     */
    private boolean reportPulse;

    /**
     * Statistics and metrics of the system at the time of the pulse.
     */
    private SystemStats systemStats;
}
