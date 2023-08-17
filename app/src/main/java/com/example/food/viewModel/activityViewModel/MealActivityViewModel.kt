package com.example.food.viewModel.activityViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food.dataBase.MealDatabase
import com.example.food.model.Meal
import com.example.food.model.MealList
import com.example.food.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealActivityViewModel(val mealDatabase: MealDatabase) : ViewModel() {



    private var mealDetailLiveData = MutableLiveData<Meal>()
    fun getMealDetails(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.isSuccessful){
                    if(response.body()!=null){
                        val mealDetail:Meal = response.body()!!.meals[0]
                        Log.i("#meal", "meal id : ${mealDetail.idMeal} name ${mealDetail.strMeal}")
                        mealDetailLiveData.value = mealDetail
                    }
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.i("#meal",t.message.toString())
            }

        })
    }

    fun observerMealDetailLiveData() : LiveData<Meal> {
        return mealDetailLiveData
    }
    fun insertMeal(meal :Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}