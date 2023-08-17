package com.example.food.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.food.adapter.CategoryAdapter
import com.example.food.adapter.PopularAdapter
import com.example.food.databinding.FragmentHomeBinding
import com.example.food.model.Meal
import com.example.food.model.MealByCategory
import com.example.food.view.activity.CategoryMealActivity
import com.example.food.view.activity.MainActivity
import com.example.food.view.activity.MealActivity
import com.example.food.view.fragment.bottomSheet.BottomSheetFragment
import com.example.food.viewModel.fragmentViewModel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var categoryAdapter : CategoryAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this)[HomeViewModel::class.java]

//w            homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        homeViewModel = (activity as MainActivity).viewmodel

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.txtHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        popularAdapter = PopularAdapter()
        categoryAdapter = CategoryAdapter()
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        homeViewModel.getPopularItems()
        observerPopularMealLiveData()
        preparePopularItemRecyclerView()
        onPopularItemClick()

        homeViewModel.getCategories()
        observerCategoriesLiveData()
        prepareCategoriesRecyclerView()
        onCategoryClick()

        onPopularItemLongClick()
    }

    private fun onPopularItemLongClick() {
        popularAdapter.onLongItemClick={meal->

        val mealBottomSheetFragment  = BottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun onCategoryClick() {
        categoryAdapter.onItemClick = {category ->
            val intent = Intent(context, CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)

        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recCategories.apply {
            layoutManager = GridLayoutManager(activity,3, GridLayoutManager.VERTICAL,false)
            adapter = categoryAdapter
        }
    }

    private fun observerCategoriesLiveData() {
        homeViewModel.observerCategoriesLiveData()
            .observe(
                viewLifecycleOwner
            ) { categories ->
                categoryAdapter.setCategoryList(categories)
                categories.forEach { category ->
                    Log.i("@azx", category.strCategory)

                }
            }
    }

    private fun onPopularItemClick() {
        popularAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun preparePopularItemRecyclerView() {
        binding.recPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularAdapter

        }
    }

    private fun observerPopularMealLiveData() {
        homeViewModel.observerPopularMealLiveData()
            .observe(
                viewLifecycleOwner
            ) { mealList ->
                popularAdapter.setMeals(mealsList = mealList as ArrayList<MealByCategory>)
            }


    }

    private fun onRandomMealClick() {
        binding.cardRandom.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        homeViewModel.observeRandomMealLiveData()
            .observe(
                viewLifecycleOwner
            ) { meal ->
                Log.i("#err", meal.toString())
                Glide.with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .into(binding.imgRandom)

                this.randomMeal = meal
            }
    }

    companion object {
        const val MEAL_ID = "meal_id"
        const val MEAL_NAME = "meal_name"
        const val MEAL_THUMB = "meal_thumb"
        const val  CATEGORY_NAME = "category_name"
    }
}