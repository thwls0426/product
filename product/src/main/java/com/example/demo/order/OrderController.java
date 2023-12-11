package com.example.demo.order;

import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.utils.ApiUtils;
import com.example.demo.option.Option;
import com.example.demo.option.OptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/save")
    public ResponseEntity<?> save(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        OrderResponse.FindByIdDTO productDTO = orderService.save(customUserDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(productDTO)); //인증 되는지 확인해야함,, 내가어케하노 시발려나
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){//주문 결과 확인
        OrderResponse.FindByIdDTO findByIdDTO = orderService.findById(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findByIdDTO);
        return ResponseEntity.ok(apiResult);

    }

    @PutMapping("/orders/{id}/update")
    public ResponseEntity<?> update(@RequestBody OrderResponse.FindByIdDTO optionResponse){
        OrderResponse.FindByIdDTO update = orderService.update(optionResponse);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(update);
        return ResponseEntity.ok(apiResult);
    }


    // ** 삭제
    @PostMapping("/orders/clear")
    public ResponseEntity<?> clear(){
        orderService.clear();
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

}
