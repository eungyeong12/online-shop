package jo.onlineshop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import jo.onlineshop.Model.SliderModel
import jo.onlineshop.Repository.MainRepository

class MainViewModel(): ViewModel() {
    private val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        return repository.loadBanner()
    }
}