package com.example.demo.heteoas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import com.example.demo.services.PersonService;
import com.example.demo.services.exception.RequiredObjectIsNullException;
import com.example.demo.vo.v1.PersonVO;

import lombok.var;

import com.example.demo.entities.Person;
import com.example.demo.mocks.MockPerson;
import com.example.demo.repository.PersonRepository;
import java.util.Optional;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    Person entity;
    PersonVO entityVo;
    MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    PersonRepository repository;

    @Mock
    ModelMapper mapper;

    @BeforeEach
    void setUpMocks() {
        input = new MockPerson();
        entity = input.mockEntity(1);
        entityVo = input.mockVO(1);
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testFindById() {

        entity.setId(1L);
        entityVo.setKey(1L);

        when(mapper.map(entity, PersonVO.class)).thenReturn(entityVo);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testCreate() {

        Person persisted = entity;
        persisted.setId(1L);
        entityVo.setKey(1L);

        when(mapper.map(entity, PersonVO.class)).thenReturn(entityVo);
        when(mapper.map(entityVo, Person.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.create(entityVo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testUpdate() {

        Person persisted = entity;
        persisted.setId(1L);

        entityVo.setKey(1L);

        when(mapper.map(entity, PersonVO.class)).thenReturn(entityVo);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(entityVo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testDelete() {

        entity.setId(1L);

        service.delete(1L);
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It's not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It's not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testFindAll() {
        List<Person> list = input.mockEntityList();
        int count = 0; // Mova a declaração de count para fora do loop
        for (var x : list) {
            entityVo = input.mockVO(count);
            count++;
            when(mapper.map(x, PersonVO.class)).thenReturn(entityVo);
        }
        when(repository.findAll()).thenReturn(list);

        var people = service.findAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        var personOne = people.get(1);
        assertNotNull(personOne);
        assertNotNull(personOne.getKey());
        assertNotNull(personOne.getLinks());

        // Verifique os valores dos atributos, como address, firstName, lastName e
        // gender
        assertEquals("Addres Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());
    }

}
