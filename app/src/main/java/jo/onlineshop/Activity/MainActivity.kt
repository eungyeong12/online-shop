package jo.onlineshop.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import jo.onlineshop.Model.SliderModel
import jo.onlineshop.R
import jo.onlineshop.ViewModel.MainViewModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainActivityScreen()
        }
    }
}

@Composable
@Preview
fun MainActivityScreen() {
    val viewModel = MainViewModel()
    // banners 리스트 생성
    val banners = remember { mutableStateListOf<SliderModel>() }

    // 로딩 상태 관리
    var showBannerLoading by remember { mutableStateOf(true) }
    // Composable이 최초로 실행될 때 한 번만 실행됨
    LaunchedEffect(Unit) {
        viewModel.loadBanner().observeForever { //  Firebase에서 데이터를 가져오며 데이터 변화를 감지
            // 데이터가 변경되면
            banners.clear() // 기존 데이터 삭제
            banners.addAll(it) // 새로운 데이터 추가
            showBannerLoading = false // 로딩 완료
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
        }
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
