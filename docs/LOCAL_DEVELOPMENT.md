# Local Development Setup

## Using Artifactory (Corporate Environment)

If you're working in a corporate environment that uses Artifactory, follow these steps:

### 1. Configure Maven Settings

Add Artifactory server credentials to `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>artifactory</id>
      <username>YOUR_ARTIFACTORY_USERNAME</username>
      <password>YOUR_ARTIFACTORY_PASSWORD</password>
    </server>
  </servers>
</settings>
```

### 2. Configure npm Registry

**Option A: Override `.npmrc` locally** (recommended - won't be committed)

Create or update `frontend/.npmrc` with:
```
registry=https://repox.jfrog.io/artifactory/api/npm/npm/
//repox.jfrog.io/artifactory/api/npm/npm/:_auth=${ARTIFACTORY_NPM_AUTH}
always-auth=true
```

Set the environment variable:
```bash
export ARTIFACTORY_NPM_AUTH=your_base64_encoded_auth_token
```

**Option B: Use npm config**
```bash
cd frontend
npm config set registry https://repox.jfrog.io/artifactory/api/npm/npm/
npm config set //repox.jfrog.io/artifactory/api/npm/npm/:_authToken YOUR_TOKEN
```

### 3. Build the Project

Artifactory is configured by default. Just build normally:

```bash
# Build entire project
mvn clean install

# Build only frontend
mvn -pl frontend clean install

# Build and run tests
mvn clean verify

# Build with SonarQube analysis
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
```

### Adding/Updating npm Packages (Critical for CI/CD)

**IMPORTANT**: The `package-lock.json` must always contain public registry URLs (`registry.npmjs.org`) for CI/CD to work. 

**The Problem:**
- If you add/update packages while using Artifactory, npm will write Artifactory URLs to the lockfile
- CI/CD will then fail with authentication errors when trying to install from Artifactory

**The Solution:**

**Option 1: Use the helper script (recommended)**
```bash
cd frontend
./update-lockfile.sh
```

**Option 2: Manually ensure public registry**
```bash
cd frontend

# Add a new package (always specify public registry)
npm install <package-name> --registry=https://registry.npmjs.org/

# Add a dev dependency
npm install <package-name> --save-dev --registry=https://registry.npmjs.org/

# Update existing packages
npm update --registry=https://registry.npmjs.org/

# After any install/update, verify no Artifactory URLs
grep -q "repox.jfrog.io" package-lock.json && echo "ERROR: Artifactory URLs found!" || echo "OK"
```

**Option 3: Fix lockfile after using Artifactory**
If you accidentally used Artifactory and the lockfile has Artifactory URLs:
```bash
cd frontend
# Replace all Artifactory URLs with public registry
sed -i '' 's|https://repox\.jfrog\.io/artifactory/api/npm/npm/|https://registry.npmjs.org/|g' package-lock.json
```

### Using Artifactory for Regular Installs

**Important Note**: When `package-lock.json` exists, npm uses the URLs from the lockfile, not your `.npmrc` registry setting. This means:

1. **If lockfile has public URLs** (current state):
   - `npm install` will fetch from public registry (even if .npmrc points to Artifactory)
   - To use Artifactory, it must be configured as a **transparent proxy** that mirrors `registry.npmjs.org`
   - The lockfile URLs stay public (safe for CI/CD)

2. **If you want Artifactory for local installs**:
   - Configure Artifactory as a remote repository pointing to `https://registry.npmjs.org/`
   - npm will fetch from Artifactory, but lockfile URLs remain public
   - This gives you Artifactory caching while keeping CI/CD compatibility

3. **Regular installs** (when lockfile exists):
   ```bash
   cd frontend
   npm install  # Uses URLs from lockfile (public registry)
   ```

**Summary**: The lockfile URLs determine where npm fetches from. Keep them as public registry URLs for CI/CD compatibility.

**Key Points:**
- ✅ **Lockfile URLs**: Must always be `registry.npmjs.org` (for CI/CD)
- ✅ **Adding packages**: Always use `--registry=https://registry.npmjs.org/`
- ✅ **Regular installs**: Can use Artifactory if configured as proxy, but lockfile URLs stay public
- ❌ **Never commit**: Lockfile with Artifactory URLs (will break CI/CD)

## Notes

- **CI/CD**: GitHub Actions workflows are configured to use Artifactory with authentication secrets
- **Local `.npmrc`**: The committed `.npmrc` is configured to use Artifactory
- **Maven Configuration**: Artifactory is configured by default for npm binary downloads (no profile needed)
- **npm packages**: Configured via `.npmrc` to use Artifactory
- **package-lock.json**: Uses Artifactory URLs - CI/CD is configured with authentication to handle this

