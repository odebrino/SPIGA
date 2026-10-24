# Dependency maintenance — 24 October 2026

The release keeps the documented JDK 17/21 matrix. Updates are verified with `./mvnw -B -ntp verify` and the scripted CLI example.

| Requests | Decision |
| --- | --- |
| #20 Enforcer 3.6.3, #9 Compiler 3.15.0 | Adopted together as build-tool updates |
| #6 Surefire / #7 Failsafe 3.5.5 | Adopted together to keep test tooling aligned |
| #1 JaCoCo 0.8.14 | Adopted; existing coverage gates retained |
| #19 JUnit 6.1.0 | Adopted with Surefire/Failsafe update; JUnit 6 requires Java 17, compatible with the declared floor |
| #18 Dependency Review, #13 artifact upload, #10 CodeQL, #8 checkout, #4 setup-java | Already superseded by the previous verified Actions updates; dependency review uses the existing exact `v5.0.0` tag |
| #16/#17 JavaFX 26.0.1 | Deferred because JavaFX 26 requires JDK 24; updated both JavaFX dependencies together within 17.x to 17.0.21 |

Sources: [JUnit supported Java versions](https://docs.junit.org/6.0.3/overview.html), [JavaFX 26 runtime requirement](https://openjfx.io/highlights/26/), [Maven Central JavaFX version metadata](https://repo.maven.apache.org/maven2/org/openjfx/javafx-controls/maven-metadata.xml). The actual Maven resolution and CI matrix validate the selected versions.

Dependabot groups routine Maven updates and ignores JavaFX major updates while the JDK 17 floor remains. Review that floor deliberately before adopting a newer JavaFX major. No dependency alerts were open at the time of this check.
