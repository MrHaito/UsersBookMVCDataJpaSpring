package ru.klopskiy.usersbook.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klopskiy.usersbook.model.Book;
import ru.klopskiy.usersbook.model.Person;
import ru.klopskiy.usersbook.repository.BookRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksByOwner(Person owner) {
        List<Book> books = bookRepository.findAllByOwner(owner);
        books.forEach(book -> {
            LocalDateTime startTime = book.getTaken().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            book.setOverdue(ChronoUnit.DAYS.between(startTime, LocalDateTime.now()) > 10);
        });
        return books;
    }

    public List<Book> getAll(Integer page, Integer itemsPerPage, boolean sortByYear) {
        if (sortByYear && page != null) {
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("year"))).getContent();
        } else if (page != null) {
            if (itemsPerPage == null || itemsPerPage == 0) {
                itemsPerPage = Integer.MAX_VALUE;
            }
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
        } else if (sortByYear) {
            return bookRepository.findAll(Sort.by("year"));
        } else {
            return bookRepository.findAll();
        }
    }

    public Person getOwner(int id) {
        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    public Book get(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void clear(int id) {
        Book book = get(id);
        book.setOwner(null);
        book.setTaken(null);
    }

    @Transactional
    public void addOwner(int bookId, Person person) {
        Book book = get(bookId);
        book.setOwner(person);
        book.setTaken(new Date());
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        Book updated = bookRepository.findById(id).get();

        book.setId(id);
        book.setOwner(updated.getOwner());
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findByName(String name) {
        return bookRepository.findByTitleStartingWithIgnoreCase(name);
    }
}
