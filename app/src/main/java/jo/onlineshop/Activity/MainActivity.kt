package jo.onlineshop.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import jo.onlineshop.Model.CategoryModel
import jo.onlineshop.Model.ItemsModel
import jo.onlineshop.Model.SliderModel
import jo.onlineshop.R
import jo.onlineshop.ViewModel.MainViewModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainActivityScreen {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
    }
}

@Composable
fun MainActivityScreen(onCartClick: () -> Unit) {
    val viewModel = MainViewModel()
    // banners 리스트 생성
    val banners = remember { mutableStateListOf<SliderModel>() }
    // categories 리스트 생성
    val categories = remember { mutableStateListOf<CategoryModel>() }
    // 인기 상품 리스트 생성
    val Popular = remember { mutableStateListOf<ItemsModel>() }

    // 로딩 상태 관리
    var showBannerLoading by remember { mutableStateOf(true) }
    var showCategoryLoading by remember { mutableStateOf(true) }
    var showPopularLoading by remember { mutableStateOf(true) }

    // Composable이 최초로 실행될 때 한 번만 실행됨
    // banner
    LaunchedEffect(Unit) {
        viewModel.loadBanner().observeForever { // Firebase에서 데이터를 가져오며 데이터 변화를 감지
            // 데이터가 변경되면
            banners.clear() // 기존 데이터 삭제
            banners.addAll(it) // 새로운 데이터 추가
            showBannerLoading = false // 로딩 완료
        }
    }

    // category
    LaunchedEffect(Unit) {
        viewModel.loadCategory().observeForever {
            categories.clear()
            categories.addAll(it)
            showCategoryLoading = false
        }
    }

    // Popular
    LaunchedEffect(Unit) {
        viewModel.loadPopular().observeForever {
            Popular.clear()
            Popular.addAll(it)
            showPopularLoading = false
        }
    }

    ConstraintLayout(modifier = Modifier.background(Color.White)) {
        // 제약을 줄 두 개의 요소를 생성
        val(scrollList, bottomMenu) = createRefs()
        LazyColumn ( // 세로로 스크롤되는 목록을 생성
            modifier = Modifier
                .fillMaxSize() // 전체 화면을 채우도록 설정
                .constrainAs(scrollList) { // scrollList를 부모의 중앙에 위치하도록 설정
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        ) {
            item { // LazyColumn 내부의 개별 아이템을 정의
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // 좌우 끝으로 정렬
                    verticalAlignment = Alignment.CenterVertically // 세로 중앙 정렬
                ){
                    Column {
                        Text("Welcome Back", color = Color.Black)
                        Text("Jackie",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row {
                        Image( // 검색 아이콘
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Image( // 알림 아이콘
                            painter = painterResource(R.drawable.bell_icon),
                            contentDescription = null
                        )
                    }
                }
            }

            // Banners
            item {
                if (showBannerLoading) { // 로딩중
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center // 내부 요소를 중앙 정렬
                    ) {
                        CircularProgressIndicator() // 로딩 인디케이터 표시
                    }
                } else { // 로딩 완료
                    Banners(banners) // 배너 표시
                }
            }

            item {
                Text(
                    text = "Official Brand",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                if (showCategoryLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator() // 로딩 인디케이터 표시
                    }
                } else {
                    CategoryList(categories)
                }
            }

            item {
                SectionTitle("Most Popular", "See All")
            }
            item {
                if (showPopularLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    ListItems(Popular)
                }
            }
        }

        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomMenu) { // ConstraintLayout 내에서 제약 조건을 설정
                    bottom.linkTo(parent.bottom) // 부모의 하단에 메뉴를 고정
                },
            onItemClick = onCartClick // 메뉴 아이템 클릭 시 실행할 함수 전달
        )
    }
}

@Composable
fun CategoryList(categories: SnapshotStateList<CategoryModel>) { // 상태 관리가 가능한 List
    // 선택된 카테고리의 인덱스를 저장하는 변수
    var selectedIndex by remember { mutableStateOf(-1) }
    val context = LocalContext.current

    // 가로 스크롤 리스트
    LazyRow (
        modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp), // 아이템 간 간격 24dp
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp) // 리스트의 내부 여백
    ) {
        // 카테고리 리스트의 개수를 기반으로 아이템을 생성
        items(categories.size) { index ->
            CategoryItem(item = categories[index], isSelected = selectedIndex == index,
                onItemClick = {
                    selectedIndex = index
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(context, ListItemsActivity::class.java).apply {
                            putExtra("id", categories[index].id.toString())
                            putExtra("title", categories[index].title)
                        }
                        startActivity(context, intent, null)
                    }, 500)
                }
            )
        }
    }
}

@Composable
fun CategoryItem(item: CategoryModel, isSelected: Boolean, onItemClick:()->Unit) {
    Column (
        modifier = Modifier
            .clickable (onClick = onItemClick), // 클릭 시 onItemClick 이벤트가 실행됨
        horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
    ) {
        // 비동기 이미지 로딩
        AsyncImage(
            model = (item.picUrl), // 이미지의 URL을 전달
            contentDescription = item.title,
            modifier = Modifier
                .size(if(isSelected) 60.dp else 50.dp)
                .background(
                    color = if(isSelected) colorResource(R.color.darkBrown) else colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(100.dp)
                ),
            contentScale = ContentScale.Inside, // 이미지를 컨테이너 내부에 맞춰 표시
            // 이미지에 색상 필터 효과 추가
            colorFilter = if(isSelected) {
                ColorFilter.tint(Color.White) // 이미지를 흰색으로 덮어씌움
            } else {
                ColorFilter.tint(Color.Black) // 이미지를 검은색으로 덮어씌움
            }
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Text(
            text = item.title,
            color = colorResource(R.color.darkBrown),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SectionTitle(title: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = actionText,
            color = colorResource(R.color.darkBrown)
        )
    }
}

// 배너 데이터를 받아 AutoSlidingCarousel()을 호출
@OptIn(ExperimentalPagerApi::class) // HorizontalPager 사용 허용
@Composable
fun Banners(banners: SnapshotStateList<SliderModel>) {
    AutoSlidingCarousel(banners = banners)
}

// 배너를 슬라이드하기 위한 HorizontalPager 구현
@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier.padding(top = 16.dp),
    pagerState: PagerState = remember { PagerState() }, // 현재 페이지 상태를 관리
    banners: List<SliderModel>
) {
    // 사용자가 수동으로 배너를 넘겼는지 감지
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState() // 터치 이벤트 발생 여부를 State<Boolean>으로 반환

    Column(modifier = modifier.fillMaxSize()) {
        // 배너 리스트를 수평으로 슬라이딩
        HorizontalPager (count = banners.size, state = pagerState) { page -> // 각 페이지의 인덱스를 page 매개변수로 전달
            // Coil을 사용하여 배너 이미지 로드
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current) // 이미지 요청
                    .data(banners[page].url) // 배너의 URL을 전달
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds, // 이미지를 컨테이너에 꽉 차게 표시
                modifier = Modifier // 이미지의 위치와 크기를 조정
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
                    .height(150.dp)
            )
        }
        // 현재 페이지 위치를 나타냄
        DotIndicator(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally),
            totalDots = banners.size,
            // 현재 페이지를 기반으로 선택된 도트 색상 변경
            selectedIndex = if (isDragged) pagerState.currentPage else pagerState.currentPage,
            dotSize = 8.dp // 도트 크기 지정
        )
    }
}

@Composable
fun DotIndicator(
    modifier: Modifier = Modifier, // modifier를 전달하지 않으면 Modifier 기본값이 적용
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = colorResource(R.color.darkBrown),
    unSelectedColor: Color = colorResource(R.color.grey),
    dotSize: Dp
) {
    // DotIndicator 리스트 생성
    LazyRow(
        modifier = modifier
            .wrapContentSize()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                // selectedIndex 값과 index가 같으면 선택된 색상 표시, 다르면 회색 표시
                color = if(index==selectedIndex)selectedColor else unSelectedColor,
                size = dotSize
            )
            if (index != totalDots-1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp)) // 도트 간 간격 조정
            }
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape) // 도트를 원형으로 변경
            .background(color)
    )
}

@Composable
fun BottomMenu(modifier: Modifier, onItemClick: () -> Unit) {
    Row (
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp)
            .background(
                colorResource(R.color.darkBrown),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomMenuItem(icon = painterResource(R.drawable.btn_1), text = "Explorer")
        BottomMenuItem(icon = painterResource(R.drawable.btn_2), text = "Cart", onItemClick = onItemClick)
        BottomMenuItem(icon = painterResource(R.drawable.btn_3), text = "Favorite")
        BottomMenuItem(icon = painterResource(R.drawable.btn_4), text = "Orders")
        BottomMenuItem(icon = painterResource(R.drawable.btn_5), text = "Profile")
    }
}

@Composable
// 하단 네비게이션 메뉴의 개별 아이템
fun BottomMenuItem(icon: Painter, text:String, onItemClick: (() -> Unit) ?= null) {
    Column (modifier = Modifier
        .height(70.dp)
        .clickable { onItemClick?.invoke() } // 클릭 시 onItemClick 콜백을 실행 (null이면 실행 안 됨)
        // invoke()는 함수 타입의 객체를 호출할 때 사용
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // 아이콘과 텍스트를 수평 중앙 정렬
        verticalArrangement = Arrangement.Center // 아이콘과 텍스트를 수직 중앙 정렬
    ) {
        Icon(icon, contentDescription = text, tint = Color.White)
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text(text, color = Color.White, fontSize = 10.sp)
    }
}