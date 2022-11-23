package ru.klopskiy.usersbook.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.klopskiy.usersbook.model.Person;
import ru.klopskiy.usersbook.services.PersonService;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (personService.getPersonByName(person.getName()).isPresent()) {
            errors.rejectValue("name", "", "Имя должно быть уникальным");
        }
    }
}
