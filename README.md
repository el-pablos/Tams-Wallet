# 🏦 Tams Wallet - Premium Personal Finance App

<div align="center">
  <img src="https://img.shields.io/badge/Android-Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/Material_Design-757575?style=for-the-badge&logo=material-design&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
</div>

## 🎨 Premium Design System

**Gojek-Level UI/UX** with smooth animations and premium feel:

### Color Palette
- **Primary Black**: `#000000` - Bold, professional
- **Mint Green**: `#CFFFE2` - Fresh, modern accent  
- **Sage Green**: `#A2D5C6` - Calming secondary
- **Clean White**: `#F6F6F6` - Pristine background

### Design Features
- 🔥 **Shimmer Loading Effects** for premium feel
- 🎭 **Lottie Animations** for smooth interactions
- 💳 **Card-based UI** with subtle shadows
- 📱 **Responsive Design** for all screen sizes
- ⚡ **60fps Smooth Scrolling** with CoordinatorLayout

## 🚀 Features

### 💰 Financial Management
- **Real-time Balance Tracking** with Firebase sync
- **Transaction Categories** (Income/Expense)
- **Budget Planning** with visual progress
- **Smart Analytics** with MPAndroidChart

### 🔐 Security & Auth
- **Biometric Authentication** (Fingerprint/Face)
- **Firebase Authentication** for secure login
- **Data Encryption** for sensitive information

### 📊 Analytics & Reports
- **Interactive Charts** for spending analysis
- **Export to CSV/JSON** for external use
- **Monthly/Weekly Reports** with insights

### 🎯 Modern UX
- **Bottom Navigation** with smooth transitions
- **Floating Action Button** for quick actions
- **Swipe Refresh** for data updates
- **Dark Mode Support** for user preference

## 🏗️ Architecture

### Tech Stack
```
📱 Frontend: Java + XML (Material Design 3)
🔥 Backend: Firebase Realtime Database
🗄️ Local Storage: Room Database (SQLite)
📊 Charts: MPAndroidChart
🎨 Animations: Lottie + Shimmer
```

### Project Structure
```
app/
├── src/main/java/com/tamswallet/app/
│   ├── ui/
│   │   ├── auth/          # Login, Register, Onboarding
│   │   ├── main/          # Dashboard, History, Budget
│   │   └── settings/      # Profile, Preferences
│   ├── database/          # Room entities, DAOs
│   ├── utils/             # Firebase, Currency, Date utils
│   └── models/            # Data models
└── res/
    ├── layout/            # XML layouts
    ├── values/            # Colors, strings, dimensions
    └── drawable/          # Icons, backgrounds
```

## 🔧 Setup & Installation

### Prerequisites
- Android Studio Arctic Fox+
- Android SDK 34
- Java 8+
- Firebase account

### Firebase Configuration
1. **Database URL**: `https://tams-wallet-default-rtdb.asia-southeast1.firebasedatabase.app/`
2. **Environment**: See `firebase_config.env`
3. **Security Rules**: User-specific read/write access

### Installation Steps
```bash
# 1. Clone repository
git clone https://github.com/el-pablos/Tams-Wallet.git

# 2. Open in Android Studio
File → Open → Select TamsWallet folder

# 3. Sync Gradle dependencies
Build → Rebuild Project

# 4. Connect device & run
Run → Click ▶️ (Shift+F10)
```

## 🔥 Firebase Integration

### Database Structure
```json
{
  "users": {
    "{userId}": {
      "profile": { "name", "email", "avatar" },
      "settings": { "currency", "theme", "notifications" }
    }
  },
  "transactions": {
    "{userId}": {
      "{transactionId}": {
        "amount", "type", "category", "date", "description"
      }
    }
  },
  "budgets": {
    "{userId}": {
      "{budgetId}": {
        "category", "limit", "spent", "period"
      }
    }
  }
}
```

### Environment Variables
Configure in `firebase_config.env`:
```env
FIREBASE_DATABASE_URL=https://tams-wallet-default-rtdb.asia-southeast1.firebasedatabase.app/
FIREBASE_PROJECT_ID=tams-wallet-default-rtdb
```

## 🎨 Design System

### Typography
- **Headlines**: `sans-serif-medium` for impact
- **Body Text**: `sans-serif` for readability
- **Responsive Sizing**: Scales for tablets

### Spacing & Layout
- **Base Unit**: 8dp grid system
- **Card Padding**: 16dp standard
- **Screen Margins**: 16dp phone, 24dp tablet
- **Icon Sizes**: 24dp, 32dp, 48dp variants

### Component Guidelines
- **Cards**: 8dp corner radius, 2-4dp elevation
- **Buttons**: 48dp height, ripple effects
- **FAB**: Anchored to bottom navigation
- **Loading**: Shimmer for premium feel

## 🛡️ Security Features

### Data Protection
- **Firebase Security Rules** for user isolation
- **Local Encryption** for sensitive data
- **Biometric Authentication** for quick access
- **Session Management** with auto-logout

### Privacy
- **No Third-party Tracking** in production
- **User Data Control** with export/delete options
- **Offline-first** approach for privacy

## 📱 Responsive Design

### Phone (< 600dp)
- Single column layout
- Compact navigation
- Touch-friendly sizing

### Tablet (≥ 600dp)
- Multi-column layouts
- Larger text sizes
- Enhanced spacing

### Adaptive Features
- **Dynamic Typography** scaling
- **Flexible Grids** for content
- **Context-aware** interactions

## 🚀 Performance Optimizations

### Loading & Caching
- **Firebase Persistence** for offline use
- **Image Caching** with optimized loading
- **Lazy Loading** for large lists
- **Preloading** critical data

### Smooth Animations
- **60fps Target** with proper threading
- **Shared Element Transitions** between screens
- **Hardware Acceleration** enabled
- **Memory Management** optimized

## 🧪 Testing

### Test Coverage
```bash
# Unit Tests
./gradlew test

# Instrumentation Tests  
./gradlew connectedAndroidTest

# UI Tests
./gradlew connectedDebugAndroidTest
```

## 📈 Development Roadmap

### Phase 1: Core Features ✅
- [x] Authentication & User Management
- [x] Transaction CRUD Operations
- [x] Budget Tracking
- [x] Dashboard Analytics

### Phase 2: Enhanced UX 🔄
- [ ] Advanced Charts & Insights
- [ ] Export/Import Functionality
- [ ] Dark Mode Implementation
- [ ] Onboarding Experience

### Phase 3: Premium Features 📋
- [ ] Cloud Backup & Sync
- [ ] Multi-currency Support
- [ ] Goal Setting & Tracking
- [ ] Smart Notifications

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'add: amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### Commit Convention
- `add:` New features
- `fix:` Bug fixes
- `change:` Modifications to existing features
- `delete:` Removed code/features

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Material Design** for design guidelines
- **Firebase** for backend infrastructure
- **MPAndroidChart** for beautiful charts
- **Lottie** for smooth animations

---

<div align="center">
  <p><strong>Built with ❤️ for modern financial management</strong></p>
  <p>© 2024 Tams Wallet. Premium Personal Finance Solution.</p>
</div>