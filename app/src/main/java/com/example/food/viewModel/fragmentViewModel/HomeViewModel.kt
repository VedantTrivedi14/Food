package com.example.food.viewModel.fragmentViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food.dataBase.MealDatabase
import com.example.food.model.Category
import com.example.food.model.CategoryList
import com.example.food.model.Meal
import com.example.food.model.MealByCategory
import com.example.food.model.MealByCategoryList
import com.example.food.model.MealList
import com.example.food.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Home"
    }
    val text: LiveData<String> = _text

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemLiveData = MutableLiveData<List<MealByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealLiveData = mealDatabase.mealDao().getAllMeal()
    private var bottomSheetLiveData = MutableLiveData<Meal>()

fun getRandomMeal() {
    RetrofitInstance.api.getRandomModel().enqueue(object : Callback<MealList> {
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.i(
                        "#random",
                        "meal id : ${randomMeal.idMeal} name ${randomMeal.strMeal}"
                    )
                    randomMealLiveData.value = randomMeal
                }
            }
        }

        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.i("#random", t.message.toString())
        }

    })
}

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealByCategoryList> {
                override fun onResponse(
                    call: Call<MealByCategoryList>,
                    response: Response<MealByCategoryList>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val categoryMeal: List<MealByCategory> = response.body()!!.meals
                            Log.i("#categoryMal", "meal Thumb :${categoryMeal[0].strMealThumb}")
                            popularItemLiveData.value = categoryMeal
                        }
                    }
                }

                override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                    Log.i("#categoryMal", t.message.toString())
                }

            })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val categories: List<Category> = response.body()!!.categories
                        Log.i("#categoryMal", "meal Thumb :${categories[0].strCategoryThumb}")
                        categoriesLiveData.value = categories
                    }
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.i("#categoryMal", t.message.toString())
            }

        })
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observerPopularMealLiveData(): LiveData<List<MealByCategory>> {
        return popularItemLiveData
    }
    fun observerCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavoriteMealsLiveData():LiveData<List<Meal>>{
        return favoritesMealLiveData
    }

    fun delete(meal :Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
    fun insertMeal(meal :Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun getMealById(id : String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
               if(response.isSuccessful){
                   if(response.body()!=null){
                       val meal = response.body()!!.meals.first()
                       meal?.let {meal->
                           bottomSheetLiveData.postValue(meal)
                       }
                   }
               }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.i("@rew",t.message.toString())
            }

        })
    }

    fun observeBottomSheetLiveData() : LiveData<Meal>{
        return bottomSheetLiveData
    }


}