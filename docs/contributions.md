# Contributions and implementation scope

Maintained by [Rafael Ode Brino](https://github.com/odebrino). The existing MIT license credits **SPIGA Contributors** and is retained.

## Documented release and maintenance work

- [Demonstration/presentation revision](https://github.com/odebrino/SPIGA/commit/f4ff7b60f8b94119b1adabadca67a0cf67940a62): real simulator screenshot, repeatable CLI input/transcript, architecture explanation, and updated CI actions.
- [Dependency-review verification](https://github.com/odebrino/SPIGA/commit/92d093c433f8f02b755541dd2872b662dbe2b1b5): explicit comparison inputs and supported action reference.
- [Runnable release revision](https://github.com/odebrino/SPIGA/commit/09e4db4bf7c8e09198a232be2664beec4fe39933): compatible dependency updates, JDK 17/21 built-JAR checks, CLI packaging, and tagged-release automation.
- [Final maintenance revision](https://github.com/odebrino/SPIGA/commit/5652f48004a28898f048325080ade96df17d7522): verified Java setup action update and wrapper line-ending normalization.

These commits establish specific repository, verification, and release responsibilities. They do not claim sole authorship of the original simulator. Dependabot-authored dependency updates remain credited in Git history.

## Simulation implementation

The [architecture overview](architecture.md) maps mobile-asset behavior, A* pathfinding, terrain/collision checks, mission orchestration, and JavaFX/CLI presentation to their source modules. It describes the implementation without attributing every original module to the maintainer. [Demonstrations](demonstrations.md) and the [CLI release](release-cli.md) provide runnable evidence.
