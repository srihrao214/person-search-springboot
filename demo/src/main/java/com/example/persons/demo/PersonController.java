package com.example.persons.demo;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST endpoints for person search, filtering and aggregation.
 */
@RestController

public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping
    public List<Person> getPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String sort
    ) {
        return personService.getPersons(page, size, sort);
    }

    @GetMapping("/persons/sort/firstname")
    public List<Person> sortByFirstName() {

        return personService.getPersons()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Person::getFirstName,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .limit(500)
                .toList();
    }

    @GetMapping("/persons/sort/birthday")
    public List<Person> sortByBirthday() {

        return personService.getPersons()
                .stream()
                .sorted(Comparator.comparing(Person::getBirthday))
                .limit(50)
                .toList();
    }

    @GetMapping("/persons/genders")
    public Map<String, Long> getGenderCounts() {

        return personService.getPersons()
                .stream()
                .collect(Collectors.groupingBy(
                        Person::getGender,
                        Collectors.counting()
                ));
    }

    @GetMapping("/persons/gender/{gender}")
    public List<Person> getByGender(@PathVariable String gender) {

        return personService.getPersons()
                .stream()
                .filter(p -> p.getGender().equalsIgnoreCase(gender))
                .limit(50)
                .toList();
    }

    @GetMapping("/persons/genres")
    public Map<String, Long> getGenreCounts() {

        return personService.getPersons()
                .stream()
                .flatMap(person ->
                        person.getMovieGenres()
                                .stream()
                                .flatMap(g -> Arrays.stream(g.split("\\|")))
                                .distinct()
                                .map(genre -> Map.entry(genre, person.getId()))
                )
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.counting()
                ));
    }

    @GetMapping("/persons/genre/{genre}")
    public List<Person> filterByGenre(@PathVariable String genre) {

        return personService.getPersons()
                .stream()
                .filter(person -> person.getMovieGenres()
                        .stream()
                        .flatMap(g -> Arrays.stream(g.split("\\|")))
                        .anyMatch(g -> g.equalsIgnoreCase(genre)))
                .limit(50)
                .toList();
    }
    @GetMapping("/persons/page")
    public List<Person> getPersonsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return personService.getPersons()
                .stream()
                .skip((long) page * size)
                .limit(size)
                .toList();
    }
}