package ru.klopskiy.usersbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.klopskiy.usersbook.model.Book;
import ru.klopskiy.usersbook.model.Person;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByOwner(Person owner);

    List<Book> findByTitleStartingWithIgnoreCase(String title);
}
