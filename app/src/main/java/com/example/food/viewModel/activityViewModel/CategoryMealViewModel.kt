package com.example.food.viewModel.activityViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.food.model.MealByCategory
import com.example.food.model.MealByCategoryList
import com.example.food.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealViewModel : ViewModel() {

    val mealsLiveData = MutableLiveData<List<MealByCategory>>()
    fun getMealByCategory(categoryName :String){

        RetrofitInstance.api.getMealByCategory(categoryName).enqueue(object :
            Callback<MealByCategoryList> {
            override fun onResponse(
                call: Call<MealByCategoryList>,
                response: Response<MealByCategoryList>
            ) {
                if(response.isSuccessful){
                    if(response.body()!=null){
                        val mealList : List<MealByCategory> = response.body()!!.meals
                        Log.i("#zxc","${mealList[0].strMealThumb}")
                        mealsLiveData.value = mealList
                    }
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.i("#zxc",t.message.toString())
            }

        })

    }

    fun observerMealsLivedata() : LiveData<List<MealByCategory>> {
        return mealsLiveData
    }
}