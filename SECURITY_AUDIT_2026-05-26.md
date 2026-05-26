# Security audit report (2026-05-26)

## Scope
- Android application static security review (Manifest, file sharing, storage, import/export paths, and DB access).
- Local checks executed in CI-like shell without Android SDK/emulator.
- Dependency-level CVE scan was not performed in this run.

## Checks performed
- `./gradlew test` (failed due to missing Android SDK in environment).
- `./gradlew lint` (failed due to missing Android SDK in environment).
- Targeted static pattern scan for risky APIs and misconfigurations with `rg` (WebView/JS bridge, unsafe crypto, exported components, URI grants, debug logs, hardcoded secrets).
- Manual code review for CSV/XLSX generation and import pipeline.

## Findings

### 1) CSV formula injection risk in generated reports (Medium)
**Where:** `app/src/main/java/com/boldrex/postavki/ExcelService.kt`

User-controlled text fields (e.g., product names, articles, barcode-like strings) are exported to CSV. Spreadsheet applications may execute values beginning with `=`, `+`, `-`, or `@` as formulas.

**Risk:** Opening exported CSV in Excel/LibreOffice can trigger formula execution (CSV Injection), including data exfiltration tricks via external references.

**Remediation:** Prefix dangerous leading characters with `'` before CSV escaping.

## Positive observations
- `android:allowBackup` is disabled (`false`).
- `FileProvider` is non-exported and configured with narrowed `reports/` directories.
- No hardcoded Ozon API credentials found (`CLIENT_ID` / `API_KEY` empty placeholders).
- Room queries are parameterized; obvious SQL injection vectors were not identified.

## Residual risks / recommendations
1. Add dependency vulnerability scanning in CI (e.g., OWASP Dependency-Check, OSV-Scanner, or Gradle plugin equivalent).
2. Add Android-specific static checks in CI where SDK is available (`lintVitalRelease`, `detekt`, SAST).
3. Keep API keys outside source code (BuildConfig/local secure storage/remote config) and enforce secret scanning in CI.
