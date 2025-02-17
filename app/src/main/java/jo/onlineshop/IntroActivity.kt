package jo.onlineshop

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class IntroActivity : BaseActivity() { // BaseActivity를 상속받음
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { IntroScreen(
            onClick = {
                startActivity(Intent(this, MainActivity::class.java))
            }
        ) }
    }
}
@Composable
@Preview
// onClick: 클릭 이벤트를 처리할 함수를 받는 매개변수
// ()->Unit: 매개변수가 없고 반환값도 없는 함수 타입
// ={}: 기본값으로 빈 함수를 설정
fun IntroScreen(onClick:()->Unit={}) {
    Image( // 배경
        painter = painterResource(id=R.drawable.background),
        contentDescription = null,
        modifier = Modifier.background(Color.White)
            .fillMaxSize(),
        contentScale = ContentScale.Crop // 컨테이너를 꽉 채우되, 이미지가 잘릴 수도 있음
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id=R.drawable.fashion),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            contentScale = ContentScale.Fit // 원본 비율 유지, 이미지가 잘리지 않게 표시
        )
        Image(
            painter = painterResource(id=R.drawable.title),
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(id=R.drawable.go),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .clickable { onClick() },

        )
    }
}