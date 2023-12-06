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
    public List<ProductResponse.FindAllDTO> findAll(int page){// 여기서만 int. db에서도 사용되려면 long
        Pageable pageable = PageRequest.of(page, 3);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse.FindAllDTO> productDTOS =
                productPage.getContent().stream().map(ProductResponse.FindAllDTO::new)
                        //               ------------ -------------------------------- 실제 람다식
                        //               여기 들어가면 람다식
                        // new로 빈껍데기 생성자 만듦. 무엇을 :: << 이거 앞에거가 뭘 생성할건지 만들건지에 대한것.
                        // :: <<

                .collect(Collectors.toList());

        // ** 밑에건 복사생성자 버전
//        List<ProductResponse.FindAllDTO> test =
//                productPage.getContent().stream().map(product -> new ProductResponse.FindAllDTO(new Product()))
//                        .collect(Collectors.toList());

        return productDTOS;
    }

    // ** 개별 상품 검색
    // DTO 안에 List가 포함되어 있기 때문에 List를 반환할 필요가 없으니 DTO 바로 반환.
    public ProductResponse.FindByIdDTO findById(Long id) { //id가 db를 검색해서 갖고와서 밑의 findbyId를 쓸수잇음.. 먼소리노...씨발아...
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception404("상품이 존재하지 않습니다.: " + id) );
        //     매개변수. 아무것도 없는데 new ~ 이후를 한번 사용한다는 개념.
        //상품을 찾앗는데 ex. 니트 라고 썻는데 니트가 한두개가 아닐 수 있음. 그니까 이걸 연관관계 매핑을 통해 연결해야함.
        // 니트 안의 옵션들은 옵션!


        // product.getId() 로 option 상품 검색
        List<Option> optionList = optionRepository.findByProductId(product.getId());//product의 id를 받아서 찾아와야함.
        // 한두개가 아니기때문에 List로 받아야함

        // ** 검색이 완료된 제품 반환 -- List로.
        return new ProductResponse.FindByIdDTO(product, optionList);


        //옵션 데이터가 추가되지 안하서 오늘부터 만들어야함 20231205

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
