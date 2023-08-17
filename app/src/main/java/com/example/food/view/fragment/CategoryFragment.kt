package com.example.food.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.food.R
import com.example.food.adapter.CategoryAdapter
import com.example.food.databinding.FragmentCategoryBinding
import com.example.food.model.Category
import com.example.food.view.activity.CategoryMealActivity
import com.example.food.view.activity.MainActivity
import com.example.food.viewModel.fragmentViewModel.HomeViewModel


class CategoryFragment : Fragment() {

    private lateinit var binding : FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryAdapter = CategoryAdapter()
        viewModel = (activity as MainActivity).viewmodel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategories()
        prepareRecyclerView()
        observeCategories()
        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoryAdapter.onItemClick = {category ->
            val intent = Intent(context, CategoryMealActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)
            startActivity(intent)

        }
    }

    private fun observeCategories() {
        viewModel.observerCategoriesLiveData().observe(viewLifecycleOwner
        ) { category -> categoryAdapter.setCategoryList(category) }
    }

    private fun prepareRecyclerView() {
        binding.recCategory.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoryAdapter
        }
    }

    companion object {
        const val  CATEGORY_NAME = "category_name"
    }
}