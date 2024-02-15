package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.demo.controllers.PersonController;
import com.example.demo.entities.Person;
import com.example.demo.mapper.custom.PersonMapper;
import com.example.demo.repository.PersonRepository;
import com.example.demo.services.exception.DatabaseException;
import com.example.demo.services.exception.RequiredObjectIsNullException;
import com.example.demo.services.exception.ResourceNotFoundException;
import com.example.demo.vo.v1.PersonVO;
import com.example.demo.vo.v2.PersonVOV2;

@Service
public class PersonService {

    @Autowired
    PersonRepository repository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    PersonMapper mapperCustom;

    public PersonVO toPersonList(Person entity) {

        return mapper.map(entity, PersonVO.class);
    }

    public List<PersonVO> findAll() {
        List<PersonVO> list = repository.findAll().stream().map(this::toPersonList).collect(Collectors.toList());

        list.forEach(vo -> vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel()));

        return list;
    }

    public PersonVO findById(Long id) {
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Found no user"));
        PersonVO vo = mapper.map(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {
        if (person == null)
            throw new RequiredObjectIsNullException();
        var entity = mapper.map(person, Person.class);
        var vo = mapper.map(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            new DatabaseException(e.getMessage());
        }

    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        var entity = mapperCustom.convertVoToEntity(person);
        var vo = mapperCustom.convertEntityToVo(repository.save(entity));

        return vo;
    }

    public PersonVO update(PersonVO person) {
        if (person == null)
            throw new RequiredObjectIsNullException();
        var entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado"));

        entity.setFirstName(person.getFirstName());
        entity.setFirstName(person.getLastName());
        entity.setFirstName(person.getAddress());
        entity.setFirstName(person.getGender());
        entity = repository.save(entity);
        var vo = mapper.map(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }
}
