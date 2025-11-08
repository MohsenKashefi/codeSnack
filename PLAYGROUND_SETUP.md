# Code Playground Setup

The Interactive Code Playground feature uses Judge0 API via RapidAPI to execute code in real-time.

## Getting Your Free API Key (Takes 2 minutes)

### Step 1: Sign Up for RapidAPI (Free)
1. Go to: https://rapidapi.com/
2. Click "Sign Up" (top right)
3. Create a free account (use Google/GitHub for quick signup)

### Step 2: Subscribe to Judge0 CE (Free Tier)
1. Go to: https://rapidapi.com/judge0-official/api/judge0-ce
2. Click **"Subscribe to Test"** button
3. Select the **"BASIC (Free)"** plan:
   - ✅ **50 requests/day** (plenty for testing!)
   - ✅ **$0.00/month**
   - ✅ No credit card required
4. Click **"Subscribe"**

### Step 3: Get Your API Key
1. On the Judge0 CE page, go to the **"Endpoints"** tab
2. Look for **"X-RapidAPI-Key"** in the code snippet on the right
3. Copy your API key (looks like: `a1b2c3d4e5f6g7h8i9j0...`)

### Step 4: Add Key to Your App
1. Open: `app/src/main/java/com/example/codesnack/playground/CodeExecutionService.kt`
2. Find line 14:
   ```kotlin
   private val RAPIDAPI_KEY = "YOUR_RAPIDAPI_KEY_HERE"
   ```
3. Replace `YOUR_RAPIDAPI_KEY_HERE` with your actual key:
   ```kotlin
   private val RAPIDAPI_KEY = "a1b2c3d4e5..."
   ```
4. Save the file
5. Rebuild the app: `./gradlew assembleDebug`

## Done! 🎉

Now the Code Playground will execute code successfully!

## API Limits (Free Tier)
- **50 requests/day** - Perfect for testing and demos
- Resets every 24 hours
- For production: upgrade to higher tier (starting at $10/month for 1000 requests/day)

## Supported Languages
All languages in CodeSnack are supported:
- ✅ Kotlin
- ✅ Python
- ✅ JavaScript
- ✅ Java
- ✅ C++
- ✅ Swift
- ✅ Rust
- ✅ Go
- ✅ Dart

## Testing
1. Open the app
2. Tap any code snippet
3. Click **"▶ Try Code"**
4. Edit the code
5. Click **"Run Code"**
6. See output! ⚡

## Troubleshooting

### Error: "HTTP 401"
- Your API key is missing or incorrect
- Double-check you copied the full key
- Make sure there are no extra spaces

### Error: "HTTP 429"
- You've exceeded 50 requests/day
- Wait for daily reset or upgrade plan

### No internet connection
- Code execution requires internet
- Check your network connection

## Alternative (Self-Hosted)
If you want unlimited requests, you can run Judge0 locally:
1. Install Docker
2. Run: `docker run -p 2358:2358 judge0/judge0:latest`
3. Change baseUrl in CodeExecutionService.kt to: `http://localhost:2358/`

## Security Note
⚠️ **Never commit your API key to GitHub!**
- Add to `.gitignore`
- Consider moving to BuildConfig or environment variables for production