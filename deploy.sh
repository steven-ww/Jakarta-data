#!/bin/bash

# Jakarta EE 11 Application Deployment Script
# This script builds and deploys the application to GlassFish

set -e

# Configuration
PROJECT_NAME="jakarta-ee-app"
GLASSFISH_HOME=${GLASSFISH_HOME:-"$HOME/Downloads/glassfish8"}
ADMIN_PORT=${ADMIN_PORT:-"4848"}
HTTP_PORT=${HTTP_PORT:-"8080"}
DOMAIN_NAME=${DOMAIN_NAME:-"domain1"}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if GlassFish is running
check_glassfish_running() {
    if curl -s -o /dev/null -w "%{http_code}" http://localhost:${ADMIN_PORT} | grep -q "200\|302"; then
        return 0
    else
        return 1
    fi
}

# Function to start GlassFish if not running
start_glassfish() {
    if check_glassfish_running; then
        print_status "GlassFish is already running"
    else
        print_status "Starting GlassFish..."
        if [ -f "${GLASSFISH_HOME}/bin/asadmin" ]; then
            "${GLASSFISH_HOME}/bin/asadmin" start-domain ${DOMAIN_NAME}
            sleep 5
            if check_glassfish_running; then
                print_status "GlassFish started successfully"
            else
                print_error "Failed to start GlassFish"
                exit 1
            fi
        else
            print_error "GlassFish not found at ${GLASSFISH_HOME}"
            print_error "Please set GLASSFISH_HOME environment variable or install GlassFish"
            exit 1
        fi
    fi
}

# Function to build the application
build_application() {
    print_status "Building application..."
    mvn clean package -DskipTests
    if [ $? -eq 0 ]; then
        print_status "Application built successfully"
    else
        print_error "Failed to build application"
        exit 1
    fi
}

# Function to deploy application
deploy_application() {
    print_status "Deploying application..."
    
    # Check if application is already deployed
    if "${GLASSFISH_HOME}/bin/asadmin" list-applications | grep -q "${PROJECT_NAME}"; then
        print_warning "Application ${PROJECT_NAME} is already deployed. Redeploying..."
        "${GLASSFISH_HOME}/bin/asadmin" redeploy "target/${PROJECT_NAME}.war"
    else
        print_status "Deploying new application..."
        "${GLASSFISH_HOME}/bin/asadmin" deploy "target/${PROJECT_NAME}.war"
    fi
    
    if [ $? -eq 0 ]; then
        print_status "Application deployed successfully"
    else
        print_error "Failed to deploy application"
        exit 1
    fi
}

# Function to test deployment
test_deployment() {
    print_status "Testing deployment..."
    
    # Wait a moment for deployment to complete
    sleep 3
    
    # Test health endpoint
    HEALTH_URL="http://localhost:${HTTP_PORT}/${PROJECT_NAME}/api/hello/health"
    
    if curl -s -f "${HEALTH_URL}" > /dev/null; then
        print_status "Health check passed"
        print_status "Application is available at: http://localhost:${HTTP_PORT}/${PROJECT_NAME}/"
        print_status "API endpoints available at: http://localhost:${HTTP_PORT}/${PROJECT_NAME}/api/hello/"
    else
        print_warning "Health check failed, but deployment may still be successful"
        print_status "Check application at: http://localhost:${HTTP_PORT}/${PROJECT_NAME}/"
    fi
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [options]"
    echo "Options:"
    echo "  -h, --help          Show this help message"
    echo "  -b, --build-only    Only build the application"
    echo "  -d, --deploy-only   Only deploy (skip build)"
    echo "  -s, --start         Start GlassFish if not running"
    echo "  -t, --test          Test deployment after deploy"
    echo ""
    echo "Environment Variables:"
    echo "  GLASSFISH_HOME      Path to GlassFish installation (default: /usr/local/glassfish8)"
    echo "  ADMIN_PORT          GlassFish admin port (default: 4848)"
    echo "  HTTP_PORT           GlassFish HTTP port (default: 8080)"
    echo "  DOMAIN_NAME         GlassFish domain name (default: domain1)"
}

# Parse command line arguments
BUILD_ONLY=false
DEPLOY_ONLY=false
START_GLASSFISH=false
TEST_DEPLOYMENT=false

while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_usage
            exit 0
            ;;
        -b|--build-only)
            BUILD_ONLY=true
            shift
            ;;
        -d|--deploy-only)
            DEPLOY_ONLY=true
            shift
            ;;
        -s|--start)
            START_GLASSFISH=true
            shift
            ;;
        -t|--test)
            TEST_DEPLOYMENT=true
            shift
            ;;
        *)
            print_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Main execution
print_status "Starting deployment process for ${PROJECT_NAME}"
print_status "GlassFish Home: ${GLASSFISH_HOME}"

# Check if Java is available
if ! command -v java &> /dev/null; then
    print_error "Java is not installed or not in PATH"
    exit 1
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed or not in PATH"
    exit 1
fi

# Start GlassFish if requested
if [ "$START_GLASSFISH" = true ]; then
    start_glassfish
fi

# Build application
if [ "$DEPLOY_ONLY" = false ]; then
    build_application
fi

# Deploy application
if [ "$BUILD_ONLY" = false ]; then
    if ! check_glassfish_running; then
        print_error "GlassFish is not running. Start it first or use -s flag."
        exit 1
    fi
    
    deploy_application
fi

# Test deployment
if [ "$TEST_DEPLOYMENT" = true ] && [ "$BUILD_ONLY" = false ]; then
    test_deployment
fi

print_status "Deployment process completed successfully!"
