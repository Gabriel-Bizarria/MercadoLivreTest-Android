# MercadoLivre Dev Test

A modern Android application built for MercadoLivre development testing, showcasing best practices in Android development with Jetpack Compose, MVVM architecture, and modern Android libraries.

## 🚀 Features

- **Product Search**: Search for products on MercadoLivre
- **Search Results**: Display search results with pagination
- **Product Details**: View detailed product information
- **Modern UI**: Built with Jetpack Compose and Material 3
- **Offline Support**: Cached data for better user experience

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Koin
- **Networking**: Ktor Client
- **Image Loading**: Coil
- **Pagination**: Android Paging 3
- **Testing**: JUnit, MockK, Turbine
- **Serialization**: Kotlinx Serialization

## 📱 Requirements

- **Minimum SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)
- **Java Version**: 11
- **Kotlin Version**: Latest stable

## 🏗 Project Structure

```
app/src/main/java/br/com/devtest/mercadolivre/
├── data/                    # Data layer
│   ├── datasource/         # Remote and local data sources
│   ├── models/             # Data models
│   ├── repository/         # Repository implementations
│   └── utils/              # Data utilities
├── domain/                 # Domain layer
│   └── repository/         # Repository interfaces
├── ui/                     # Presentation layer
│   ├── commons/            # Common UI components
│   ├── models/             # UI models
│   ├── navigation/         # Navigation components
│   ├── screens/            # Screen implementations
│   │   ├── productdetail/  # Product detail screen
│   │   ├── search/         # Search screen
│   │   └── searchresult/   # Search results screen
│   ├── state/              # UI state management
│   ├── theme/              # App theme and styling
│   └── viewmodels/         # ViewModels
├── di/                     # Dependency injection
├── utils/                  # Utility classes
├── MainActivity.kt         # Main activity
└── MainApplication.kt      # Application class
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK 35

### Installation

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd MercadoLivreDevTest
   ```

2. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Run the app on an emulator or physical device

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## 🧪 Testing

The project includes comprehensive testing with:

- **Unit Tests**: Using JUnit and MockK
- **UI Tests**: Using Espresso and Compose testing
- **Coroutines Testing**: Using Turbine for flow testing

Run tests with:

```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests
```

## 📦 Dependencies

### Core Dependencies

- **AndroidX Core KTX**: Core Android functionality
- **Jetpack Compose**: Modern UI toolkit
- **Material 3**: Material Design components
- **Navigation Compose**: Navigation between screens

### Networking & Data

- **Ktor Client**: HTTP client for API calls
- **Kotlinx Serialization**: JSON serialization
- **Android Paging**: Pagination support

### Dependency Injection

- **Koin**: Lightweight dependency injection

### Image Loading

- **Coil**: Image loading and caching

### Testing

- **JUnit**: Unit testing framework
- **MockK**: Mocking library
- **Turbine**: Coroutines testing
- **Espresso**: UI testing

## 🏛 Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture with clean architecture principles:

- **Presentation Layer**: UI components, ViewModels, and state management
- **Domain Layer**: Business logic and repository interfaces
- **Data Layer**: Repository implementations, data sources, and models

### Key Architectural Components

- **Repository Pattern**: Abstracts data sources
- **Use Cases**: Business logic encapsulation
- **State Management**: UI state handling with Compose
- **Dependency Injection**: Koin for DI management

## 🎨 UI/UX

- **Material 3 Design**: Modern Material Design implementation
- **Jetpack Compose**: Declarative UI development
- **Responsive Design**: Adapts to different screen sizes
- **Dark/Light Theme**: Theme support (if implemented)

## 🔧 Configuration

### Build Variants

- **Debug**: Development build with debugging enabled
- **Release**: Production build with optimizations
