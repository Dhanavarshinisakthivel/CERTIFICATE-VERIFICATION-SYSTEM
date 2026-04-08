#!/bin/bash
# =====================================================
# Quick Setup Script
# Run: chmod +x deploy.sh && ./deploy.sh
# =====================================================
echo ""
echo "🚀 Blockchain Certificate Verification System - Setup"
echo "======================================================"

# Check Java
if ! command -v java &> /dev/null; then echo "❌ Java not found. Install Java 17+"; exit 1; fi
echo "✅ Java: $(java -version 2>&1 | head -1)"

# Check Maven
if ! command -v mvn &> /dev/null; then echo "❌ Maven not found. Install Maven 3.8+"; exit 1; fi
echo "✅ Maven: $(mvn -version 2>&1 | head -1)"

# Check MySQL
if ! command -v mysql &> /dev/null; then echo "⚠️  MySQL CLI not found. Make sure MySQL server is running."; fi

echo ""
echo "📦 Building backend..."
cd backend
mvn clean package -DskipTests -q
echo "✅ Build complete!"

echo ""
echo "🗄️  To setup database, run:"
echo "   mysql -u root -p < ../database/schema.sql"

echo ""
echo "🚀 Starting backend server..."
java -jar target/blockchain-cert-system-1.0.0.jar
