# JavaDoc Generation Guide

## Issue

When running `mvn javadoc:javadoc` in the backend directory, you may encounter dependency resolution errors because the parent POM and frontend module haven't been installed to your local Maven repository yet.

## Solution

Build and install all modules first, then generate JavaDoc:

### Step 1: Build and Install All Modules

From the **project root** directory:

```bash
mvn clean install -DskipTests
```

This will:
1. Build the parent POM
2. Build the frontend module (packages React app as JAR)
3. Build the backend module
4. Install all artifacts to your local Maven repository (`~/.m2/repository`)

### Step 2: Generate JavaDoc

After the build completes successfully, generate JavaDoc:

```bash
cd backend
mvn javadoc:javadoc
```

### Step 3: View JavaDoc

Open the generated JavaDoc in your browser:

```bash
open backend/target/site/apidocs/index.html
```

Or navigate to: `backend/target/site/apidocs/index.html`

## Alternative: Generate JavaDoc from Root

You can also generate JavaDoc from the root directory:

```bash
mvn javadoc:javadoc -pl backend
```

The `-pl backend` flag tells Maven to only run the goal on the backend module.

## Troubleshooting

### Error: "Could not find artifact com.sonarshowcase:sonarshowcase-parent"

**Solution:** Run `mvn clean install` from the project root first.

### Error: "JAVA_HOME environment variable is not defined"

**Solution:** Set JAVA_HOME to point to your JDK installation:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

### Error: "Failed to read artifact descriptor for sonarshowcase-frontend"

**Solution:** The frontend module needs to be built first. Run `mvn clean install` from the root directory.

## JavaDoc Output Location

- **HTML files:** `backend/target/site/apidocs/`
- **Index page:** `backend/target/site/apidocs/index.html`


## Maven Plugin Configuration

The JavaDoc plugin is configured in the parent POM. If you need to customize JavaDoc generation, you can add configuration to `backend/pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <configuration>
        <source>21</source>
        <target>21</target>
        <encoding>UTF-8</encoding>
    </configuration>
</plugin>
```

---

*Last Updated: January 2025*

