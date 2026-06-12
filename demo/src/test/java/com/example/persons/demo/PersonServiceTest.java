package com.example.persons.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PersonServiceTest {

	@Autowired
	private PersonService personService;

	@Test
	void shouldLoadAllPersons() {
		assertEquals(1000, personService.getPersons().size());
	}
}
