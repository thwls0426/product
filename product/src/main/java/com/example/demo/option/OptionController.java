package com.example.demo.option;

import com.example.demo.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OptionController {
    private final OptionService optionService;

    /** << 이렇게 쓰면 메서드에 마우스 갖다대면 주석으로 나온다!
    *@Param id
     * id 에 관련된 설명. (ProductId)
    *@return
     * List&#60;OptionResponse.FindByProductIdDTO&#62;
     **/

    // ** 옵션 개별 상품 검색
    @GetMapping("/products/{id}/options")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        List<OptionResponse.FindByProductIdDTO> optionResponses = optionService.findByProductId(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }

    // ** 옵션 전체 상품 검색
    @GetMapping("/options")
    public ResponseEntity<?> findAll() {
        List<OptionResponse.FindAllDTO> optionResponses = optionService.findAll();
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/delete/{id}/options")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        optionService.delete(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

//    @PutMapping("/update/{id}/options")
//    public ResponseEntity<?> update(@RequestBody OptionResponse.FindAllDTO findAllDTO){
//        optionService.update(findAllDTO);
//        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
//        return ResponseEntity.ok(apiResult);
//    }
}
