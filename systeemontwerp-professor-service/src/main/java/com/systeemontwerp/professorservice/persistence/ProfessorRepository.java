package com.systeemontwerp.professorservice.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.systeemontwerp.professorservice.domain.Professor;

public interface ProfessorRepository extends CrudRepository<Professor, String> {
	
	Professor findByEmployeeNumber(String employeeNumber);
	
	List<Professor> findByFirstNameAndLastName(String firstName, String lastName);
}
