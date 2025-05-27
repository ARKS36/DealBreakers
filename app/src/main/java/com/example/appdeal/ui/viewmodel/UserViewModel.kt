package com.example.appdeal.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdeal.data.User
import com.example.appdeal.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance().reference
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl.asStateFlow()
    
    private val _userStats = MutableStateFlow(UserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()
    
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()
    
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        // Initialize with Firebase Auth current user
        updateUserFromFirebase()
    }
    
    private fun updateUserFromFirebase() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            _currentUser.value = User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                password = "",  // We don't store passwords
                name = firebaseUser.displayName,
                createdAt = firebaseUser.metadata?.creationTimestamp ?: System.currentTimeMillis()
            )
            _isAuthenticated.value = true
            _userName.value = firebaseUser.displayName
            _userEmail.value = firebaseUser.email
            _profileImageUrl.value = firebaseUser.photoUrl?.toString()
            loadUserStats()
        } else {
            _currentUser.value = null
            _isAuthenticated.value = false
            _userName.value = null
            _userEmail.value = null
            _profileImageUrl.value = null
            _userStats.value = UserStats()
        }
    }
    
    fun login(email: String, password: String): Result<User> {
        val result = userRepository.login(email, password)
        result.onSuccess { user ->
            _currentUser.value = user
            _isAuthenticated.value = true
            loadUserStats()
        }
        return result
    }
    
    fun signUp(email: String, password: String, name: String? = null): Result<User> {
        val result = userRepository.signUp(email, password, name)
        result.onSuccess { user ->
            _currentUser.value = user
            _isAuthenticated.value = true
            _userStats.value = UserStats() // Initialize with empty stats
        }
        return result
    }
    
    fun loadUserFromPreferences(preferences: SharedPreferences) {
        val userId = preferences.getString("USER_ID", null)
        if (userId != null) {
            val user = userRepository.getUserById(userId)
            if (user != null) {
                _currentUser.value = user
                _isAuthenticated.value = true
                loadUserStats()
            }
        }
    }
    
    fun saveUserToPreferences(preferences: SharedPreferences) {
        val editor = preferences.edit()
        _currentUser.value?.let {
            editor.putString("USER_ID", it.id)
        } ?: editor.remove("USER_ID")
        editor.apply()
    }
    
    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _isAuthenticated.value = false
        _userStats.value = UserStats()
        _profileImageUrl.value = null
        _userName.value = null
        _userEmail.value = null
    }
    
    fun updateProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val firebaseUser = auth.currentUser ?: return@launch
                
                // Upload image to Firebase Storage
                val filename = "profile_${UUID.randomUUID()}"
                val ref = storage.child("profile_images/${firebaseUser.uid}/$filename")
                
                ref.putFile(imageUri).await()
                val downloadUrl = ref.downloadUrl.await()
                
                // Update Firebase user profile
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUrl)
                    .build()
                
                firebaseUser.updateProfile(profileUpdates).await()
                
                // Update local state
                _profileImageUrl.value = downloadUrl.toString()
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update profile image: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateUserProfile(displayName: String?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val firebaseUser = auth.currentUser ?: return@launch
                
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                
                firebaseUser.updateProfile(profileUpdates).await()
                
                // Update local state
                _userName.value = displayName
                _currentUser.value = _currentUser.value?.copy(name = displayName)
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateEmail(email: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val firebaseUser = auth.currentUser ?: return@launch
                
                firebaseUser.updateEmail(email).await()
                
                // Update local state
                _userEmail.value = email
                _currentUser.value = _currentUser.value?.copy(email = email)
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update email: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val firebaseUser = auth.currentUser ?: return@launch
                
                firebaseUser.updatePassword(newPassword).await()
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update password: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setAuthenticated(authenticated: Boolean) {
        _isAuthenticated.value = authenticated
    }
    
    fun setUserName(name: String) {
        updateUserProfile(name)
    }
    
    fun setUserEmail(email: String) {
        updateEmail(email)
    }
    
    private fun loadUserStats() {
        // In a real app, this would load from a database or API
        // For now, using mock data
        _userStats.value = UserStats(
            savedDeals = 12,
            contributedDeals = 5,
            totalSavings = 87.50
        )
    }
    
    fun updateUserStats(stats: UserStats) {
        _userStats.value = stats
    }
    
    fun refreshUserProfile() {
        updateUserFromFirebase()
    }
}

data class UserStats(
    val savedDeals: Int = 0,
    val contributedDeals: Int = 0,
    val totalSavings: Double = 0.0
) 