package com.example.food.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food.R
import com.example.food.dataBase.MealDatabase
import com.example.food.databinding.ActivityMealBinding
import com.example.food.model.Meal
import com.example.food.view.fragment.HomeFragment
import com.example.food.viewModel.activityViewModel.MealActivityViewModel
import com.example.food.viewModel.viewModelFactory.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealActivityViewModel: MealActivityViewModel
    private var youtubeUri: String? = null
    private var meal: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealActivityViewModel =
            ViewModelProvider(this, viewModelFactory)[MealActivityViewModel::class.java]
//        mealActivityViewModel = ViewModelProvider(this)[MealActivityViewModel::class.java]


        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()
        mealActivityViewModel.getMealDetails(mealId)
        observerMealDetailLiveData()
        onYoutubeImageClick()

        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.fabFavorites.setOnClickListener {
            meal?.let {
                mealActivityViewModel.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
                binding.fabFavorites.setImageResource(R.drawable.baseline_favorite_24)
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUri))
            startActivity(intent)
        }
    }


    private fun observerMealDetailLiveData() {
        mealActivityViewModel.observerMealDetailLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(m: Meal) {
                onResponseCase()
                meal = m
                binding.txtCategories.text = "Categories : ${m.strCategory}"
                binding.txtArea.text = "Area : ${m.strArea}"
                binding.txtDetail.text = m.strInstructions
                youtubeUri = m.strYoutube
            }

        })
    }

    private fun setInformationInViews() {
        Glide.with(this)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
    }

    private fun getMealInformationFromIntent() {
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtCategories.visibility = View.INVISIBLE
        binding.txtArea.visibility = View.INVISIBLE
        binding.fabFavorites.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
        binding.txtInstruction.visibility = View.INVISIBLE


    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.txtCategories.visibility = View.VISIBLE
        binding.txtArea.visibility = View.VISIBLE
        binding.fabFavorites.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
        binding.txtInstruction.visibility = View.VISIBLE
    }
}