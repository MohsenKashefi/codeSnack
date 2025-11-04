# CodeSnack - Android Widget for Coding Tips

A beautiful iOS-style Android widget that displays programming tips with AI-powered content generation using Google Gemini.

## Features

- ✅ **Language Filtering** - Select specific programming languages or view tips from all languages
- ✅ **AI-Generated Tips** - 70% AI-generated tips using Google Gemini, 30% curated static tips
- ✅ **Smart Caching** - AI tips are cached locally using Room database
- ✅ **Configurable Updates** - Choose update frequency: 1 hour, 4 hours, 12 hours, or daily
- ✅ **iOS-Style Design** - Beautiful light theme with iOS color palette
- ✅ **Tap to Refresh** - Get a new tip instantly by tapping the widget
- ✅ **Hourly Auto-Updates** - Widget automatically refreshes based on your preferred schedule

## Supported Languages

- Kotlin
- Python
- JavaScript
- Java
- C++
- Swift
- Rust
- Go

## Setup

### 1. Get Gemini API Key

1. Visit [Google AI Studio](https://aistudio.google.com/app/apikey)
2. Create or sign in to your Google account
3. Click "Get API Key"
4. Copy your API key

### 2. Configure API Key

Add your API key to `local.properties` file in the project root:

```properties
GEMINI_API_KEY=your_api_key_here
```

**Note:** The `local.properties` file is git-ignored and won't be committed to version control.

### 3. Alternative: Runtime Configuration

If you prefer not to use environment variables, you can set the API key at runtime:

1. Build and install the app
2. Open the app
3. Tap the Settings icon (⚙️)
4. Enter your Gemini API key
5. Choose your preferred update frequency
6. Save settings

## How It Works

### AI Tip Generation

When AI is enabled:
- **70% of tips** are generated using Google Gemini AI
- **30% of tips** are curated static tips from the repository
- AI tips are cached locally and reused to minimize API calls
- Cache maintains up to 50 tips and automatically cleans up old ones

### Update Schedule

The widget respects your chosen update frequency:
- **1 hour** - New tip every hour
- **4 hours** - New tip every 4 hours (recommended)
- **12 hours** - New tip twice daily
- **Daily** - New tip once per day

### Language Filtering

When you add the widget:
1. Choose a programming language or "All Languages"
2. Widget only shows tips for your selected language
3. Both static and AI-generated tips respect this filter

## Project Structure

```
app/src/main/java/com/example/codesnack/
├── data/
│   ├── AiTip.kt              # Room entity for AI tips
│   ├── AiTipDao.kt            # Database operations
│   ├── AppDatabase.kt         # Room database
│   └── SnippetRepository.kt   # Static tips repository
├── model/
│   ├── CodeSnippet.kt         # Tip data model
│   ├── ProgrammingLanguage.kt # Language enum
│   └── SnippetCategory.kt     # Category enum
├── service/
│   ├── GeminiService.kt       # AI tip generation
│   └── TipProvider.kt         # Unified tip provider (AI + static)
├── widget/
│   ├── CodeSnackWidget.kt     # Main widget UI
│   ├── WidgetConfigActivity.kt # Language selection UI
│   ├── WidgetPreferences.kt   # Shared preferences
│   ├── RefreshWidgetAction.kt # Tap-to-refresh handler
│   └── CodeSnackWidgetReceiver.kt # Widget lifecycle
├── worker/
│   ├── WidgetUpdateWorker.kt  # Background updates
│   └── WidgetWorkScheduler.kt # Update scheduling
├── MainActivity.kt            # Main app UI
└── SettingsActivity.kt        # Settings UI
```

## Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test
```

## Dependencies

- **Jetpack Compose** - Modern UI toolkit
- **Glance** - Widget framework
- **Room** - Local database
- **WorkManager** - Background scheduling
- **Google Gemini AI** - AI tip generation
- **Kotlin Coroutines** - Async operations

## Privacy

- API key is stored locally and never shared
- AI tips are cached locally using Room database
- No user data is collected or transmitted except API calls to Google Gemini
- Widget data is stored in app-private shared preferences

## License

This project is for educational purposes.

## Credits

Built with ❤️ using Kotlin, Jetpack Compose, and Google Gemini AI.
