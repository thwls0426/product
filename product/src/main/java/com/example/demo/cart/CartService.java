package com.example.demo.cart;

import com.example.demo.core.error.Exception.Exception400;
import com.example.demo.core.error.Exception.Exception404;
import com.example.demo.core.error.Exception.Exception500;
import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.option.Option;
import com.example.demo.option.OptionRepository;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Caret;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final OptionRepository optionRepository;

    public CartResponse.FindAllDTO findAll() {
        List<Cart> cartList = cartRepository.findAll();

        return new CartResponse.FindAllDTO(cartList);
    }

    // ** 카트에 상품 추가
    @Transactional
    public void addCartList(List<CartRequest.SaveDTO> saveDTOS, User user) {

        // ** 동일한 상품 예외 처리
        Set<Long> optionsId = new HashSet<>();
        //컨테이너. 동일한 데이터를 묶어줌. optionsId 가 들어와서 동일한 값이면 묶음.
        for (CartRequest.SaveDTO cart : saveDTOS){
            if(!optionsId.add(cart.getOptionId())) {
                throw new Exception400("옵션이 중복됩니다." + cart.getOptionId());
            }
        }


        // ** 상품 존재 유무 확인. 상품번호 확인. 상품에 문제가 잇는지 없는지 확인
        List<Cart> cartList = saveDTOS.stream().map(cartDTO -> {// 여러줄 작성할거면 {} 로 적는게 좋다. 함수형태라 선언 등 자유도가 높아짐
            Option option = optionRepository.findById(cartDTO.getOptionId()).orElseThrow(
                    ()-> new Exception404("해당 상품 옵션을 찾을 수 없습니다" + cartDTO.getOptionId())
            );
            return cartDTO.toEntity(option,user);
        }).collect(Collectors.toList());

        // 문제가 없으면 저장
        cartList.forEach( cart -> {
            try{
                cartRepository.save(cart);
            }catch (Exception e){
                throw new Exception500("오류가 발생 했습니다."+e.getMessage());
            }

        });
    }

    @Transactional
    public CartResponse.UpdateDTO update(List<CartRequest.UpdateDTO> requestDTO, User user) {

        // 카트에 있는 모든 정보를 가져옴
        List<Cart> cartList = cartRepository.findAllByUserId(user.getId());

        List<Long> cartIds = cartList.stream().map(cart -> cart.getId()).collect(Collectors.toList());
        List<Long> requestIds = requestDTO.stream().map(dto -> dto.getCartId()).collect(Collectors.toList());

        if (cartIds.size() == 0) { // 자료가 없음
            throw new Exception404("주문 가능한 상품이 없습니다.");
        }

        // distinct() = 동일한 키는 제거 ex) 1, 1, 3, 3, 4 -> 3개
        if (requestIds.stream().distinct().count() != requestIds.size()) {
            throw new Exception400("동일한 카트 아이디를 주문할 수 없습니다.");
        }

        for (Long requestId : requestIds) {
            if (!cartIds.contains(requestId)) {
                throw new Exception400("카트에 없는 상품은 주문할 수 없습니다." + requestId);
            }
        }

        for (CartRequest.UpdateDTO updateDTO : requestDTO) {
            for (Cart cart : cartList) {
                if (cart.getId() == updateDTO.getCartId()) {
                    cart.update(updateDTO.getQuantity(), cart.getOption().getPrice() * updateDTO.getQuantity());
                }
            }
        }

        return new CartResponse.UpdateDTO(cartList);
    }


    public void delete(List<CartResponse.FindAllDTO> findAllDTOList, Long id) {
        cartRepository.deleteById(id);
    }
}
