package jo.onlineshop.Activity

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import jo.onlineshop.Model.ItemsModel
import jo.onlineshop.R

@Composable
// 하나의 상품을 보여줌
fun PopularItem(items: List<ItemsModel>, pos: Int) {
    val context = LocalContext.current

    Column (
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = items[pos].picUrl.firstOrNull(), // 상품의 첫 번째 이미지 URL
            contentDescription = items[pos].title,
            modifier = Modifier
                .width(175.dp)
                .background(
                    colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(10.dp)
                ) // 이미지의 모서리를 둥글게 처리
                .height(195.dp)
                .clickable {
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra("object", items[pos])
                    }
                    startActivity(context, intent, null)
                }, contentScale = ContentScale.Crop // 이미지가 컨테이너를 꽉 채우도록 설정
        )
        Text(
            text = items[pos].title,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1, // 한 줄만 표시
            overflow = TextOverflow.Ellipsis, // 길면 ... 처리
            modifier = Modifier.padding(top = 8.dp)
        )
        // 평점과 가격 표시
        Row (
            modifier = Modifier
                .width(175.dp)
                .padding(top = 4.dp)
        ) {
            Row {
                Image(painter = painterResource(id = R.drawable.star) // 별 아이콘
                , contentDescription = "Rating",
                    modifier = Modifier.align(Alignment.CenterVertically) // 수직 중앙 정렬
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = items[pos].rating.toString(),
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }
            Text(
                text = "$${items[pos].price}", // 상품 가격
                color = colorResource(R.color.darkBrown),
                textAlign = TextAlign.End, // 오른쪽 정렬
                modifier = Modifier.fillMaxWidth(), // 남은 공간을 모두 차지하도록 설정
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
// 여러 상품을 가로 스크롤로 보여줌
fun ListItems(items: List<ItemsModel>) {
    LazyRow ( modifier = Modifier
        .padding(top = 8.dp)
        .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // 아이템 간 간격
    ) {
        items(items.size) { index: Int ->
            PopularItem(items, index) // 각 상품 렌더링
        }
    }
}

@Composable
// 2열 그리드 형태로 상품을 표시
fun ListItemsFullSize(items: List<ItemsModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 각 줄에 2개의 상품을 표시
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // 열 간 간격을 16dp로
    ) {
        items(items.size) { row -> // 전달받은 아이템 개수만큼 그리드 아이템 생성
            PopularItem(items, row)
        }
    }
}