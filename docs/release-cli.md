# SPIGA CLI release

Requires **Java 17 or 21** on your PATH. No Maven or JavaFX installation is needed for this CLI archive. Extract it, enter the extracted folder, and run:

```sh
./run-cli.sh < cli-input.txt
```

Or, on any platform with Java available:

```sh
java -cp spiga-cli.jar com.spiga.ui.ConsoleInterface < cli-input.txt
```

The input creates the `DEMO` logistics drone, assigns `RECON-DEMO`, advances one step, lists its `EN_MISSION` status and position, and exits. Compare `expected-output.txt`; there are no timing-sensitive inputs. Run without redirection for the interactive console. The console labels are French.

For the graphical interface, use the tagged source, a desktop session, JDK 17/21 and `./mvnw javafx:run` from `spiga-simulator/`. Maven resolves the platform-specific JavaFX 17 dependencies. The CLI archive does not include the JavaFX UI runtime.

The archive is produced by `scripts/package-cli.sh` after the full Maven verification and demonstration output checks. Original MIT license included.
