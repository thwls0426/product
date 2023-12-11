package com.example.demo.cart;

import com.example.demo.option.Option;
import com.example.demo.user.User;
import lombok.Data;

public class CartRequest {
    @Data
    public static class SaveDTO{ // 하기대로 저장해달라는 요청
        private Long optionId;
        private Long quantity;

        public Cart toEntity(Option option, User user) { // 유저정보는 항상, 단, 인증이 되었을 경우. (인증정보 때문)
            return Cart.builder()
                    .option(option)
                    .quantity(quantity)
                    .price(option.getPrice() * quantity)
                    .user(user)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO{
        private Long cartId;
        private Long quantity;

    }
}
