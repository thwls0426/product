package com.example.demo.option;

import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    //좀잇다 변경해야함
    public List<OptionResponse.FindByProductIdDTO> findByProductId(Long id) {
        List<Option> optionList = optionRepository.findByProductId(id);
        List<OptionResponse.FindByProductIdDTO> optionResponses =
                optionList.stream().map(OptionResponse.FindByProductIdDTO::new)// optionResponse에 들어갈 new 생성
                .collect(Collectors.toList());

        return optionResponses;
    }

    public List<OptionResponse.FindAllDTO> findAll() {
        List<Option> optionList = optionRepository.findAll();
        List<OptionResponse.FindAllDTO> findAllDTOS =
                optionList.stream().map(OptionResponse.FindAllDTO::new)// optionResponse에 들어갈 new 생성
                        .collect(Collectors.toList());
        return findAllDTOS;
    }

    @Transactional
    public void delete(Long id) {
        optionRepository.deleteById(id);
    }



    @Transactional
    public Option update(OptionResponse.FindAllDTO findAllDTO){
        Optional<Option> optionalProduct = optionRepository.findById(findAllDTO.getId());

        optionalProduct.ifPresent(option -> {
            option.update(findAllDTO);
        });
        return null;
    }

    @Transactional
    public Option save(OptionRequest.saveDTO optionDTO) {

        Optional<Product> optionalOption = productRepository.findById(optionDTO.getProductId());

        if (optionalOption.isPresent()) {
            Product product = optionalOption.get();

            Option entity = optionDTO.toEntity(product);

            return optionRepository.save(entity);
        } else {
            throw new IllegalArgumentException("해당 ID에 대한 Option을 찾을 수 없습니다. ID: " );
        }
    }
}
