package com.example.demo.product;

import com.example.demo.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
/* @RestController는 @Controller와 @ResponseBody를 합친 것으로,
    이 어노테이션을 사용하면 메소드가 반환하는 값이 HTTP Response Body에 직접 쓰여집니다.
    따라서, 주로 데이터를 반환하는 API를 구현할 때 사용됩니다.*/
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ** 전체 상품 확인
    @GetMapping("/products")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page) {
        List<ProductResponse.FindAllDTO> productResponses = productService.findAll(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productResponses);
        return ResponseEntity.ok(apiResult);
    }

    // ** 개별 상품 확인
    @GetMapping("/products/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ProductResponse.FindByIdDTO productDTOS = productService.findById(id);
        // 상품에 대한 정보를 원하는데, 필요한거 : 상품정보, 옵션정보 2가지가 다 있어야함. 그런데 한개만 받아와짐.
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.delete(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

}