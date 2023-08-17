package com.example.food.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.food.R
import com.example.food.adapter.CategoryMealAdapter
import com.example.food.databinding.ActivityCategoryMealBinding
import com.example.food.view.fragment.HomeFragment
import com.example.food.viewModel.activityViewModel.CategoryMealViewModel

class CategoryMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealBinding
    private lateinit var categoryMealViewModel: CategoryMealViewModel
    private lateinit var categoryMealAdapter: CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryMealAdapter = CategoryMealAdapter()
        prepareRecyclerView()

        categoryMealViewModel = ViewModelProvider(this)[CategoryMealViewModel::class.java]

        categoryMealViewModel.getMealByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealViewModel.observerMealsLivedata().observe(this) { mealList ->
            binding.txtCategoryCount.text = mealList.size.toString()
            categoryMealAdapter.setMealList(mealList)
            mealList.forEach { meal ->
                Log.i("#qwe", meal.strMeal)
            }
        }
    }
    private fun prepareRecyclerView() {
        binding.recSubcategory.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }
}