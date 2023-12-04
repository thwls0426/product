package com.example.demo.product;


import com.example.demo.core.error.Exception.Exception404;
import com.example.demo.option.Option;
import com.example.demo.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository; //옵션은 옵션에서 찾아와야함


    // ** 전체 상품 검색
    public List<ProductResponse.FindAllDTO> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse.FindAllDTO> productDTOS =
                productPage.getContent().stream().map(ProductResponse.FindAllDTO::new)
                .collect(Collectors.toList());

        return productDTOS;
    }

    // ** 개별 상품 검색
    // DTO 안에 List가 포함되어 있기 때문에 List를 반환할 필요가 없으니 DTO 바로 반환.
    public ProductResponse.FindByIdDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception404("상품이 존재하지 않습니다.: " + id) );
        //상품을 찾앗는데 ex. 니트 라고 썻는데 니트가 한두개가 아닐 수 있음. 그니까 이걸 연관관계 매핑을 통해 연결해야함.
        // 니트 안의 옵션들은 옵션!


        // product.getId() 로 option 상품 검색
        List<Option> optionList = optionRepository.findByProductId(product.getId());//product의 id를 받아서 찾아와야함.
        // 한두개가 아니기때문에 List로 받아야함

        // ** 검색이 완료된 제품 반환 -- List로.
        return new ProductResponse.FindByIdDTO(product, optionList);

    }

    @Transactional
    public void delete(Long id){
        productRepository.deleteById(id);
    }

    @Transactional
    public void update(ProductResponse.FindAllDTO findAllDTO){
        Optional<Product> optionalProduct = productRepository.findById(findAllDTO.getId());

        optionalProduct.ifPresent(product -> {
            product.update(findAllDTO);
        });
    }

}
