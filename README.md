# Pulse4App Library :heartpulse:

Pulse4App is a Java library designed to send periodic heartbeat signals (pulses) to a configured endpoint in 
Spring-based application. 
The library can help monitor the health and status of an application by providing system metrics and customizable metadata.

## Features 

- Sends POST requests (pulses) to configured endpoint.
- Includes system statistics and application metadata in the payload.
- Configurable intervals, alert retries, and reporting options.
- Easy integration with Spring applications via `@EnablePulseService` annotation.

## Requirements

- Java 17 or higher.
- Spring-based application.

## Installation

### Maven

Add the library as a dependency in your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://raw.githubusercontent.com/nomad4tech/pulse4app/main</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
    <groupId>tech.nomad4</groupId>
    <artifactId>pulse4app</artifactId>
    <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Clone and Install from Source

Alternatively, you can clone the project repository and install it locally using Maven:

```bash
git clone https://github.com/nomad4tech/pulse4app.git
cd pulse4app
mvn clean install
```

## Usage

### 1. Enable Pulse Service

Annotate your Spring application configuration class with `@EnablePulseService`:

```java
import tech.nomad4.annotation.EnablePulseService;

@EnablePulseService
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### 2. Configuration

Pulse4App can be configured through application properties or via the `@EnablePulseService` annotation parameters. 
The library prioritizes properties from the `application.properties` or `application.yml` file. 
If property is not defined, the annotation's default values will be used.

#### Available Properties:

| Property                        | Default Value | Description                                                                 |
|---------------------------------|---------------|-----------------------------------------------------------------------------|
| `pulse4app.listener-url`        | (required)    | The endpoint to send pulses to.                                             |
| `pulse4app.bit-delay-sec`       | 10            | Interval (in seconds) between consecutive pulses.                           |
| `pulse4app.name`                | (required)    | The application name.                                                       |
| `pulse4app.alert-tries`         | 1             | Number of retry attempts on pulse failure.                                  |
| `pulse4app.alert-delay-sec`     | 60            | Delay (in seconds) between retry alerts.                                    |
| `pulse4app.report-pulse`        | false         | Whether to send detailed reports for each pulse.                            |
| `pulse4app.message`             | (optional)    | Application state message.                                                  |
| `pulse4app.max-pulse-delay-sec` | 60            | Maximum allowed time (in seconds) between pulses before an alert is raised. |

#### Example `application.properties`:

```properties
pulse4app.listener-url=http://example.com/pulse
pulse4app.bit-delay-sec=15
pulse4app.name=MyApp
pulse4app.alert-tries=3
pulse4app.alert-delay-sec=120
pulse4app.report-pulse=true
pulse4app.max-pulse-delay-sec=90
```

### 3. Annotation Parameters

You can also configure Pulse4App directly via `@EnablePulseService`. Example:

```java
@EnablePulseService(
    listenerUrl = "http://example.com/pulse",
    bitDelaySeconds = 15,
    name = "MyApp",
    alertTries = 3,
    maxPulseDelaySec = 30,
    alertDelaySec = 120,
    reportPulse = true
)
```

## Payload Structure

Each pulse sent by the library includes the following fields:

### `Pulse` Class

```java
@Getter
@Setter
public class Pulse {
    private String message;          // Application state message (optional).
    private String name;             // Application name.
    private int alertTries;          // Number of retry attempts on pulse failure.
    private int maxPulseDelaySec;    // Max allowable delay between pulses.
    private int alertDelaySec;       // Delay between consecutive alerts.
    private boolean reportPulse;     // Whether detailed reports are sent.
    private SystemStats systemStats; // System metrics and statistics.
}
```

### System Metrics

The `SystemStats` object provides automatically collected details about the system running
the application at the time of the pulse, such as memory usage, CPU load, and available disk space:

```java
@Getter
@Setter
public class SystemStats {
    private double systemLoadAverage;   // Average system load over the last minute.
    private long usedMemory;            // Amount of used memory in bytes.
    private long totalMemory;           // Total memory available in bytes.
    private long usedDiskSpace;         // Amount of used disk space in bytes.
    private long totalDiskSpace;        // Total disk space available in bytes.
    private String osName;              // Operating system name.
    private String osArch;              // Operating system architecture.
    private int availableProcessors;    // Number of available processors.
}
```

## Default Behavior

1. **Pulse Sending**: The library sends POST request to the configured `listenerUrl` every `bitDelaySeconds`.
2. **Alert Handling**: If pulse is not acknowledged within `maxPulseDelaySec`, the library retries sending alerts based on `alertTries` and `alertDelaySec`.
3. **Metadata**: Each pulse contains metadata defined in the configuration, along with system statistics at the time of the pulse.

## Example Pulse Payload

```json
{
  "message": "Application running smoothly",
  "name": "MyApp",
  "alertTries": 3,
  "maxPulseDelaySec": 30,
  "alertDelaySec": 120,
  "reportPulse": true,
  "systemStats": {
    "systemLoadAverage": 0.75,
    "usedMemory": 512000000,
    "totalMemory": 2048000000,
    "usedDiskSpace": 50000000000,
    "totalDiskSpace": 100000000000,
    "osName": "Linux",
    "osArch": "amd64",
    "availableProcessors": 4
  }
}
```

## Contributing
**Guidelines:** Contributions are welcome! Please fork the repository, make your changes, and submit pull request with description of your modifications.  
For questions or collaboration, feel free to reach out at [alex.sav4387@gmail.com](mailto:alex.sav4387@gmail.com), check out my **GitHub** profile: [nomad4tech](https://github.com/nomad4tech), or connect with me on **Telegram**: [@nomad4tech](https://t.me/nomad4tech).

## License
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

**Terms:** This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

