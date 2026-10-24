# Reproducible demonstrations

## JavaFX interface

From `spiga-simulator/`:

```bash
./mvnw javafx:run
```

The default scene contains two drones, a surface vehicle, a submarine, and a land vehicle. Blue regions are water and green regions are land. The dashboard exposes asset creation, fleet state, and target assignment. The displayed map is a 2D projection of assets with x/y/z positions.

![Actual JavaFX simulator scene](images/simulator.png)

This image was captured from the running application on 24 October 2026, using its default demo scene. It is an application-scene snapshot, not a diagram or generated mockup.

## Scripted CLI scenario

The console is interactive by default. A recorded input stream makes a small scenario repeatable:

```bash
cd spiga-simulator
./mvnw -q compile exec:java \
  -Dexec.mainClass=com.spiga.ui.ConsoleInterface \
  < ../docs/examples/cli-input.txt
```

The input creates the `DEMO` logistics drone, lists the fleet, assigns `RECON-DEMO`, advances one simulation step, lists the fleet again, and exits. It exercises the existing CLI and orchestration; it does not require a display server.

The expected output includes an asset-created line, assignment of `RECON-DEMO` to `DEMO`, and the final `EN_MISSION` fleet state. See [the captured output](examples/cli-output.txt) for the verified transcript. Positions and state changes describe this small simulator scenario, not a physical validation experiment.

## Test and coverage reports

```bash
cd spiga-simulator
./mvnw -B -ntp verify
```

Open `target/site/jacoco/index.html` for coverage and `target/surefire-reports/` for test reports. CI publishes the coverage report as an artifact. See [architecture and the measured verification snapshot](architecture.md).
