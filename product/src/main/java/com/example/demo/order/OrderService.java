package com.example.demo.order;

import com.example.demo.cart.Cart;
import com.example.demo.cart.CartRepository;
import com.example.demo.core.error.Exception.Exception404;
import com.example.demo.core.error.Exception.Exception500;
import com.example.demo.order.item.Item;
import com.example.demo.order.item.ItemRepository;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public OrderResponse.FindByIdDTO save(User user) { //오더에 들어와서 세이브 된 이유? > 주문하려는 시도함
         // ** 장바구니 조회
        List<Cart> cartList = cartRepository.findAllByUserId(user.getId());
        // 유저의 id 를 가지고있는 동일한 카트 다 검색됨

        if(cartList.size() == 0){
            throw new Exception404("장바구니에 상품 내역이 존재하지 않습니다.");
        }


        // ** 주문 생성
        Order order = orderRepository.save(
                Order.builder().user(user).build());

        List<Item> itemList = new ArrayList<>();

        for(Cart cart : cartList){
            Item item = Item.builder()
                    .option(cart.getOption())
                    .order(order)
                    .quantity(cart.getQuantity())
                    .price(cart.getOption().getPrice() * cart.getQuantity())
                    .build();

            itemList.add(item);
        }

        try{
            itemRepository.saveAll(itemList);//리스트에 잇는거 다 저장
        }catch (Exception e){
            throw new Exception500("결제 실패"); // 세이브 중 오류
        }
        return new OrderResponse.FindByIdDTO(order, itemList);
    }

    public OrderResponse.FindByIdDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
            ()-> new Exception404("해당 주문 내역을 찾을 수 없습니다 : "+ id));

        List<Item> itemList = itemRepository.findAllByOrderId(id); //똑같은 주문내역이있음 다 들고와라


        return new OrderResponse.FindByIdDTO(order, itemList);
    }

    @Transactional
    public void clear() {
        try{
            itemRepository.deleteAll();
        }catch (Exception e){
            throw new Exception500("아이템 삭제 오류 : " + e.getMessage());
        }
    }

    @Transactional
    public OrderResponse.FindByIdDTO update(OrderResponse.FindByIdDTO orderResponse) {
        Long orderId = orderResponse.getId();

        // 주문을 찾아옴
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception404("해당 주문 내역을 찾을 수 없습니다: " + orderId));

        // 여기에서 필요한 업데이트 로직을 수행
        // 예를 들어, 주문의 상태를 변경하거나 다른 필드를 업데이트할 수 있습니다.
        // order.setStatus(orderResponse.getStatus());
        // order.setSomeField(orderResponse.getSomeField());

        // Item 업데이트
        List<Item> updatedItems = new ArrayList<>();

        for (OrderResponse.FindByIdDTO.ProductDTO productDTO : orderResponse.getProductDTOS()) {
            for (OrderResponse.FindByIdDTO.ProductDTO.ItemDTO itemDTO : productDTO.getItemDTOS()) {
                Item item = itemRepository.findById(itemDTO.getId())
                        .orElseThrow(() -> new Exception404("해당 아이템을 찾을 수 없습니다: " + itemDTO.getId()));

                // 여기에서 필요한 Item 업데이트 로직을 수행
                // 예를 들어, item.setQuantity(itemDTO.getQuantity());
                // item.setPrice(itemDTO.getPrice());

                updatedItems.add(item);
            }
        }

        // 업데이트된 Item 저장
        try {
            itemRepository.saveAll(updatedItems);
        } catch (Exception e) {
            throw new Exception500("주문 업데이트 중 오류 발생: " + e.getMessage());
        }

        return new OrderResponse.FindByIdDTO(order, updatedItems);
    }
}
