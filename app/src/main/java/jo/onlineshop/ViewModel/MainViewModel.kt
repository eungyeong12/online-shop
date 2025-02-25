package jo.onlineshop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import jo.onlineshop.Model.CategoryModel
import jo.onlineshop.Model.ItemsModel
import jo.onlineshop.Model.SliderModel
import jo.onlineshop.Repository.MainRepository

class MainViewModel(): ViewModel() {
    private val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    fun loadFiltered(id: String): LiveData<MutableList<ItemsModel>> {
        return repository.loadFiltered(id)
    }
}