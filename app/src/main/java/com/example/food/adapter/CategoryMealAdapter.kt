package com.example.food.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.databinding.MealItemBinding
import com.example.food.model.MealByCategory

class CategoryMealAdapter () : RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder>() {


    private var mealList = ArrayList<MealByCategory>()
    fun setMealList(mealList: List<MealByCategory>){
        this.mealList = mealList as ArrayList<MealByCategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.txtMealName.text = mealList[position].strMeal
    }

    class CategoryMealViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}