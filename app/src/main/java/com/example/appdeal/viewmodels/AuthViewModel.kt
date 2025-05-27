package com.example.appdeal.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdeal.data.FirebaseManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val TAG = "AuthViewModel"
    private val firebaseManager = FirebaseManager()
    
    private val _currentUser = MutableStateFlow<FirebaseUser?>(firebaseManager.getCurrentUser())
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _displayName = MutableStateFlow<String?>(firebaseManager.getCurrentUser()?.displayName)
    val displayName: StateFlow<String?> = _displayName.asStateFlow()
    
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        return firebaseManager.getGoogleSignInClient(context)
    }
    
    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                if (data == null) {
                    _errorMessage.value = "Google sign in failed: No data received"
                    return@launch
                }
                
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                
                // Google Sign In was successful, authenticate with Firebase
                account.idToken?.let { idToken ->
                    Log.d(TAG, "Google Sign In successful, authenticating with Firebase")
                    firebaseManager.signInWithGoogle(idToken)
                        .onSuccess { user ->
                            _currentUser.value = user
                            _displayName.value = user.displayName
                            Log.d(TAG, "Firebase authentication successful")
                        }
                        .onFailure { error ->
                            Log.e(TAG, "Firebase authentication failed", error)
                            _errorMessage.value = "Google sign in failed: ${error.message}"
                        }
                } ?: run {
                    Log.e(TAG, "ID token is null")
                    _errorMessage.value = "Google sign in failed: ID token is null"
                }
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed with status code: ${e.statusCode}", e)
                val errorMessage = when (e.statusCode) {
                    12500 -> "This app is not authorized to use Google Sign-In (app not properly configured)"
                    12501 -> "User canceled the sign-in flow"
                    12502 -> "Sign-in attempt failed"
                    10 -> "Invalid client ID or missing SHA-1 fingerprint in Firebase console"
                    else -> "Google sign in failed: code ${e.statusCode}"
                }
                _errorMessage.value = errorMessage
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during Google sign in", e)
                _errorMessage.value = "Google sign in failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            firebaseManager.signIn(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _displayName.value = user.displayName
                }
                .onFailure { error ->
                    _errorMessage.value = "Sign in failed: ${error.message}"
                }
            
            _isLoading.value = false
        }
    }
    
    fun signUp(email: String, password: String, name: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                // First create the account
                val result = firebaseManager.signUp(email, password)
                
                result.onSuccess { user ->
                    _currentUser.value = user
                    _displayName.value = name.ifEmpty { null }
                    
                    // If name is provided, update the display name
                    if (name.isNotEmpty()) {
                        firebaseManager.updateUserDisplayName(user, name)
                            .onSuccess {
                                // Update the display name in our state
                                _displayName.value = name
                            }
                            .onFailure { error ->
                                Log.e(TAG, "Failed to update display name", error)
                                // We don't set an error message here as the account was still created
                            }
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = "Sign up failed: ${error.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Sign up failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun signOut(context: Context) {
        firebaseManager.signOut(context)
        _currentUser.value = null
        _displayName.value = null
    }
    
    fun isUserSignedIn(): Boolean {
        return firebaseManager.getCurrentUser() != null
    }
} 