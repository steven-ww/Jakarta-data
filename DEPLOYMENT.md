# Jakarta EE 11 Application Deployment Guide

This guide explains how to deploy the Jakarta EE 11 application to GlassFish using Maven.

## Prerequisites

1. **GlassFish 8.0.0 or later** (supports Jakarta EE 11)
2. **Java 21** or later
3. **Maven 3.9** or later

## Installation

### 1. Install GlassFish

Download and install GlassFish from:
- https://glassfish.org/download

Or using Homebrew (macOS):
```bash
brew install glassfish
```

### 2. Start GlassFish

```bash
# Navigate to GlassFish installation directory
cd /path/to/glassfish8/bin

# Start the domain
./asadmin start-domain domain1
```

### 3. Create Admin Password File (Optional)

For automatic deployment, create a password file:

```bash
# Create password file
echo "AS_ADMIN_PASSWORD=" > ~/.asadminpass
chmod 600 ~/.asadminpass
```

## Deployment Options

### Option 1: Using GlassFish Admin Console

1. Build the application:
   ```bash
   mvn clean package
   ```

2. Open GlassFish Admin Console:
   - URL: http://localhost:4848
   - Username: admin
   - Password: (empty by default)

3. Navigate to Applications → Deploy
4. Upload `target/jakarta-ee-app.war`
5. Click Deploy

### Option 2: Using asadmin Command

1. Build the application:
   ```bash
   mvn clean package
   ```

2. Deploy using asadmin:
   ```bash
   # Navigate to GlassFish bin directory
   cd /path/to/glassfish8/bin
   
   # Deploy the application
   ./asadmin deploy /path/to/your/project/target/jakarta-ee-app.war
   ```

### Option 3: Using Maven GlassFish Plugin

#### Setup

1. Set the GlassFish home directory:
   ```bash
   export GLASSFISH_HOME=/path/to/glassfish8
   ```

2. Or set it in your Maven command:
   ```bash
   mvn clean package glassfish:deploy -Dglassfish.home=/path/to/glassfish8
   ```

#### Deploy Commands

```bash
# Build and deploy
mvn clean package glassfish:deploy -Dglassfish.home=/path/to/glassfish8

# Undeploy
mvn glassfish:undeploy -Dglassfish.home=/path/to/glassfish8

# Redeploy
mvn glassfish:redeploy -Dglassfish.home=/path/to/glassfish8
```

### Option 4: Using Cargo Maven Plugin

```bash
# Deploy using Cargo
mvn clean package cargo:deploy

# Undeploy
mvn cargo:undeploy

# Redeploy
mvn cargo:redeploy
```

## Manual Deployment Steps

If you prefer manual deployment:

1. **Build the application:**
   ```bash
   mvn clean package
   ```

2. **Copy WAR file to GlassFish:**
   ```bash
   cp target/jakarta-ee-app.war /path/to/glassfish8/glassfish/domains/domain1/autodeploy/
   ```

3. **GlassFish will automatically deploy the application**

## Testing the Deployment

Once deployed, test the application:

```bash
# Health check
curl http://localhost:8080/jakarta-ee-app/api/hello/health

# Create a greeting
curl "http://localhost:8080/jakarta-ee-app/api/hello?name=World"

# Get all greetings
curl http://localhost:8080/jakarta-ee-app/api/hello/greetings
```

## Web Interface

Access the web interface at:
- http://localhost:8080/jakarta-ee-app/

## Database Configuration

The application uses GlassFish's default datasource. For production deployment:

1. **Configure a proper database connection pool in GlassFish**
2. **Update the JNDI name in persistence.xml if needed**
3. **Add your database driver to GlassFish's lib directory**

### Default Database Setup

The application is configured to use GlassFish's default datasource (`java:app/jdbc/DefaultDataSource`). 

For development, you can set up a simple database connection:

1. **Access GlassFish Admin Console** (http://localhost:4848)
2. **Navigate to Resources → JDBC → Connection Pools**
3. **Create a new connection pool** or use the default one
4. **Navigate to Resources → JDBC → JDBC Resources**
5. **Ensure `java:app/jdbc/DefaultDataSource` points to your database**

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 8080 and 4848 are available
2. **Java version**: Make sure Java 21 is being used
3. **Database connection**: Check that the datasource is properly configured
4. **WAR file**: Verify the WAR file was built successfully

### Logs

Check GlassFish logs for deployment issues:
```bash
tail -f /path/to/glassfish8/glassfish/domains/domain1/logs/server.log
```

## Production Considerations

For production deployment:

1. **Configure proper security settings**
2. **Set up SSL/TLS certificates**  
3. **Configure connection pooling**
4. **Set up monitoring and logging**
5. **Configure clustering if needed**
6. **Set up backup and recovery procedures**
