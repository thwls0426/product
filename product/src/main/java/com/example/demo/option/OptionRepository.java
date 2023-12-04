package com.example.demo.option;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByProductId(Long id); //이름이 쿼리문이 된다. 다른데서 이름이 충돌나기때문에.
    // 옵션에 있는 private Prouct product의 Id를 갖고오려고 하기 때문에

}
