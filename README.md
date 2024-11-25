# Serato Watcher (SSL)

A Java application that monitors Serato DJ Pro and logs track information and deck status changes.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven 3.6 or higher
- Serato DJ Pro 2.1.0 or higher

### Installing Prerequisites

#### Windows

1. Install JDK:

   - Download OpenJDK 11 from [Adoptium](https://adoptium.net/)
   - Run the installer and follow the prompts
   - Add Java to your PATH environment variable if not done automatically

2. Install Maven:
   - Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
   - Extract the downloaded archive to a directory (e.g., `C:\Program Files\Apache\maven`)
   - Add Maven's `bin` directory to your PATH environment variable
   - Verify installation by opening Command Prompt and running: `mvn -version`

#### macOS

1. Install JDK using Homebrew:

   ```bash
   brew tap adoptopenjdk/openjdk
   brew install --cask adoptopenjdk11
   ```

2. Install Maven using Homebrew:
   ```bash
   brew install maven
   ```

## Building the Application

1. Clone the repository:

   ```bash
   git clone git@github.com:c1oneman/serato-watcher.git
   cd serato-watcher
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```
   or
   ```bach
   mvn spring-boot:run
   ```

## Running the Application

1. Make sure Serato DJ Pro is installed and running

2. Start the application:

   ```bash
   mvn spring-boot:run
   ```

3. The application will start and begin monitoring Serato DJ Pro's status

## Configuration

By default, the application:

- Listens on port 8080 for WebSocket connections
- localhost:8080/track-status is the only socket
- Monitors Serato DJ Pro's session files
- Tracks deck status changes

## Troubleshooting

### Common Issues

1. **Application fails to start**

   - Ensure Java 11+ is installed: `java -version`
   - Verify Maven installation: `mvn -version`
   - Check if Serato DJ Pro is running

2. **Cannot connect to WebSocket**
   - Verify the application is running
   - Check if port 8080 is available
   - Ensure your firewall isn't blocking the connection

### Getting Help

If you encounter any issues:

1. Check the console output for error messages
2. Review the application logs
3. Open an issue on the project's GitHub repository

## License

MIT

## Contributing

Just open a PR
