package com.example.food.viewModel.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food.dataBase.MealDatabase
import com.example.food.viewModel.activityViewModel.MealActivityViewModel



class MealViewModelFactory(private val mealDatabase: MealDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealActivityViewModel(mealDatabase) as T
    }
}