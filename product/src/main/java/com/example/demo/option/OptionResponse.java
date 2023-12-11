package com.example.demo.option;

import com.example.demo.product.Product;
import com.example.demo.product.ProductResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

// findall, findbyid 에 productId 가 있으니, 상품을 불러와서 상품별 옵션을 추가하도록 할 수 있다.

// 파일은 한장
@Data
@NoArgsConstructor
@ToString
public class OptionResponse {

    private Long id;

    private Long productId;

    private Product product;

    private String optionName;

    private Long price;

    private Long quantity;

    @Data
    @NoArgsConstructor
    public static class FindByProductIdDTO{
        private Long id;

        private Long productId;

        private String optionName;

        private Long price;

        private Long quantity;


        public FindByProductIdDTO(Option option){
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity = option.getQuantity();
        }
    }

    @Data
    @NoArgsConstructor
    public static class FindAllDTO{
        private Long id;

        private Long productId;

        private String optionName;

        private Long price;

        private Long quantity;


        public FindAllDTO(Option option){
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity = option.getQuantity();
        }
    }

    public void update(OptionResponse.FindAllDTO updateDTO){
        this.optionName = updateDTO.getOptionName();
        this.price = updateDTO.getPrice();
        this.quantity = updateDTO.getQuantity();
         }
}
