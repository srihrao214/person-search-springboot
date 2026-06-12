package com.example.persons.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Service responsible for loading and querying person data.
 */
@Service
public class PersonService {

    private List<Person> persons;

    @PostConstruct
    public void loadPersons() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Resource resource =  new ClassPathResource("persons.json");

        persons = mapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<Person>>() {}
        );

        System.out.println("Loaded persons: " + persons.size());
    }

    public List<Person> getPersons() {
        return persons;
    }
    public List<Person> getPersons(int page, int size, String sort) {

        Stream<Person> stream = persons.stream();

        if ("firstName".equalsIgnoreCase(sort)) {
            stream = stream.sorted(
                    Comparator.comparing(Person::getFirstName, String.CASE_INSENSITIVE_ORDER)
            );
        }

        return stream
                .skip((long) page * size)
                .limit(size)
                .toList();
    }
}