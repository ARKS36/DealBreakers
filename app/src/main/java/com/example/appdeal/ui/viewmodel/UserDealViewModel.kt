package com.example.appdeal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdeal.data.UserDeal
import com.example.appdeal.data.UserDealDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserDealViewModel(private val dao: UserDealDao) : ViewModel() {

    val allDeals: Flow<List<UserDeal>> = dao.getAllDeals()

    fun insertDeal(deal: UserDeal) {
        viewModelScope.launch {
            dao.insertUserDeal(deal)
        }
    }
}


