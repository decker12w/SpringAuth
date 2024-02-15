package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entities.Person;
import com.example.demo.vo.v1.PersonVO;

@Configuration
public class ModelConfiguration {

    @Bean
    ModelMapper getModel() {
        var x = new ModelMapper();

        final var typeMap = x.typeMap(Person.class, PersonVO.class);
        typeMap.addMappings(map -> map.map(Person::getId, PersonVO::setKey));

        return x;

    }

}
