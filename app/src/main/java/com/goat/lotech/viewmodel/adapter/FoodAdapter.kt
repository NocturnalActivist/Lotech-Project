package com.goat.lotech.viewmodel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goat.lotech.databinding.ItemFoodBinding
import com.goat.lotech.model.FoodModel

class FoodAdapter(private val listFood: ArrayList<FoodModel>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(listFood[position])
    }

    override fun getItemCount(): Int = listFood.size

    inner class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: FoodModel) {
            with(binding) {
                foodName.text = list.foodName
                calories.text = list.calories
                protein.text = list.protein
                lemak.text = list.fat
                karbohidrat.text = list.karbohidrat
                serat.text = list.serat
                vitc.text = list.vitaminc
            }
        }
    }
}