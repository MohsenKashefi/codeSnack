## CodeSnack — Android Widget for Coding Tips

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

An Android app and homescreen widget that surfaces concise coding tips. Tips are sourced from a small curated set and optionally generated with Google Gemini, then cached locally for quick, offline-friendly access.

### Highlights
- **Homescreen widget**: Glance-based widget with tap‑to‑refresh and periodic updates
- **AI + static tips**: Gemini‑generated tips blended with curated snippets
- **Language filter**: Pick a language or show tips from all languages
- **Local caching**: Room‑backed cache to reduce network calls
- **Battery‑friendly scheduling**: WorkManager for periodic refreshes

### Tech stack
- **Kotlin**, **Jetpack Compose**, **Glance (AppWidget)**
- **Room**, **WorkManager**, **Coroutines**
- **Google Gemini** client SDK, **OkHttp**

## Requirements
- Android **minSdk 24**, target/compile **36**
- A Google Gemini API key

## Setup
1) **Get an API key**: from Google AI Studio (`https://aistudio.google.com/app/apikey`).
2) **Create a `.env` file** at the project root with:

```env
GEMINI_API_KEY=your_api_key_here
```

The build reads this value and exposes it as `BuildConfig.GEMINI_API_KEY`. Do not commit secrets.

## Build & run
```bash
./gradlew assembleDebug      # build
./gradlew installDebug       # install to device/emulator
./gradlew test               # unit tests
```

## Using the widget
1) Long‑press the homescreen → Add widgets → CodeSnack
2) Choose a programming language (or All)
3) Optionally set update frequency in Settings
4) Tap the widget to fetch a fresh tip on demand

## Project layout (high level)
```text
app/src/main/java/com/example/codesnack/
  data/           # Room entities + DAO + database
  model/          # Core models and enums
  service/        # Gemini integration + tip provider
  widget/         # Glance UI, config activity, receiver
  worker/         # WorkManager scheduling + worker
  MainActivity.kt
  SettingsActivity.kt
```

## Privacy
- API key stays on device and is used only to call Gemini
- Tips cached locally; no unsolicited analytics
- Preferences are stored in app‑private storage

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

The MIT License is a permissive open source license that allows you to use, modify, and distribute this code freely, even for commercial purposes, as long as you include the original copyright notice.
