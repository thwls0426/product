package com.example.demo.option;

import com.example.demo.product.Product;
import com.example.demo.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepository optionRepository;

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

    public void delete(Long id) {
        optionRepository.deleteById(id);
    }

//    public void update(ProductResponse.OptionDTO optionDTO) {
//        List<Option> optionalProduct = optionRepository.findByProductId(optionDTO.getId());
//
//        optionalProduct.ifPresent
//        optionRepository.save(optionDTO);
//
//    }
}
