package com.example.appdeal.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdeal.data.User
import com.example.appdeal.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl.asStateFlow()
    
    private val _userStats = MutableStateFlow(UserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()
    
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
        _currentUser.value = null
        _isAuthenticated.value = false
        _userStats.value = UserStats()
    }
    
    fun updateProfileImage(url: String) {
        _profileImageUrl.value = url
    }
    
    fun setAuthenticated(authenticated: Boolean) {
        _isAuthenticated.value = authenticated
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
}

data class UserStats(
    val savedDeals: Int = 0,
    val contributedDeals: Int = 0,
    val totalSavings: Double = 0.0
) 