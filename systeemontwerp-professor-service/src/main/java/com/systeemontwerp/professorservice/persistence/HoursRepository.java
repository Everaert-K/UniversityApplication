package com.systeemontwerp.professorservice.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.systeemontwerp.professorservice.domain.Hours;
import com.systeemontwerp.professorservice.domain.Time;

public interface HoursRepository extends CrudRepository<Hours, String> {
	List<Hours> findByProfessorEmployeeNumberOrderByDayAscStartAsc(String professorEmployeeNumber);
	Hours findByProfessorEmployeeNumberAndStartAndDay(String professorEmployeeNumber, Time start, LocalDate day);
}
