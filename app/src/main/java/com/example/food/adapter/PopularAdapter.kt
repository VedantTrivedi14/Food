package com.example.food.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.databinding.PopularItemBinding
import com.example.food.model.MealByCategory

//mealsList: ArrayList<CategoryMeals>
class PopularAdapter () : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {
    private var mealsList = ArrayList<MealByCategory>()
    lateinit var onItemClick:((MealByCategory)->Unit)
     var onLongItemClick : ((MealByCategory)-> Unit)? = null


    fun setMeals(mealsList: ArrayList<MealByCategory>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopular)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick!!.invoke(mealsList[position])
            true
        }
    }

    class PopularViewHolder(val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}