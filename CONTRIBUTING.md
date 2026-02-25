# Contributing to SPIGA

Thanks for contributing.

## Development Setup

1. Fork and clone the repository.
2. Enter the module directory:

```bash
cd spiga-simulator
```

3. Use the Maven wrapper shipped with this repo:

```bash
./mvnw -B test
```

## Branching and PRs

1. Create a branch from `main`.
2. Keep each PR focused on one topic.
3. Include tests for behavior changes.
4. Ensure CI is green before requesting review.

## Commit Style

Prefer clear, imperative commit messages.

Examples:
- `fix: prevent land vehicle from accepting sea target`
- `build: pin surefire and add enforcer rules`
- `docs: add security and contribution guides`

## Quality Requirements

Before opening a pull request, run:

```bash
cd spiga-simulator
./mvnw -B verify
```

## Code Review Expectations

Review feedback should focus on:
- correctness and regressions
- test coverage for changed behavior
- clarity and maintainability
