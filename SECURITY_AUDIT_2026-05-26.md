# Security audit report (2026-05-26)

## Scope
- Static review of Android app manifest, file sharing configuration, and dependency declarations.
- No dynamic penetration testing was performed.

## Findings

### 1) Broad implicit intent attack surface on exported activity (High)
**Where:** `app/src/main/AndroidManifest.xml`

`MainActivity` is exported and declares custom implicit intent actions:
- `com.boldrex.postavki.action.NEW_SHIPMENT`
- `com.boldrex.postavki.action.IMPORT_REPORTS`

Any third-party app can send these actions unless guarded by permission checks or explicit caller validation. This can enable intent spoofing / unauthorized workflow triggering.

**Risk:** Forced UI state changes, untrusted data flow into app logic, social engineering surfaces.

**Recommendation:**
- Prefer explicit intents from trusted packages.
- Add custom signature-level permission for these actions and enforce it.
- Validate incoming intent extras and caller identity before executing business logic.

### 2) FileProvider paths are overly broad (Medium)
**Where:** `app/src/main/res/xml/file_paths.xml`

The provider exposes `.` for:
- `external-files-path`
- `files-path`
- `cache-path`

Although provider is non-exported, URI grants from app code may unintentionally expose more files than needed.

**Risk:** Excessive data disclosure if a shared URI is abused or if share-flow mistakes occur.

**Recommendation:** Restrict paths to minimal subdirectories used for reports only (e.g., `reports/`).

### 3) Backups enabled by default without exclusions (Medium)
**Where:** `app/src/main/AndroidManifest.xml`, `app/src/main/res/xml/backup_rules.xml`, `app/src/main/res/xml/data_extraction_rules.xml`

`android:allowBackup="true"` is enabled. Rules files are mostly default templates with no explicit exclusions.

**Risk:** App-local operational data may be backed up/restored unexpectedly (depends on Android version and transport), increasing data exposure risk.

**Recommendation:**
- If business data is sensitive, set `allowBackup=false`.
- Or define strict include/exclude rules and exclude operational/PII-like datasets.

## What was *not* found
- Hardcoded API secrets in `OzonApiConfig.kt` (values are empty placeholders).

## Next steps
1. Lock down exported intent handlers with permissions + validation.
2. Narrow FileProvider to least-privilege paths.
3. Harden backup/data extraction policy.
4. Run dependency CVE scan in CI (OWASP Dependency-Check or OSV) on every PR.
