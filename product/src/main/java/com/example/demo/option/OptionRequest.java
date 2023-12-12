package com.example.demo.option;

import com.example.demo.product.Product;
import com.example.demo.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;

@Data
public class OptionRequest {

    @NoArgsConstructor
    @Data
    public static class saveDTO {
        private Long id;

        private Long productId;

        private String optionName;

        private Long price;

        private Long quantity;

        public saveDTO(Option option) {
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity = option.getQuantity();
        }

        public Option toEntity(Product product) {
            return Option.builder()
                    .optionName(optionName)
                    .price(price)
                    .quantity(quantity)
                    .product(product)
                    .build();
        }

    }


    @NoArgsConstructor
    @Data
    public static class updateDTO {

        private Long id;
        private String optionName;
        private  Long price;
        private Long Quantity;
        private Long productId;

        public updateDTO(Option option) {
            this.id = option.getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.Quantity = option.getQuantity();
            this.productId = option.getProduct().getId();
        }
    }
}
