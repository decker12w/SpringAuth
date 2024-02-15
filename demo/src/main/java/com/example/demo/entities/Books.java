package com.example.demo.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.demo.vo.BooksVO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_books")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Books implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String author;
    private LocalDateTime launch_date;
    private Double price;
    private String title;

    public Books(BooksVO booksVO) {
        this.setId(booksVO.getId());
        this.setAuthor(booksVO.getAuthor());
        this.setLaunch_date(booksVO.getLaunch_date());
        this.setPrice(booksVO.getPrice());
        this.setTitle(booksVO.getTitle());
    }
}
