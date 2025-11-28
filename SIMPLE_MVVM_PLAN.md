# Simple MVVM Implementation Plan

## Overview
This is a **minimal MVVM setup** that adds only what's necessary:
- ✅ One new folder: `view_model/`
- ✅ One ViewModel: `AuthViewModel` (handles all auth operations)
- ✅ Simple API service interface
- ✅ Simple Retrofit setup
- ✅ Optional: Dependency Injection with Hilt (recommended for better architecture)
- ✅ Keep all existing folders and structure as-is

## Current Structure (No Changes)
```
app/src/main/java/com/joyersapp/
├── auth/                    # Keep as-is
├── common_widgets/         # Keep as-is
├── response/                # Keep as-is (use existing models)
├── theme/                  # Keep as-is
├── utils/                  # Keep as-is
├── MainActivity.kt          # Keep as-is
└── NavGraph.kt             # Keep as-is
```

## What Will Be Added

### 1. New Folder Structure
```
app/src/main/java/com/joyersapp/
└── view_model/             # NEW FOLDER
    └── AuthViewModel.kt    # Single ViewModel for all auth
```

### 2. New Files to Create

#### A. `api/AuthApiService.kt` (Simple API interface)
```kotlin
package com.joyersapp.api

import com.joyersapp.response.CheckUserNameModel
import retrofit2.http.*

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse
    
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ForgotPasswordResponse
    
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse
    
    @GET("auth/check-username")
    suspend fun checkUsername(@Query("username") username: String): CheckUserNameModel
    
    @GET("titles")  // If you need this
    suspend fun getTitles(): TitlesApiResponse
}
```

#### B. `api/RetrofitClient.kt` (Simple Retrofit setup)
```kotlin
package com.joyersapp.api

import com.joyersapp.api.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "YOUR_BASE_URL_HERE" // Update this
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val authApiService: AuthApiService = retrofit.create(AuthApiService::class.java)
}
```

#### C. `view_model/AuthViewModel.kt` (Single ViewModel for all auth)
```kotlin
package com.joyersapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.api.RetrofitClient
import com.joyersapp.response.CheckUserNameModel
import com.joyersapp.response.TitlesApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    
    // Login State
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    // SignUp State
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()
    
    // Forgot Password State
    private val _forgotPasswordState = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState: StateFlow<ForgotPasswordState> = _forgotPasswordState.asStateFlow()
    
    // Reset Password State
    private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Idle)
    val resetPasswordState: StateFlow<ResetPasswordState> = _resetPasswordState.asStateFlow()
    
    // Check Username State
    private val _checkUsernameState = MutableStateFlow<CheckUsernameState>(CheckUsernameState.Idle)
    val checkUsernameState: StateFlow<CheckUsernameState> = _checkUsernameState.asStateFlow()
    
    // Titles State
    private val _titlesState = MutableStateFlow<TitlesState>(TitlesState.Idle)
    val titlesState: StateFlow<TitlesState> = _titlesState.asStateFlow()
    
    // ========== LOGIN ==========
    fun login(username: String, password: String, rememberMe: Boolean = false) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = RetrofitClient.authApiService.login(
                    LoginRequest(username, password, rememberMe)
                )
                _loginState.value = LoginState.Success(response)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            }
        }
    }
    
    // ========== SIGN UP ==========
    fun signUp(request: SignUpRequest) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                val response = RetrofitClient.authApiService.signUp(request)
                _signUpState.value = SignUpState.Success(response)
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error(e.message ?: "Sign up failed")
            }
        }
    }
    
    // ========== FORGOT PASSWORD ==========
    fun forgotPassword(identifier: String, isPhone: Boolean) {
        viewModelScope.launch {
            _forgotPasswordState.value = ForgotPasswordState.Loading
            try {
                val response = RetrofitClient.authApiService.forgotPassword(
                    ForgotPasswordRequest(identifier, isPhone)
                )
                _forgotPasswordState.value = ForgotPasswordState.Success(response)
            } catch (e: Exception) {
                _forgotPasswordState.value = ForgotPasswordState.Error(e.message ?: "Request failed")
            }
        }
    }
    
    // ========== RESET PASSWORD ==========
    fun resetPassword(
        identifier: String,
        countryCode: String,
        countryNameCode: String,
        verificationCode: String,
        newPassword: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            _resetPasswordState.value = ResetPasswordState.Loading
            try {
                val response = RetrofitClient.authApiService.resetPassword(
                    ResetPasswordRequest(
                        identifier, countryCode, countryNameCode,
                        verificationCode, newPassword, confirmPassword
                    )
                )
                _resetPasswordState.value = ResetPasswordState.Success(response)
            } catch (e: Exception) {
                _resetPasswordState.value = ResetPasswordState.Error(e.message ?: "Reset failed")
            }
        }
    }
    
    // ========== CHECK USERNAME ==========
    fun checkUsername(username: String) {
        viewModelScope.launch {
            _checkUsernameState.value = CheckUsernameState.Loading
            try {
                val response = RetrofitClient.authApiService.checkUsername(username)
                _checkUsernameState.value = CheckUsernameState.Success(response)
            } catch (e: Exception) {
                _checkUsernameState.value = CheckUsernameState.Error(e.message ?: "Check failed")
            }
        }
    }
    
    // ========== GET TITLES ==========
    fun getTitles() {
        viewModelScope.launch {
            _titlesState.value = TitlesState.Loading
            try {
                val response = RetrofitClient.authApiService.getTitles()
                _titlesState.value = TitlesState.Success(response)
            } catch (e: Exception) {
                _titlesState.value = TitlesState.Error(e.message ?: "Failed to load titles")
            }
        }
    }
    
    // Reset states when needed
    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
    
    fun resetSignUpState() {
        _signUpState.value = SignUpState.Idle
    }
    
    fun resetForgotPasswordState() {
        _forgotPasswordState.value = ForgotPasswordState.Idle
    }
    
    fun resetResetPasswordState() {
        _resetPasswordState.value = ResetPasswordState.Idle
    }
}

// ========== STATE CLASSES ==========

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    data class Success(val response: SignUpResponse) : SignUpState()
    data class Error(val message: String) : SignUpState()
}

sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Loading : ForgotPasswordState()
    data class Success(val response: ForgotPasswordResponse) : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
}

sealed class ResetPasswordState {
    object Idle : ResetPasswordState()
    object Loading : ResetPasswordState()
    data class Success(val response: ResetPasswordResponse) : ResetPasswordState()
    data class Error(val message: String) : ResetPasswordState()
}

sealed class CheckUsernameState {
    object Idle : CheckUsernameState()
    object Loading : CheckUsernameState()
    data class Success(val response: CheckUserNameModel) : CheckUsernameState()
    data class Error(val message: String) : CheckUsernameState()
}

sealed class TitlesState {
    object Idle : TitlesState()
    object Loading : TitlesState()
    data class Success(val response: TitlesApiResponse) : TitlesState()
    data class Error(val message: String) : TitlesState()
}

// ========== REQUEST/RESPONSE DATA CLASSES ==========
// These will go in a new file: api/models.kt or use existing response/ folder

data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean = false
)

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val token: String?,
    val user: UserData?
)

data class SignUpRequest(
    val username: String,
    val email: String?,
    val phone: String?,
    val password: String,
    val countryCode: String?
    // Add other fields as needed
)

data class SignUpResponse(
    val success: Boolean,
    val message: String?,
    val userId: String?
)

data class ForgotPasswordRequest(
    val identifier: String, // email or phone
    val isPhone: Boolean
)

data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String?,
    val verificationCode: String? // If sent in response
)

data class ResetPasswordRequest(
    val identifier: String,
    val countryCode: String,
    val countryNameCode: String,
    val verificationCode: String,
    val newPassword: String,
    val confirmPassword: String
)

data class ResetPasswordResponse(
    val success: Boolean,
    val message: String?
)

data class UserData(
    val id: String,
    val username: String,
    val email: String?
)
```

## Required Dependencies to Add

Add these to `app/build.gradle.kts`:

```kotlin
dependencies {
    // ... existing dependencies ...
    
    // ViewModel & Lifecycle (REQUIRED)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
    
    // Coroutines (REQUIRED)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}
```

Or add to `gradle/libs.versions.toml`:
```toml
[versions]
# ... existing ...
lifecycle-viewmodel-compose = "2.9.4"
lifecycle-runtime-compose = "2.9.4"
coroutines = "1.9.0"

[libraries]
# ... existing ...
androidx-lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "lifecycle-viewmodel-compose" }
androidx-lifecycle-runtime-compose = { module = "androidx.lifecycle:lifecycle-runtime-compose", version.ref = "lifecycle-runtime-compose" }
kotlinx-coroutines-android = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-android", version.ref = "coroutines" }
```

## Dependency Injection Setup (Hilt)

### Why Use Dependency Injection?
- ✅ Cleaner code - No manual object creation
- ✅ Easier testing - Can inject mock dependencies
- ✅ Better architecture - Dependencies are explicit
- ✅ Automatic lifecycle management

### Step 1: Add Hilt Dependencies

Add to `gradle/libs.versions.toml`:
```toml
[versions]
# ... existing versions ...
hilt = "2.54"
hilt-navigation-compose = "1.2.0"

[libraries]
# ... existing libraries ...
hilt-android = { module = "com.google.dagger:hilt-android", version.ref = "hilt" }
hilt-compiler = { module = "com.google.dagger:hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { module = "androidx.hilt:hilt-navigation-compose", version.ref = "hilt-navigation-compose" }

[plugins]
# ... existing plugins ...
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
kotlin-kapt = { id = "org.jetbrains.kotlin.kapt", version.ref = "kotlin" }
```

Add to `app/build.gradle.kts`:
```kotlin
plugins {
    // ... existing plugins ...
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    // ... existing config ...
    
    // Add this for Hilt
    buildFeatures {
        compose = true
    }
}

dependencies {
    // ... existing dependencies ...
    
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
}
```

### Step 2: Create Application Class

Create `di/JoyersApplication.kt`:
```kotlin
package com.joyersapp.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JoyersApplication : Application()
```

Update `AndroidManifest.xml`:
```xml
<application
    android:name=".di.JoyersApplication"
    ...>
    <!-- rest of manifest -->
</application>
```

### Step 3: Create Hilt Modules

Create `di/NetworkModule.kt`:
```kotlin
package com.joyersapp.di

import com.joyersapp.api.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    private const val BASE_URL = "YOUR_BASE_URL_HERE" // Update this
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}
```

### Step 4: Update ViewModel to Use Dependency Injection

Update `view_model/AuthViewModel.kt`:
```kotlin
package com.joyersapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.api.AuthApiService
import com.joyersapp.response.CheckUserNameModel
import com.joyersapp.response.TitlesApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authApiService: AuthApiService
) : ViewModel() {
    
    // ... rest of the ViewModel code stays the same ...
    // Just replace RetrofitClient.authApiService with authApiService
    
    fun login(username: String, password: String, rememberMe: Boolean = false) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = authApiService.login(  // Changed from RetrofitClient
                    LoginRequest(username, password, rememberMe)
                )
                _loginState.value = LoginState.Success(response)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            }
        }
    }
    
    // Update all other methods similarly - use authApiService instead of RetrofitClient.authApiService
}
```

### Step 5: Update MainActivity

Update `MainActivity.kt`:
```kotlin
package com.joyersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// ... existing imports ...
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // ... rest stays the same ...
}
```

### Step 6: Use ViewModel in Screens (With Hilt)

Update `LoginScreen.kt`:
```kotlin
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),  // Changed from viewModel()
    onLoginClick: () -> Unit = {},
    // ... other params
) {
    // ... rest stays the same ...
}
```

Don't forget to import:
```kotlin
import androidx.hilt.navigation.compose.hiltViewModel
```

### Step 7: Remove RetrofitClient Object (Optional)

Since we're using DI, you can remove the `RetrofitClient` object file if you want, as everything is now provided through Hilt modules.

### Updated Folder Structure with DI

```
app/src/main/java/com/joyersapp/
├── api/
│   └── AuthApiService.kt        # API interface (no RetrofitClient needed)
├── di/                          # NEW FOLDER
│   ├── JoyersApplication.kt    # Application class
│   └── NetworkModule.kt         # Hilt module for network
├── view_model/
│   └── AuthViewModel.kt         # Updated to use @Inject
├── auth/                        # Keep as-is
├── common_widgets/             # Keep as-is
└── ... (rest stays the same)
```

### Benefits of Using Hilt

1. **Automatic Dependency Management**: Hilt creates and manages all dependencies
2. **Scoped Instances**: ViewModels are automatically scoped to their lifecycle
3. **Easy Testing**: Can easily inject mock dependencies for testing
4. **Less Boilerplate**: No need for manual object creation
5. **Type Safety**: Compile-time dependency checking

### Alternative: Without Dependency Injection

If you prefer to keep it simpler without DI, you can:
- Keep using `RetrofitClient` object directly
- Create ViewModel instances manually
- This is fine for smaller projects, but DI becomes more valuable as the app grows

## How to Use in Your Screens

### Example: LoginScreen.kt

**Before (Current):**
```kotlin
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    // ... other params
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // ... local state management
}
```

**After (With ViewModel - Without Hilt):**
```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginClick: () -> Unit = {},
    // ... other params
) {
    // Observe login state
    val loginState by viewModel.loginState.collectAsState()
    
    // Local UI state (for form inputs)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    
    // Handle login state changes
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                // Navigate on success
                onLoginClick()
                viewModel.resetLoginState()
            }
            is LoginState.Error -> {
                // Show error message
                // You can access: loginState.message
            }
            is LoginState.Loading -> {
                // Show loading indicator
            }
            is LoginState.Idle -> {
                // Initial state
            }
        }
    }
    
    // In your login button:
    Button(
        onClick = {
            viewModel.login(username, password, rememberMe)
        },
        enabled = loginState !is LoginState.Loading && isFormValid
    ) {
        if (loginState is LoginState.Loading) {
            CircularProgressIndicator()
        } else {
            Text("Login")
        }
    }
}
```

**After (With ViewModel - With Hilt):**
```kotlin
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),  // Use hiltViewModel() instead
    onLoginClick: () -> Unit = {},
    // ... other params
) {
    // ... rest is exactly the same as above ...
}
```

**Key Difference:**
- **Without Hilt**: Use `viewModel()` - ViewModel is created manually
- **With Hilt**: Use `hiltViewModel()` - ViewModel is injected with dependencies

## Summary

### What Gets Added:

**Core MVVM:**
1. ✅ `view_model/AuthViewModel.kt` - Single ViewModel for all auth
2. ✅ `api/AuthApiService.kt` - API interface
3. ✅ `api/RetrofitClient.kt` - Retrofit setup (if not using Hilt)
4. ✅ `api/models.kt` - Request/Response models (or use existing `response/` folder)

**Dependency Injection (Optional but Recommended):**
5. ✅ `di/JoyersApplication.kt` - Application class with Hilt
6. ✅ `di/NetworkModule.kt` - Hilt module for network dependencies
7. ✅ Hilt dependencies and plugins

### What Stays the Same:
- ✅ All existing folders (`auth/`, `common_widgets/`, `response/`, etc.)
- ✅ All existing screens (just add ViewModel usage)
- ✅ All existing navigation
- ✅ All existing UI components

### Minimal Changes:
- Add 3 core dependencies (ViewModel, Lifecycle, Coroutines)
- Add Hilt dependencies (if using DI)
- Update screens to use ViewModel (optional, can do gradually)
- Add API base URL in RetrofitClient or NetworkModule
- Add `@AndroidEntryPoint` to MainActivity (if using Hilt)

### Choose Your Approach:

**Option 1: Simple (No DI)**
- Use `RetrofitClient` object directly
- Use `viewModel()` in screens
- Less setup, good for smaller projects

**Option 2: With DI (Hilt)**
- Use Hilt modules for dependencies
- Use `hiltViewModel()` in screens
- More setup, better for larger projects and testing

## Next Steps

1. **Review this plan** - Does this structure work for you?
2. **Choose DI approach** - Do you want to use Hilt or keep it simple without DI?
3. **Confirm API endpoints** - Do you have the actual API endpoints and request/response formats?
4. **Decide on models location** - Use existing `response/` folder or create new `api/models.kt`?
5. **Implementation** - I can create all these files once you approve

### Recommended Approach:
- **Start Simple**: Begin without DI, get MVVM working first
- **Add DI Later**: Once comfortable, add Hilt for better architecture
- **Or Go Full DI**: If you're familiar with DI, start with Hilt from the beginning

Let me know if you want any changes to this plan!

