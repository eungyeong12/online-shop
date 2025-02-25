package jo.onlineshop.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.project1762.Helper.ManagmentCart
import jo.onlineshop.Model.ItemsModel
import jo.onlineshop.R

class DetailActivity : BaseActivity() {
    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = intent.getSerializableExtra("object") as ItemsModel
        managmentCart = ManagmentCart(this)

        setContent {
            DetailScreen(
                item = item,
                onBackClick = {finish()},
                onAddToCartClick = {
                    item.numberInCart = 1 // 장바구니에 담긴 개수 설정
                    managmentCart.insertItem(item) // 장바구니에 아이템 추가
                },
                onCartClick = {

                }
            )
        }
    }

    @Composable
    private fun DetailScreen(
        item: ItemsModel,
        onBackClick: () -> Unit,
        onAddToCartClick: () -> Unit,
        onCartClick: () -> Unit
    ) {
        // 선택된 이미지의 URL을 상태로 관리
        var selectedImageUrl by remember { mutableStateOf(item.picUrl.first()) }
        var selectedModelIndex by remember { mutableStateOf(-1) }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // 이미지와 썸네일을 표시하는 ConstraintLayout
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
                    .padding(bottom = 16.dp)
            ) {
                // 제약 조건에 사용할 요소들 정의
                val (back, fav, mainImage, thumbnail) = createRefs()

                // 메인 이미지 표시
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize() // 전체 영역 채움
                        .background(
                            colorResource(R.color.lightBrown),
                            shape = RoundedCornerShape(8.dp) // 모서리 둥글게 처리
                        )
                        .constrainAs(mainImage) { // 부모의 중앙에 배치
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }
                )

                // 뒤로가기 버튼
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 48.dp, start = 16.dp)
                        .clickable { onBackClick() }
                        .constrainAs(back) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                )

                // 이미지 썸네일 리스트
                LazyRow (
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .background(color = colorResource(R.color.white),
                            shape = RoundedCornerShape(10.dp))
                        .constrainAs(thumbnail) { // 부모 하단 중앙에 배치
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }
                ) {
                    // 상품 이미지 리스트 순회하면서 썸네일 표시
                    items(item.picUrl) { imageUrl ->
                        ImageThumbnail(
                            imageUrl = imageUrl,
                            isSelected = selectedImageUrl == imageUrl, // 현재 선택된 이미지와 비교
                            onClick = {selectedImageUrl = imageUrl}  // 썸네일 클릭 시 메인 이미지 변경
                        )
                    }
                }
            }

            // 상품 정보와 선택 옵션을 표시
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                // 상품명
                Text(
                    text = item.title,
                    fontSize = 23.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 16.dp)
                )
                // 가격 표시
                Text(
                    text = "$${item.price}",
                    fontSize = 22.sp
                )
            }
            // 평점 표시
            RatingBar(rating = item.rating)
            // 모델 선택
            ModelSelector(
                models = item.model, // 모델 리스트 전달
                selectedModelIndex = selectedModelIndex, // 현재 선택된 모델 인덱스
                onModelSelected = {selectedModelIndex = it} // 선택 시 인덱스 갱신
            )
            // 상품 설명
            Text(text = item.description,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            // 장바구니 및 구매 버튼
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                // 장바구니 버튼
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.background(
                        colorResource(R.color.lightBrown),
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.btn_2),
                        contentDescription = "Cart",
                        tint = Color.Black
                    )
                }
                // 장바구니에 추가하는 버튼
                Button(
                    onClick = onAddToCartClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.darkBrown)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(50.dp)
                ) {
                    Text(text = "Add to Cart", fontSize = 18.sp)
                }
            }
        }
    }

    // 모델 선택
    private @Composable
    fun ModelSelector(
        models: ArrayList<String>, // 모델 리스트
        selectedModelIndex: Int, // 선택된 모델 인덱스
        onModelSelected: (Int) -> Unit // 모델 선택 시 실행될 이벤트
    ) {
        LazyRow ( // 가로 스크롤
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            itemsIndexed(models) {
                index, model ->
                Box(modifier = Modifier
                    .padding(end = 16.dp)
                    .height(40.dp)
                    .then(
                        if (index == selectedModelIndex) {
                            Modifier.border(1.dp, colorResource(R.color.darkBrown)
                                , RoundedCornerShape(10.dp)
                            )
                        } else {
                            Modifier.border(1.dp, colorResource(R.color.darkBrown)
                                , RoundedCornerShape(10.dp)
                            )
                        }
                    )
                    .background(
                        if (index == selectedModelIndex) colorResource(R.color.darkBrown) else
                        colorResource(R.color.white),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onModelSelected(index) } // 클릭 시 선택된 인덱스 전달
                    .padding(horizontal = 16.dp)
                ) {
                    // 모델 이름 표시
                    Text(
                        text = model,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (index == selectedModelIndex) colorResource(R.color.white)
                        else colorResource(R.color.black),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    // 평점 표시
    @Composable
    private fun RatingBar(rating: Double) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp)
        ) {
            // Select Model 텍스트
            Text(
                text = "Select Model",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f) // 100%의 비율
            )
            // 별 아이콘
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            // 평점 표시
            Text(text = "$rating Rating", style = MaterialTheme.typography.bodyMedium)
        }
    }

    // 🖼️ 이미지 썸네일
    @Composable
    private fun ImageThumbnail(
        imageUrl: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        val backColor = if (isSelected) colorResource(R.color.darkBrown) else
            colorResource(R.color.veryLightBrown)

        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(55.dp) // 썸네일 크기
                .then(
                    if (isSelected) {
                        Modifier.border( // 테두리
                            1.dp,
                            colorResource(R.color.darkBrown),
                            RoundedCornerShape(10.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .background(backColor, shape = RoundedCornerShape(10.dp))
                .clickable(onClick = onClick)
        ) {
            // 이미지 로딩
            Image(painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop, // 이미지를 썸네일 크기에 맞게 자름
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}