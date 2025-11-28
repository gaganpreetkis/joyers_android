# MVVM Architecture Implementation Guide

## Current State Analysis

Your app currently has:
- ✅ Jetpack Compose UI screens
- ✅ Navigation Compose setup
- ✅ Retrofit & OkHttp dependencies (already added)
- ✅ Response models in `response/` folder
- ❌ No ViewModel layer (business logic in Composables)
- ❌ No Repository pattern
- ❌ No data source abstraction
- ❌ No dependency injection

## Proposed MVVM Structure

### Folder Structure

```
app/src/main/java/com/joyersapp/
├── data/
│   ├── local/                    # Local data sources (Room, DataStore, etc.)
│   │   └── dao/                   # Data Access Objects
│   ├── remote/                    # Remote data sources
│   │   ├── api/                   # Retrofit API interfaces
│   │   └── dto/                   # Data Transfer Objects (API response models)
│   └── repository/                # Repository implementations
│       └── AuthRepository.kt
│
├── domain/                        # Business logic layer (optional but recommended)
│   ├── model/                     # Domain models (pure Kotlin data classes)
│   ├── usecase/                   # Use cases (business logic)
│   └── repository/                # Repository interfaces
│
├── ui/                            # UI layer (renamed from current structure)
│   ├── auth/
│   │   ├── LoginScreen.kt        # Composable (UI only)
│   │   ├── LoginViewModel.kt      # ViewModel
│   │   └── LoginUiState.kt        # UI State data class
│   ├── signup/
│   ├── forgotpassword/
│   └── ...
│
├── common_widgets/                # Keep as is
├── theme/                         # Keep as is
├── utils/                         # Keep as is
└── response/                      # Can be moved to data/remote/dto/ or kept for backward compatibility
```

## MVVM Layers Explained

### 1. **Data Layer** (`data/`)
   - **Purpose**: Handles all data operations (API calls, local storage)
   - **Components**:
     - `remote/api/`: Retrofit API interfaces
     - `remote/dto/`: API response models (what you currently have in `response/`)
     - `local/`: Room database, DataStore, SharedPreferences
     - `repository/`: Concrete implementations that combine remote + local

### 2. **Domain Layer** (`domain/`) - Optional but Recommended
   - **Purpose**: Pure business logic, independent of Android
   - **Components**:
     - `model/`: Domain models (cleaner than DTOs)
     - `usecase/`: Single responsibility business operations
     - `repository/`: Repository interfaces (abstractions)

### 3. **UI Layer** (`ui/`)
   - **Purpose**: Presentation logic and UI
   - **Components**:
     - `Screen.kt`: Composable functions (UI only, no business logic)
     - `ViewModel.kt`: Manages UI state, handles user actions
     - `UiState.kt`: Data class representing UI state

## Required Dependencies

Add these to your `gradle/libs.versions.toml`:

```toml
[versions]
# ... existing versions ...
lifecycle-viewmodel-compose = "2.9.4"
lifecycle-runtime-compose = "2.9.4"
coroutines = "1.9.0"
hilt = "2.54"  # Optional: for dependency injection
hilt-navigation-compose = "1.2.0"  # Optional

[libraries]
# ... existing libraries ...
androidx-lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "lifecycle-viewmodel-compose" }
androidx-lifecycle-runtime-compose = { module = "androidx.lifecycle:lifecycle-runtime-compose", version.ref = "lifecycle-runtime-compose" }
kotlinx-coroutines-android = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-android", version.ref = "coroutines" }
kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }

[plugins]
# Optional: for dependency injection
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
kotlin-kapt = { id = "org.jetbrains.kotlin.kapt", version.ref = "kotlin" }
```

Then in `app/build.gradle.kts`:
```kotlin
dependencies {
    // ... existing dependencies ...
    
    // ViewModel & Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
}
```

## Implementation Example: Login Feature

### Step 1: Create API Interface (`data/remote/api/AuthApi.kt`)

```kotlin
package com.joyersapp.data.remote.api

import com.joyersapp.data.remote.dto.LoginRequest
import com.joyersapp.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
```

### Step 2: Create DTOs (`data/remote/dto/`)

```kotlin
// LoginRequest.kt
package com.joyersapp.data.remote.dto

data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean = false
)

// LoginResponse.kt
package com.joyersapp.data.remote.dto

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val user: UserDto?
)

data class UserDto(
    val id: String,
    val username: String,
    val email: String
)
```

### Step 3: Create Repository (`data/repository/AuthRepository.kt`)

```kotlin
package com.joyersapp.data.repository

import com.joyersapp.data.remote.api.AuthApi
import com.joyersapp.data.remote.dto.LoginRequest
import com.joyersapp.data.remote.dto.LoginResponse

class AuthRepository(
    private val authApi: AuthApi
) {
    suspend fun login(username: String, password: String, rememberMe: Boolean): Result<LoginResponse> {
        return try {
            val response = authApi.login(LoginRequest(username, password, rememberMe))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Step 4: Create UI State (`ui/auth/LoginUiState.kt`)

```kotlin
package com.joyersapp.ui.auth

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPhoneMode: Boolean = false,
    val selectedCountryCode: String = "+1",
    val rememberMe: Boolean = false,
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val usernameError: Boolean = false,
    val passwordError: Boolean = false
)
```

### Step 5: Create ViewModel (`ui/auth/LoginViewModel.kt`)

```kotlin
package com.joyersapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username, usernameError = false) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, passwordError = false) }
    }

    fun togglePhoneMode() {
        _uiState.update { 
            it.copy(
                isPhoneMode = !it.isPhoneMode,
                username = "",
                usernameError = false
            )
        }
    }

    fun toggleRememberMe() {
        _uiState.update { it.copy(rememberMe = !it.rememberMe) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        
        // Validation
        if (!isFormValid(state)) {
            _uiState.update { 
                it.copy(
                    usernameError = !isUsernameValid(state),
                    passwordError = !isPasswordValid(state)
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            authRepository.login(
                username = state.username,
                password = state.password,
                rememberMe = state.rememberMe
            ).fold(
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Login failed",
                            passwordError = true
                        )
                    }
                }
            )
        }
    }

    private fun isFormValid(state: LoginUiState): Boolean {
        return isUsernameValid(state) && isPasswordValid(state)
    }

    private fun isUsernameValid(state: LoginUiState): Boolean {
        // Your validation logic here
        return state.username.isNotEmpty()
    }

    private fun isPasswordValid(state: LoginUiState): Boolean {
        // Your validation logic here
        return state.password.isNotEmpty()
    }
}
```

### Step 6: Refactor Screen (`ui/auth/LoginScreen.kt`)

```kotlin
package com.joyersapp.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onJoinWithClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Your existing UI code, but now using uiState instead of local state
    // Example:
    // AppBasicTextField(
    //     value = uiState.username,
    //     onValueChange = viewModel::updateUsername,
    //     ...
    // )
    
    // Button(
    //     onClick = { viewModel.login(onLoginClick) },
    //     enabled = !uiState.isLoading && isFormValid(uiState),
    //     ...
    // )
}
```

### Step 7: Setup Retrofit (`data/remote/RetrofitModule.kt` or in Application class)

```kotlin
package com.joyersapp.data.remote

import com.joyersapp.data.remote.api.AuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {
    private const val BASE_URL = "https://your-api-url.com/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
}
```

## Migration Strategy

### Phase 1: Setup Infrastructure
1. Add required dependencies
2. Create folder structure
3. Setup Retrofit configuration
4. Create base classes/interfaces

### Phase 2: Migrate One Feature (Start with Login)
1. Create API interface
2. Create DTOs
3. Create Repository
4. Create ViewModel
5. Create UI State
6. Refactor Screen to use ViewModel

### Phase 3: Migrate Remaining Features
1. Repeat Phase 2 for each screen
2. SignUp → SignUpViewModel
3. ForgotPassword → ForgotPasswordViewModel
4. etc.

### Phase 4: Add Dependency Injection (Optional but Recommended)
- Use Hilt or Koin for cleaner dependency management
- Makes testing easier

## Benefits of This Architecture

1. **Separation of Concerns**: UI, business logic, and data are separated
2. **Testability**: Easy to unit test ViewModels and Repositories
3. **Maintainability**: Changes in one layer don't affect others
4. **Reusability**: Repositories can be used by multiple ViewModels
5. **State Management**: Centralized UI state management
6. **Lifecycle Aware**: ViewModels survive configuration changes

## Next Steps

1. Review this guide
2. Decide if you want to include Domain layer (recommended for larger apps)
3. Choose dependency injection solution (Hilt recommended)
4. Start with one feature (Login) as a proof of concept
5. Gradually migrate other features

Would you like me to proceed with implementing this structure? I can:
- Create the folder structure
- Add required dependencies
- Migrate the Login feature as an example
- Set up dependency injection (Hilt)

Let me know which approach you prefer!

