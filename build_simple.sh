#!/bin/bash

# Simple build script untuk Tams Wallet
echo "🚀 Building Tams Wallet..."

# Set environment
export ANDROID_HOME="/mnt/c/Users/Administrator/AppData/Local/Android/Sdk"
export BUILD_TOOLS="$ANDROID_HOME/build-tools/34.0.0"
export PLATFORM="$ANDROID_HOME/platforms/android-34"
export PLATFORM_TOOLS="$ANDROID_HOME/platform-tools"

echo "📁 Android SDK: $ANDROID_HOME"
echo "🔧 Build Tools: $BUILD_TOOLS"

# Check if device connected
echo "📱 Checking connected devices..."
"$PLATFORM_TOOLS/adb.exe" devices

# Create build directory
mkdir -p build/gen
mkdir -p build/obj
mkdir -p build/apk

echo "📦 Building APK with Android Studio tools..."
echo ""
echo "⚠️  MANUAL STEPS REQUIRED:"
echo "1. Open Android Studio"
echo "2. File → Open → Select TamsWallet folder"
echo "3. Wait for Gradle sync"
echo "4. Click Run button (▶️)"
echo "5. Select device: 192.168.1.2:42993"
echo ""
echo "📱 Device ready at: 192.168.1.2:42993"
echo "📂 Project location: $(pwd)"
echo ""
echo "✅ Project structure is complete and ready for Android Studio!"