package com.example.demo.product;

import com.example.demo.option.Option;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

//얘가 DTO


/* 리팩토링
*  : 비슷한 코드가 두번이상 사용되는 경우 메서드화 == 모듈화
*
* 같은 product 라고하더라도, dto는 꼭 한개가 아니어도 괜찮다*/
@Data
@ToString
@NoArgsConstructor
public class ProductResponse {

    @Data
    @NoArgsConstructor
    public static class FindAllDTO{

        // ** PK
        private Long id;

        // ** 상품명
        private String productName;

        // ** 상품설명
        private String description;

        // ** 이미지 정보
        private String image;

        // ** 가격
        private int price;

        // ** 수량. 이건 상품의 갯수가 아니고 상품 옵션의 갯수. 마우스 - 흰색 몇개 이런식으로 되어야 하기 때문에
        // 쌤이 생각하기에는 이거 오류임.
//    private int quantity; (남은수량으로 사용하고자 했지만 이건 option 에서 쓸거니까 여기서 뺌)

        public FindAllDTO(Product product) { //얘가 builder
            //원래 괄호 안에 Long id, String productName, String description, String image, int price, int quantity 있었음
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
        }
    }


    @Data
    @NoArgsConstructor
    public static class FindByIdDTO{

        // ** PK
        private Long id;

        // ** 상품명
        private String productName;

        // ** 상품설명
        private String description;

        // ** 이미지 정보
        private String image;

        // ** 가격
        private int price;

        // ** 수량. 이건 상품의 갯수가 아니고 상품 옵션의 갯수. 마우스 - 흰색 몇개 이런식으로 되어야 하기 때문에
        // 쌤이 생각하기에는 이거 오류임.
//    private int quantity; (남은수량으로 사용하고자 했지만 이건 option 에서 쓸거니까 여기서 뺌)

        private List<OptionDTO> optionList;


        public FindByIdDTO(Product product, List<Option> optionList) { //service에서 list로 받아서 list로 받아올거임.
            //원래 괄호 안에 Long id, String productName, String description, String image, int price, int quantity 있었음
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice(); //이건 낚시금액. 맨 처음 표시되는금액
            this.optionList = optionList.stream().map(OptionDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // 밑은 option 클래스 안의 entity 애들
    @Data
    @NoArgsConstructor
    public static class OptionDTO{
        private Long id;
        private String optionName;
        private Long price; //옵션금액.
        private Long quantity;

        public OptionDTO(Option option) {
            this.id = option.getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity = option.getQuantity();
        }
    }
}
