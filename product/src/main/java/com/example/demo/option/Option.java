package com.example.demo.option;

import com.example.demo.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "option_tb",
        indexes = {
            @Index(name = "option_product_id_index", columnList = "product_id")
}) //그냥 option으로하면 동일하게 사용되는 테이블이 있어서 tb붙여서 사용
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    // ** 옵션 상품 이름, 필수 입력값
    @Column(length = 100, nullable = false)
    private String optionName;

    // ** 옵션 상품 가격
    private Long price;

    // ** 옵션 상품 수량
    private Long quantity;

}
