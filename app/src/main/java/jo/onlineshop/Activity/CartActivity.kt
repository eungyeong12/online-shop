package jo.onlineshop.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.project1762.Helper.ManagmentCart
import jo.onlineshop.Helper.ChangeNumberItemsListener
import jo.onlineshop.Model.ItemsModel
import jo.onlineshop.R

class CartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartScreen (ManagmentCart(this), // 장바구니 관리 객체 전달
                onBackClick = {
                    finish()
                })
        }
    }
}

// 장바구니 내 상품 총 금액에 대한 세금을 계산
fun calculatorCart(managmentCart: ManagmentCart, tax: MutableState<Double>) {
    val percentTax = 0.02 // 세율 2%
    tax.value = Math.round((managmentCart.getTotalFee()*percentTax)*100)/100.0
}

@Composable
private fun CartScreen(
    managmentCart: ManagmentCart = ManagmentCart(LocalContext.current),
    onBackClick: () -> Unit
) {
    // 장바구니에 담긴 상품 목록을 상태로 관리
    val cartItems = remember { mutableStateOf(managmentCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) } // 세금 상태 관리

    // 초기 세금 계산
    calculatorCart(managmentCart, tax)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 상단 툴바
        ConstraintLayout(modifier = Modifier.padding(top = 36.dp)) {
            val (backBtn, cartTxt) = createRefs()
            // 장바구니 제목
            Text(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(cartTxt) { centerTo(parent) } // 부모 중앙 정렬
            , text = "Your Cart",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            // 뒤로가기 버튼
            Image(painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
        }

        // 장바구니가 비어있으면 'Cart Is Empty' 메시지 표시
        if (cartItems.value.isEmpty()) {
            Text(text = "Cart Is Empty", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // 장바구니 목록과 요약 표시
            CartList(cartItems = cartItems.value, managmentCart) {
                cartItems.value = managmentCart.getListCart() // 아이템 변경 시 재로드
                calculatorCart(managmentCart, tax) // 세금 다시 계산
            }
            CartSummary(
                itemTotal = managmentCart.getTotalFee(),
                tax = tax.value,
                delivery = 10.0 // 고정 배송비
            )
        }
    }
}

@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val total = itemTotal + tax + delivery // 최종 총액 계산
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        // 각 항목의 요약 (상품 총액, 세금, 배송비)
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Item Total: ",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "$$itemTotal")
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Tax: ",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "$$tax")
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Delivery: ",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "$$delivery")
        }
        // 총 결제 금액과 결제 버튼
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
        ) {
            Text(
                text = "Total: ",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "$$total")
        }
        Button(
            onClick = { /* 결제 로직 */ },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.darkBrown)
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Check Out",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun CartList(
    cartItems: ArrayList<ItemsModel>,
    managmentCart: ManagmentCart,
    onItemChange: () -> Unit // 상품 수량 변경 시 호출
) {
    LazyColumn (Modifier.padding(top = 16.dp)) {
        // 장바구니에 담긴 각 상품을 CartItem으로 구성
        items(cartItems) { item ->
            CartItem(
                cartItems,
                item = item,
                managmentCart = managmentCart,
                onItemChange = onItemChange
            )
        }
    }
}

@Composable
fun CartItem(
    cartItems: ArrayList<ItemsModel>,
    item: ItemsModel,
    managmentCart: ManagmentCart,
    onItemChange: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        val (pic, titleTxt, feeEachTime, totalEachItem, Quantity) = createRefs()
        // 상품 이미지
        Image(
            painter = rememberAsyncImagePainter(item.picUrl[0]), // 첫 번째 이미지
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .background(
                    colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(10.dp)
                )
                .constrainAs(pic) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        // 상품 제목
        Text(text = item.title,
            modifier = Modifier
                .constrainAs(titleTxt) {
                    start.linkTo(pic.end)
                    top.linkTo(pic.top)
                }
                .padding(start = 8.dp, top = 8.dp)
        )

        // 개당 가격
        Text(text = "$${item.price}", color = colorResource(R.color.darkBrown)
        , modifier = Modifier
                .constrainAs(feeEachTime) {
                    start.linkTo(titleTxt.start)
                    top.linkTo(titleTxt.bottom)
                }
                .padding(start = 8.dp, top = 8.dp)
        )

        // 전체 수량에 따른 총 금액
        Text(text = "$${item.numberInCart*item.price}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(totalEachItem) {
                    start.linkTo(titleTxt.start)
                    bottom.linkTo(pic.bottom)
                }
                .padding(start = 8.dp, top = 8.dp)
        )

        // 수량 조절 버튼
        ConstraintLayout (
            modifier = Modifier
                .width(100.dp)
                .constrainAs(Quantity) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(
                    colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(100.dp)
                )
        ) {
            val (plusCartBtn, minusCartBtn, numberItemText) = createRefs()
            // 현재 수량 표시
            Text(
                text = item.numberInCart.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(numberItemText) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            // 수량 증가 버튼
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(R.color.darkBrown),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .constrainAs(plusCartBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managmentCart.plusItem(
                            cartItems,
                            cartItems.indexOf(item),
                            object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange() // 수량 변경 시 호출
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            // 수량 감소 버튼
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(R.color.white),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .constrainAs(minusCartBtn) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managmentCart.minusItem(cartItems,
                            cartItems.indexOf(item), object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange() // 수량 변경 시 호출
                                }
                            })
                    }
            ) {
                Text(
                    text = "-",
                    color = colorResource(R.color.darkBrown),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


