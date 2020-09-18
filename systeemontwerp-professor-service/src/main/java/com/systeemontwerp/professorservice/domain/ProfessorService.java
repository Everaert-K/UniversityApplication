package com.systeemontwerp.professorservice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systeemontwerp.professorservice.persistence.ProfessorRepository;

@Service
public class ProfessorService {

	private final ProfessorRepository professorRepository;
	
	@Autowired
	public ProfessorService(ProfessorRepository professorRepository) {
		this.professorRepository = professorRepository;
	}
	
	public boolean doesProfessorExist(String professorId) {
		return this.professorRepository.existsById(professorId);
	}
	
}
