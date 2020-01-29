package com.cn.boot.sample.business.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.hardware.CentralProcessor.TickType;
import oshi.software.os.*;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 使用oshi框架获取系统运行状态，内存、cpu等
 *
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/runtime")
@Api(tags = "系统运行状态", produces = MediaType.APPLICATION_JSON_VALUE)
public class RuntimeController {

    /**
     * 用于存储系统状态信息
     */
    private static List<String> systemInfo = new ArrayList<>();

    @ApiOperation("打印")
    @GetMapping("/print")
    public String get() {
        log.info("Initializing System...");
        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        printOperatingSystem(os);

        log.info("Checking computer system...");
        printComputerSystem(hal.getComputerSystem());

        log.info("Checking Processor...");
        printProcessor(hal.getProcessor());

        log.info("Checking Memory...");
        printMemory(hal.getMemory());

        log.info("Checking CPU...");
        printCpu(hal.getProcessor());

        log.info("Checking Processes...");
        printProcesses(os, hal.getMemory());

        log.info("Checking Sensors...");
        printSensors(hal.getSensors());

        log.info("Checking Power sources...");
        printPowerSources(hal.getPowerSources());

        log.info("Checking Disks...");
        printDisks(hal.getDiskStores());

        log.info("Checking File System...");
        printFileSystem(os.getFileSystem());

        log.info("Checking Network interfaces...");
        printNetworkInterfaces(hal.getNetworkIFs());

        log.info("Checking Network parameters...");
        printNetworkParameters(os.getNetworkParams());

        // hardware: displays
        log.info("Checking Displays...");
        printDisplays(hal.getDisplays());

        // hardware: USB devices
        log.info("Checking USB Devices...");
        printUsbDevices(hal.getUsbDevices(true));

        log.info("Checking Sound Cards...");
        printSoundCards(hal.getSoundCards());

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < systemInfo.size(); i++) {
            output.append(systemInfo.get(i));
            if (systemInfo.get(i) != null && !systemInfo.get(i).endsWith("\n")) {
                output.append('\n');
            }
        }
        log.info("Printing Operating System and Hardware Info:{}{}", '\n', output);
        return output.toString();
    }

    private static void printOperatingSystem(final OperatingSystem os) {
        systemInfo.add(String.valueOf(os));
        systemInfo.add("Booted: " + Instant.ofEpochSecond(os.getSystemBootTime()));
        systemInfo.add("Uptime: " + FormatUtil.formatElapsedSecs(os.getSystemUptime()));
        systemInfo.add("Running with" + (os.isElevated() ? "" : "out") + " elevated permissions.");
    }

    private static void printComputerSystem(final ComputerSystem computerSystem) {
        systemInfo.add("system: " + computerSystem.toString());
        systemInfo.add(" firmware: " + computerSystem.getFirmware().toString());
        systemInfo.add(" baseboard: " + computerSystem.getBaseboard().toString());
    }

    private static void printProcessor(CentralProcessor processor) {
        systemInfo.add(processor.toString());
    }

    private static void printMemory(GlobalMemory memory) {
        systemInfo.add("Memory: \n " + memory.toString());
        VirtualMemory vm = memory.getVirtualMemory();
        systemInfo.add("Swap: \n " + vm.toString());
    }

    private static void printCpu(CentralProcessor processor) {
        systemInfo.add("Context Switches/Interrupts: " + processor.getContextSwitches() + " / " + processor.getInterrupts());

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        long[][] prevProcTicks = processor.getProcessorCpuLoadTicks();
        systemInfo.add("CPU, IOWait, and IRQ ticks @ 0 sec:" + Arrays.toString(prevTicks));
        // Wait a second...
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        systemInfo.add("CPU, IOWait, and IRQ ticks @ 1 sec:" + Arrays.toString(ticks));
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

        systemInfo.add(String.format(
                "User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%% Steal: %.1f%%",
                100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu, 100d * idle / totalCpu,
                100d * iowait / totalCpu, 100d * irq / totalCpu, 100d * softirq / totalCpu, 100d * steal / totalCpu));
        systemInfo.add(String.format("CPU load: %.1f%%", processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100));
        double[] loadAverage = processor.getSystemLoadAverage(3);
        systemInfo.add("CPU load averages:" + (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
                + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
                + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2])));
        // per core CPU
        StringBuilder procCpu = new StringBuilder("CPU load per processor:");
        double[] load = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks);
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%%", avg * 100));
        }
        systemInfo.add(procCpu.toString());
        long[] freqs = processor.getCurrentFreq();
        if (freqs[0] > 0) {
            StringBuilder sb = new StringBuilder("Current Frequencies: ");
            for (int i = 0; i < freqs.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(FormatUtil.formatHertz(freqs[i]));
            }
            systemInfo.add(sb.toString());
        }
    }

    private static void printProcesses(OperatingSystem os, GlobalMemory memory) {
        systemInfo.add("Processes: " + os.getProcessCount() + ", Threads: " + os.getThreadCount());
        // Sort by highest CPU
        List<OSProcess> procs = Arrays.asList(os.getProcesses(5, ProcessSort.CPU));

        systemInfo.add("   PID  %CPU %MEM       VSZ       RSS Name");
        for (int i = 0; i < procs.size() && i < 5; i++) {
            OSProcess p = procs.get(i);
            systemInfo.add(String.format(" %5d %5.1f %4.1f %9s %9s %s", p.getProcessID(),
                    100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
                    100d * p.getResidentSetSize() / memory.getTotal(), FormatUtil.formatBytes(p.getVirtualSize()),
                    FormatUtil.formatBytes(p.getResidentSetSize()), p.getName()));
        }
    }

    private static void printSensors(Sensors sensors) {
        systemInfo.add("Sensors: " + sensors.toString());
    }

    private static void printPowerSources(PowerSource[] powerSources) {
        StringBuilder sb = new StringBuilder("Power Sources: ");
        if (powerSources.length == 0) {
            sb.append("Unknown");
        }
        for (PowerSource powerSource : powerSources) {
            sb.append("\n ").append(powerSource.toString());
        }
        systemInfo.add(sb.toString());
    }

    private static void printDisks(HWDiskStore[] diskStores) {
        systemInfo.add("Disks:");
        for (HWDiskStore disk : diskStores) {
            systemInfo.add(" " + disk.toString());

            HWPartition[] partitions = disk.getPartitions();
            for (HWPartition part : partitions) {
                systemInfo.add(" |-- " + part.toString());
            }
        }

    }

    private static void printFileSystem(FileSystem fileSystem) {
        systemInfo.add("File System:");

        systemInfo.add(String.format(" File Descriptors: %d/%d", fileSystem.getOpenFileDescriptors(),
                fileSystem.getMaxFileDescriptors()));

        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            systemInfo.add(String.format(
                    " %s (%s) [%s] %s of %s free (%.1f%%), %s of %s files free (%.1f%%) is %s "
                            + (fs.getLogicalVolume() != null && fs.getLogicalVolume().length() > 0 ? "[%s]" : "%s")
                            + " and is mounted at %s",
                    fs.getName(), fs.getDescription().isEmpty() ? "file system" : fs.getDescription(), fs.getType(),
                    FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace()), 100d * usable / total,
                    FormatUtil.formatValue(fs.getFreeInodes(), ""), FormatUtil.formatValue(fs.getTotalInodes(), ""),
                    100d * fs.getFreeInodes() / fs.getTotalInodes(), fs.getVolume(), fs.getLogicalVolume(),
                    fs.getMount()));
        }
    }

    private static void printNetworkInterfaces(NetworkIF[] networkIFs) {
        StringBuilder sb = new StringBuilder("Network Interfaces:");
        if (networkIFs.length == 0) {
            sb.append(" Unknown");
        }
        for (NetworkIF net : networkIFs) {
            sb.append("\n ").append(net.toString());
        }
        systemInfo.add(sb.toString());
    }

    private static void printNetworkParameters(NetworkParams networkParams) {
        systemInfo.add("Network parameters:\n " + networkParams.toString());
    }

    private static void printDisplays(Display[] displays) {
        systemInfo.add("Displays:");
        int i = 0;
        for (Display display : displays) {
            systemInfo.add(" Display " + i + ":");
            systemInfo.add(String.valueOf(display));
            i++;
        }
    }

    private static void printUsbDevices(UsbDevice[] usbDevices) {
        systemInfo.add("USB Devices:");
        for (UsbDevice usbDevice : usbDevices) {
            systemInfo.add(String.valueOf(usbDevice));
        }
    }

    private static void printSoundCards(SoundCard[] cards) {
        systemInfo.add("Sound Cards:");
        for (SoundCard card : cards) {
            systemInfo.add(" " + String.valueOf(card));
        }
    }
}
