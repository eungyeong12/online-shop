package jo.onlineshop.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import jo.onlineshop.R
import jo.onlineshop.ViewModel.MainViewModel

class ListItemsActivity : BaseActivity() {
    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getStringExtra("id") ?: ""
        title = intent.getStringExtra("title") ?: ""

        setContent {
            ListItemScreen(
                title = title,
                onBackClick = {finish()},
                viewModel = viewModel,
                id = id
            )
        }
    }

    @Composable
    // 상품 목록 조회
    private fun ListItemScreen(
        title: String,
        onBackClick: () -> Unit,
        viewModel: MainViewModel,
        id: String
    ) {
        val items by viewModel.loadFiltered(id).observeAsState(emptyList()) // LiveData 형태로 데이터를 가져와 Composable과 연결하여 상태 변화에 따라 자동으로 UI 업데이트
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(id) {
            viewModel.loadFiltered(id) // id 변경 시 재실행
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // 상단 레이아웃 - 뒤로가기 버튼, 타이틀
            ConstraintLayout (
                modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)
            ) {
                // 제약을 적용할 두 개의 요소 생성
                val (backBtn, cartTxt) = createRefs()

                // 타이틀 텍스트
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(cartTxt) {centerTo(parent)}, // 부모 중앙에 배치
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    text = title
                )

                // 뒤로가기 버튼
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onBackClick()
                        }
                        .constrainAs(backBtn) { // 좌측 상단에 위치하도록
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                )
            }
            if (isLoading) {
                Box (
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ListItemsFullSize(items)
            }
        }

        LaunchedEffect(items) {
            isLoading = items.isEmpty() // 데이터가 비어 있으면 로딩 상태 유지
        }
    }
}