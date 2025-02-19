package jo.onlineshop.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
}