package com.example.demo.product;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** 상품명, 입력값 필수
    @Column(length = 100, nullable = false)
    private String productName;

    // ** 상품설명, 입력값 필수
    @Column(length = 500, nullable = false)
    private String description;

    // ** 이미지 정보
    @Column(length = 100)
    private String image;

    // ** 가격
    private int price;

    @Builder
    public Product(Long id, String productName, String description, String image, int price) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.image = image;
        this.price = price;
    }
    
}
