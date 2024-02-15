package com.example.demo.mapper.custom;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Person;
import com.example.demo.vo.v1.PersonVO;
import com.example.demo.vo.v2.PersonVOV2;

@Service
public class PersonMapper {

    @Autowired
    ModelMapper mapper;

    public PersonVOV2 convertEntityToVo(Person person) {
        PersonVOV2 vo = new PersonVOV2();

        vo.setId(person.getId());
        vo.setAdress(person.getAddress());
        vo.setBirthDate(new Date());
        vo.setFirstName(person.getFirstName());
        vo.setLastName(person.getLastName());
        vo.setGender(person.getGender());

        return vo;
    }

    public Person convertVoToEntity(PersonVOV2 person) {
        Person entity = new Person();

        entity.setId(person.getId());
        entity.setAddress(person.getAdress());
        // entity.setBirthDate(new Date());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setGender(person.getGender());

        return entity;
    }

    public PersonVO toPersonVOList(Person entity) {

        return mapper.map(entity, PersonVO.class);
    }

    public Person toPersonList(Person entity) {

        return mapper.map(entity, Person.class);
    }
}
