package tech.nomad4.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemStats {
    private double systemLoadAverage;
    private long usedMemory;
    private long totalMemory;
    private long usedDiskSpace;
    private long totalDiskSpace;
    private String osName;
    private String osArch;
    private int availableProcessors;
}
