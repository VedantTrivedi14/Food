package com.example.food.view.fragment.bottomSheet

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.System.putString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.food.databinding.FragmentBottomSheetBinding
import com.example.food.view.activity.MainActivity
import com.example.food.view.activity.MealActivity
import com.example.food.view.fragment.HomeFragment
import com.example.food.viewModel.fragmentViewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val MEAL_ID = "param1"


class BottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding : FragmentBottomSheetBinding
    private lateinit var viewModel: HomeViewModel
    private var mealName :String?= null
    private var mealThumb : String ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
            viewModel = (activity as MainActivity).viewmodel

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it) }

        observeBottomSheetMeal()
        onBottomSheetDialogClick()
    }



    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if(mealName!=null && mealThumb!=null){
                val intent = Intent(activity,MealActivity::class.java)

                intent.apply {
                    putExtra(HomeFragment.MEAL_ID,mealId)
                    putExtra(HomeFragment.MEAL_NAME,mealName)
                    putExtra(HomeFragment.MEAL_THUMB,mealThumb)

                }
                startActivity(intent)
            }
        }
    }

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetLiveData().observe(viewLifecycleOwner
        ) { meal->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgCategory)
            binding.txtArea.text = meal.strArea
            binding.txtCategories.text = meal.strCategory
            binding.txtMealName.text = meal.strMeal
            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            BottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)

                }
            }
    }
}