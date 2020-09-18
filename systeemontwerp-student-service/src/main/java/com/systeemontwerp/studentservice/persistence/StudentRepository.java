package com.systeemontwerp.studentservice.persistence;

import org.springframework.data.repository.CrudRepository;

import com.systeemontwerp.studentservice.domain.Student;

public interface StudentRepository extends CrudRepository<Student, String> {
	
	Student findByStudentNumber(String studentNumber);
	
}
