package jo.onlineshop.Model

import java.io.Serializable

data class ItemsModel(
    val title:String="", // 상품 이름
    var description:String="", // 상품 설명
    var picUrl: ArrayList<String> = ArrayList(), // 상품 이미지 URL 리스트
    var model: ArrayList<String> = ArrayList(), // 모델 정보
    var price: Double = 0.0, // 가격
    var rating: Double = 0.0, // 평점
    var numberInCart: Int = 0, // 장바구니에 담긴 개수
    var showRecommended: Boolean = false, // 추천 상품 여부
    var categoryId: String = "" // 카테고리
) : Serializable // Bundle 전달과 저장 가능
