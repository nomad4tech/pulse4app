package tech.nomad4.app;

import tech.nomad4.model.SystemStats;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;

public class SystemStatsFetcher {

    public SystemStats gatherSystemMetrics() {

        SystemStats systemStats = new SystemStats();

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        systemStats.setSystemLoadAverage(osBean.getSystemLoadAverage());
        systemStats.setOsName(osBean.getName());
        systemStats.setOsArch(osBean.getArch());
        systemStats.setAvailableProcessors(osBean.getAvailableProcessors());

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        systemStats.setUsedMemory(heapMemoryUsage.getUsed());
        systemStats.setTotalMemory(heapMemoryUsage.getCommitted());

        File file = getFile(systemStats);
        systemStats.setUsedDiskSpace(file.getTotalSpace() - file.getUsableSpace());
        systemStats.setTotalDiskSpace(file.getTotalSpace());


        return systemStats;
    }

    private static File getFile(SystemStats systemStats) {
        String filePath;
        if (systemStats.getOsName().contains("win")) {
            filePath = "C:\\";
        } else {
            filePath = "/";
        }

        return new File(filePath);
    }

}
