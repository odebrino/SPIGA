#!/bin/sh
set -eu
repo_dir=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
cd "$repo_dir/spiga-simulator"
./mvnw -B -ntp verify
release_dir=target/spiga-cli-1.0.0
mkdir -p "$release_dir"
cp target/spiga-simulator-1.0.0.jar "$release_dir/spiga-cli.jar"
cp ../LICENSE ../docs/examples/cli-input.txt "$release_dir/"
cp ../docs/release-cli.md "$release_dir/README.md"
java -cp "$release_dir/spiga-cli.jar" com.spiga.ui.ConsoleInterface < "$release_dir/cli-input.txt" > "$release_dir/expected-output.txt"
grep -q RECON-DEMO "$release_dir/expected-output.txt"
grep -q EN_MISSION "$release_dir/expected-output.txt"
cat > "$release_dir/run-cli.sh" <<'EOF'
#!/bin/sh
set -eu
app_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
exec java -cp "$app_dir/spiga-cli.jar" com.spiga.ui.ConsoleInterface "$@"
EOF
chmod +x "$release_dir/run-cli.sh"
tar -C target -czf target/spiga-cli-1.0.0.tar.gz spiga-cli-1.0.0
cd target
sha256sum spiga-cli-1.0.0.tar.gz > SHA256SUMS
