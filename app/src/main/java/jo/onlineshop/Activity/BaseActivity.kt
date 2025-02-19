package jo.onlineshop.Activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

// 부모 클래스
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전체 화면을 상태 바와 네비게이션 바까지 확장
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // 기존의 반투명 상태 바 속성을 제거하여 투명한 상태 바 또는 색상이 있는 상태 바로 변경 가능
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 앱이 상태 바와 내비게이션 바의 색상을 변경할 수 있도록 해줌.
        // window.setStatusBarColor()와 같은 메서드가 정상적으로 동작하게 해줌.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // 상태 바(Status Bar)의 아이콘 색상을 어둡게 변경하는 역할
        // 밝은 배경의 상태 바(흰색/밝은 회색)에서 검정색 아이콘을 표시하기 위해 사용
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}