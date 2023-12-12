package com.example.demo.cart;

import com.example.demo.option.Option;
import com.example.demo.product.Product;
import com.example.demo.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;


// ** 디자인 패턴 중 브릿지 패턴과 유사함
@Data
@ToString
@NoArgsConstructor
public class CartResponse { //응답

    @Data
    public static class UpdateDTO {
        private List<CartDTO> dtoList;

        private Long totalPrice;

        public UpdateDTO(List<Cart> dtoList) {
            this.dtoList = dtoList.stream().map(CartDTO::new).collect(Collectors.toList());

            this.totalPrice = getTotalPrice(); //업데이트에 대한 응답
        }

        @Data
        public class CartDTO{

            private Long cartId;

            private Long optionId;

            public String optionName;

            private Long quantity; // 장바구니 총 수량.

            private Long price;

            public CartDTO(Cart cart) {
                this.cartId = cart.getId();
                this.optionId = cart.getOption().getId();
                this.optionName = cart.getOption().getOptionName();
                this.quantity = cart.getQuantity();
                this.price = cart.getPrice();
            }
        }


    }

    @Data
    public static class FindAllDTO { //받아야 하는거라 응답.
        List<ProductDTO> products; // 뒤에 new ArrayList 초기화는 여기서 진행 안함

        private Long totalPrice;

        public FindAllDTO(List<Cart> cartList) { //카트리스트 안에 있는 원소 안 : 카트.
            this.products = cartList.stream()
                    .map(cart -> cart.getOption().getProduct()).distinct() //distinct << 중복된 함수 제함.
                    .map(product -> new ProductDTO(cartList, product)).collect(Collectors.toList()); // 람다식 : 안에 들어있는 전체 내용에 대한 접근. for문이 생략되어있음.
            // product에 있는 걸 뭐라고?...

            /* 최초에 받는거 : 카트, 반환되는형태, List
               카
               프로덕트에 정보를 카트에 보관해서 쓰려고 이렇게 작성

            * */

            this.totalPrice = cartList.stream() //전체상품에 대한 금액이니, 저장할 필요 x
                    .mapToLong(cart -> cart.getOption().getPrice() * cart.getQuantity()) // 수량에 대한 금액 반환
                    .sum(); // 전체 합 반환
        }

        @Data
        public class ProductDTO { //프로덕트가 카트정보를 들고있지 않아서 여기에서 바로 만듦. 내부적으로만 사용할거라 static 뺌

            private Long id;
            private String productName;
            List<CartDTO> cartDTOS;

            public ProductDTO(List<Cart> cartList, Product product) {
                this.id = product.getId();
                this.productName = product.getProductName();
                this.cartDTOS = cartList.stream()
                        .filter(cart -> cart.getOption().getProduct().getId() == product.getId())
                        .map(CartDTO::new).collect(Collectors.toList());
            }

            @Data
            public class CartDTO{ //카트정보

                private Long id;
                private OptionDTO optionDTO;
                private Long price;
                private Long quantity;

                public CartDTO(Cart cart) {
                    this.id = cart.getId();
                    this.optionDTO = new OptionDTO(cart.getOption());
                    this.price = cart.getPrice();
                    this.quantity = cart.getQuantity();
                }

                @Data
                public class OptionDTO{

                    private Long id;
                    private String optionName;
                    private Long price;

                    public OptionDTO(Option option) {
                        this.id = option.getId();
                        this.optionName = option.getOptionName();
                        this.price = option.getPrice();
                    }
                }
            }
        }
    }
}
