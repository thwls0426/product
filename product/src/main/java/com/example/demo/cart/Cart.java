package com.example.demo.cart;

import com.example.demo.option.Option;
import com.example.demo.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "cart_tb",
       indexes = {
            @Index(name = "cart_user_id_idx", columnList = "user_id"),
            @Index(name = "cart_option_id_idx", columnList = "option_id")
       },
       uniqueConstraints = {/*고유값 설정*/
            @UniqueConstraint(name = "uk_cart_option_user", columnNames = {"user_id", "option_id"})
       })//최적화 작ㅇ업
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** 유저 별로 카트에 묶임
    // 쇼핑몰에 유저는 많음. 그러나 유저에게 하나씩 줘야하니까.
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Option option;

    @Column(nullable = false)
    private Long quantity; // 장바구니 총 수량.

    @Column(nullable = false)
    private Long price;
    @Builder
    public Cart(Long id, Option option, Long quantity, Long price, User user) {
        this.id = id;
        this.option = option;
        this.quantity = quantity;
        this.price = price;
        this.user = user;
    }

    public void update(Long quantity, Long price){
        this.quantity = quantity;
        this.price = price;
    }

}
