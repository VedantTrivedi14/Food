package com.example.food.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.food.adapter.FavoriteAdapter
import com.example.food.databinding.FragmentFavoriteBinding
import com.example.food.view.activity.MainActivity
import com.example.food.viewModel.fragmentViewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = (activity as MainActivity).viewmodel
        favoriteAdapter = FavoriteAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorites()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.delete(favoriteAdapter.differ.currentList[position])

                Snackbar.make(requireView(),"Meal remove from favorite",Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                ) {
                    viewModel.insertMeal(favoriteAdapter.differ.currentList[position])
                }.show()
            }

        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recFavorites)

    }

    private fun prepareRecyclerView() {
        binding.recFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }
    }

    private fun observeFavorites() {

        viewModel.observeFavoriteMealsLiveData().observe(
            requireActivity()
        ) { meals ->
            meals.forEach {
                it.idMeal?.let { it1 -> Log.i("#mnb", it1) }
            }
            favoriteAdapter.differ.submitList(meals)
        }
    }

    companion object {

    }
}