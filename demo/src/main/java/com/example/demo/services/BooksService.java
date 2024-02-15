package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.demo.controllers.BooksController;
import com.example.demo.entities.Books;
import com.example.demo.repository.BooksRepository;
import com.example.demo.services.exception.DatabaseException;
import com.example.demo.services.exception.RequiredObjectIsNullException;
import com.example.demo.services.exception.ResourceNotFoundException;
import com.example.demo.vo.BooksVO;

@Service
public class BooksService {

    @Autowired
    BooksRepository repository;

    public List<BooksVO> findAll() {
        List<BooksVO> list = repository.findAll().stream().map(x -> new BooksVO(x)).collect(Collectors.toList());
        list.forEach(vo -> vo.add(linkTo(methodOn(BooksController.class).findById(vo.getId())).withSelfRel()));
        return list;
    }

    public BooksVO findById(long id) {
        Books entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Found no books"));
        BooksVO entitVo = new BooksVO(entity);
        entitVo.add(linkTo(methodOn(BooksController.class).findById(entitVo.getId())).withSelfRel());
        return entitVo;
    }

    public BooksVO create(BooksVO booksVO) {
        if (booksVO == null)
            throw new RequiredObjectIsNullException();

        Books books = new Books(booksVO);
        Books savedEntity = repository.save(books);

        BooksVO savedVO = new BooksVO(savedEntity);

        savedVO.add(linkTo(methodOn(BooksController.class).findById(savedVO.getId())).withSelfRel());

        return savedVO;
    }

    public BooksVO update(BooksVO books) {
        Books entity = repository.findById(books.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado"));

        entity.setAuthor(books.getAuthor());
        entity.setTitle(books.getTitle());
        entity.setLaunch_date(books.getLaunch_date());
        entity.setPrice(books.getPrice());
        entity = repository.save(entity);

        BooksVO entityVo = new BooksVO(entity);

        return entityVo;
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
}
