package com.example.food.retrofit

import com.example.food.model.CategoryList
import com.example.food.model.MealByCategoryList
import com.example.food.model.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomModel() : Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id:String) : Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName : String) : Call<MealByCategoryList>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php?")
    fun getMealByCategory(@Query("c") categoryName : String) : Call<MealByCategoryList>
}