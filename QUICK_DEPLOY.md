# Quick Deployment Guide

## Fixed: The deployment script now works correctly!

The issue was that the tilde (`~`) wasn't being expanded properly in the script. It's now fixed to use `$HOME/Downloads/glassfish8`.

## Quick Deployment Options

### Option 1: Using the Fixed Script (Recommended)

```bash
# Build only
./deploy.sh -b

# Start GlassFish and deploy with testing
./deploy.sh -s -t

# Just deploy (if already built)
./deploy.sh -d
```

### Option 2: Manual Maven Commands

```bash
# Build the application
mvn clean package

# Deploy using Maven with explicit path
mvn glassfish:deploy -Dglassfish.home=$HOME/Downloads/glassfish8

# Or set environment variable first
export GLASSFISH_HOME=$HOME/Downloads/glassfish8
mvn glassfish:deploy
```

### Option 3: Direct asadmin Commands

```bash
# Build first
mvn clean package

# Start GlassFish
~/Downloads/glassfish8/bin/asadmin start-domain domain1

# Deploy
~/Downloads/glassfish8/bin/asadmin deploy target/jakarta-ee-app.war

# Check deployment
~/Downloads/glassfish8/bin/asadmin list-applications
```

### Option 4: Auto-deploy (Simplest)

```bash
# Build
mvn clean package

# Start GlassFish
~/Downloads/glassfish8/bin/asadmin start-domain domain1

# Copy to auto-deploy directory
cp target/jakarta-ee-app.war ~/Downloads/glassfish8/glassfish/domains/domain1/autodeploy/

# GlassFish will automatically deploy it
```

## Testing the Deployment

Once deployed, test with:

```bash
# Health check
curl http://localhost:8080/jakarta-ee-app/api/hello/health

# Create a greeting
curl "http://localhost:8080/jakarta-ee-app/api/hello?name=World"

# View in browser
open http://localhost:8080/jakarta-ee-app/
```

## Common Issues Fixed

1. **Path expansion**: Changed `~/Downloads/glassfish8` to `$HOME/Downloads/glassfish8`
2. **File existence**: Verified `asadmin` exists at the correct location
3. **Debugging**: Added path verification in the script

The script now properly detects your GlassFish installation and should work correctly!
