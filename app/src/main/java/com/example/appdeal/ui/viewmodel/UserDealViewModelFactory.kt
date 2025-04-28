package com.example.appdeal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appdeal.data.UserDealDao

class UserDealViewModelFactory(private val dao: UserDealDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDealViewModel::class.java)) {
            return UserDealViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
