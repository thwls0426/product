package com.example.demo.cart;

import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ** 카트에 상품 추가
    @PostMapping("/carts/add")
    public ResponseEntity<?> addCartList(@RequestBody @Valid List<CartRequest.SaveDTO> requestDTO,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails, //header에 등록된 토큰여부 체크
                                         /*만약 정보가 없으면 △ 여기서 바로에러. 인증상태인지 비인증상태인지 체크.*/
                                         Error error){

        cartService.addCartList(requestDTO,customUserDetails.getUser());//<< 여기서 유저정보 받아와서 여기담음


        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }
    // ** 카트 업데이트
    @PutMapping("/carts/update") //카트에 담겨있는 상품들에 관련해서 어떻게 처리하는지.
    public ResponseEntity<?> update( /*이 매개변수로 받는 값들이 에러나도 400에러 난다. 하지만 custom~가 확률이 높음*/
           @RequestBody @Valid List<CartRequest.UpdateDTO> requestDTO,
           @AuthenticationPrincipal CustomUserDetails customUserDetails,
           Error error){

        CartResponse.UpdateDTO updateDTO = cartService.update(requestDTO, customUserDetails.getUser());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updateDTO);
        return ResponseEntity.ok(apiResult);
    }


    // ** 카트 전체 상품
    @GetMapping("/carts")
    public ResponseEntity<?> carts(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        CartResponse.FindAllDTO findAllDTO = cartService.findAll();

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findAllDTO);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/carts/delete/")
    public ResponseEntity<?> delete(@RequestBody @Valid List<CartResponse.FindAllDTO> findAllDTOList,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    Error error){

        cartService.delete(findAllDTOList, customUserDetails.getUser().getId());//<< 여기서 유저정보 받아와서 여기담음


        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findAllDTOList);
        return ResponseEntity.ok(apiResult);
    }


}
