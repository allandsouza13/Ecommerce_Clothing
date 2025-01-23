package com.example.ecommerce_clothing.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce_clothing.Model.CategoryModel
import com.example.ecommerce_clothing.Model.ItemsModel
import com.example.ecommerce_clothing.Model.SliderModel
import com.example.ecommerce_clothing.Repository.MainRepository

class MainViewModel():ViewModel() {
    private val repository= MainRepository()

    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    fun loadFiltered(id:String): LiveData<MutableList<ItemsModel>> {
        return repository.loadFiltered(id)
    }

}