package ru.klopskiy.usersbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.klopskiy.usersbook.model.Book;
import ru.klopskiy.usersbook.model.Person;
import ru.klopskiy.usersbook.services.BookService;
import ru.klopskiy.usersbook.services.PersonService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String getAll(@RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                         @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                         Model model) {
        model.addAttribute("books", bookService.getAll(page, booksPerPage, sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable int id,
                      @ModelAttribute("owner") Person person,
                      Model model) {
        Person bookOwner = bookService.getOwner(id);

        model.addAttribute("book", bookService.get(id));
        if (bookOwner != null) {
            model.addAttribute("person", bookOwner);
        } else {
            model.addAttribute("persons", personService.getAll());
        }
        return "books/get";
    }

    @PostMapping("/{id}/clear")
    public String clearOwner(@PathVariable int id) {
        bookService.clear(id);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/add_owner")
    public String addOwner(@ModelAttribute("owner") Person person,
                           @PathVariable int id) {
        bookService.addOwner(id, person);
        return "redirect:/books/{id}";
    }


    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/create";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable int id) {
        model.addAttribute("book", bookService.get(id));
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping("/search")
    public String findByName(@RequestParam("title") String title,
                             Model model) {
        model.addAttribute("books", bookService.findByName(title));
        return "books/search";
    }
}
