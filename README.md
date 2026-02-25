# SPIGA Simulator

[![CI](https://github.com/odebrino/SPIGA/actions/workflows/ci.yml/badge.svg)](https://github.com/odebrino/SPIGA/actions/workflows/ci.yml)
[![CodeQL](https://github.com/odebrino/SPIGA/actions/workflows/codeql.yml/badge.svg)](https://github.com/odebrino/SPIGA/actions/workflows/codeql.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

SPIGA (Simulateur de Planification et de Gestion d'Actifs Mobiles) is a Java simulator for coordinating heterogeneous mobile assets (air, sea, and land) in a dynamic 3D environment.

## Repository Layout

- `spiga-simulator/`: Maven Java project (source code, tests, build config)
- `.github/`: CI/CD workflows, issue templates, and automation config

## Requirements

- JDK 17 or newer
- Git

No global Maven installation is required. Use the Maven Wrapper included in this repo.

## Quick Start

```bash
git clone https://github.com/odebrino/SPIGA.git
cd SPIGA/spiga-simulator
./mvnw -B test
```

## Build and Verify

```bash
cd spiga-simulator
./mvnw -B verify
```

## Run the Simulator

### JavaFX UI

```bash
cd spiga-simulator
./mvnw javafx:run
```

### CLI Mode

```bash
cd spiga-simulator
./mvnw -B exec:java -Dexec.mainClass="com.spiga.ui.ConsoleInterface"
```

## Project Packages

- `com.spiga.core`: domain models and vehicle behavior
- `com.spiga.env`: terrain, collision, and pathfinding services
- `com.spiga.mission`: mission and fleet orchestration
- `com.spiga.app`: application bootstrap and wiring
- `com.spiga.ui`: JavaFX and CLI interfaces

## Quality and Automation

- GitHub Actions CI on pushes and pull requests
- Maven Enforcer for Java/Maven version checks
- Surefire/Failsafe pinned for reproducible test execution
- Jacoco reports generated during test phase
- Dependabot for dependency updates

## Contributing and Policies

- Contribution guide: [CONTRIBUTING.md](CONTRIBUTING.md)
- Code of conduct: [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)
- Security policy: [SECURITY.md](SECURITY.md)
- Support guide: [SUPPORT.md](SUPPORT.md)
- Changelog: [CHANGELOG.md](CHANGELOG.md)

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).
