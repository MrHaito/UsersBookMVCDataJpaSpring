package ru.klopskiy.usersbook.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klopskiy.usersbook.model.Book;
import ru.klopskiy.usersbook.model.Person;
import ru.klopskiy.usersbook.repository.BookRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksByOwner(Person owner) {
        return bookRepository.findAllByOwner(owner);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
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
        bookRepository.save(book);
    }

    @Transactional
    public void addOwner(int bookId, Person person) {
        Book book = get(bookId);
        book.setOwner(person);
        bookRepository.save(book);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }
}
