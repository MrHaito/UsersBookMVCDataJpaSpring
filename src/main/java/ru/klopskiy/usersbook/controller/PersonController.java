package ru.klopskiy.usersbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.klopskiy.usersbook.model.Person;
import ru.klopskiy.usersbook.services.BookService;
import ru.klopskiy.usersbook.services.PersonService;
import ru.klopskiy.usersbook.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final BookService bookService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonService personService, BookService bookService, PersonValidator personValidator) {
        this.personService = personService;
        this.bookService = bookService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("persons", personService.getAll());
        return "persons/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable int id,
                      Model model) {
        model.addAttribute("person", personService.get(id));
        model.addAttribute("books", bookService.getBooksByOwner(personService.get(id)));
        return "persons/get";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "persons/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "persons/create";
        }
        personService.save(person);
        return "redirect:/persons";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable int id) {
        model.addAttribute("person", personService.get(id));
        return "/persons/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable int id) {
        if (bindingResult.hasErrors()) {
            return "persons/edit";
        }
        personService.update(id, person);
        return "redirect:/persons";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        personService.delete(id);
        return "redirect:/persons";
    }
}
