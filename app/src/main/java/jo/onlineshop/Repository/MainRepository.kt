package jo.onlineshop.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import jo.onlineshop.Model.CategoryModel
import jo.onlineshop.Model.ItemsModel
import jo.onlineshop.Model.SliderModel

class MainRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    // Firebase에서 가져온 데이터를 LiveData로 감싸서 반환
    // UI에서 변경을 감지하고 자동 업데이트 가능
    fun loadBanner(): LiveData<MutableList<SliderModel>> {
        val listData = MutableLiveData<MutableList<SliderModel>>()
        // Banner 컬렉션에서 데이터를 읽음
        val ref = firebaseDatabase.getReference("Banner")

        // Firebase의 데이터가 변경될 때마다 자동으로 호출됨
        ref.addValueEventListener(object : ValueEventListener{
            // Banner 컬렉션의 데이터를 가져와서 리스트에 추가
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    // 데이터를 SliderModel 객체로 변환
                    val item = childSnapshot.getValue(SliderModel::class.java)
                    item?.let { lists.add(it) }
                    listData.value = lists // LiveData 값 변경
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return listData
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        val listData = MutableLiveData<MutableList<CategoryModel>>()
        // Banner 컬렉션에서 데이터를 읽음
        val ref = firebaseDatabase.getReference("Category")

        // Firebase의 데이터가 변경될 때마다 자동으로 호출됨
        ref.addValueEventListener(object : ValueEventListener{
            // Category 컬렉션의 데이터를 가져와서 리스트에 추가
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    // 데이터를 CategoryModel 객체로 변환
                    val item = childSnapshot.getValue(CategoryModel::class.java)
                    item?.let { lists.add(it) }
                    listData.value = lists // LiveData 값 변경
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return listData
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> { // 데이터 변경 시 자동으로 UI에 반영
        val listData = MutableLiveData<MutableList<ItemsModel>>() // Firebase에서 가져온 데이터를 저장
        val ref = firebaseDatabase.getReference("Items")
        // showRecommended 속성을 기준으로 정렬, true인 항목만 가져옴 => 추천 상품만 필터링
        val query: Query = ref.orderByChild("showRecommended").equalTo(true)
        // 한 번만 데이터를 가져오고 리스너 종료, 실시간 변경 감지 X
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { // snapshot: Firebase에서 가져온 Items 데이터 목롥
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)  // 데이터를 ItemsModel 객체로 변환
                    if (list != null) {
                        lists.add(list)
                    }
                }
                listData.value = lists // LiveData에 불러온 데이터 전달
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return listData
    }
}