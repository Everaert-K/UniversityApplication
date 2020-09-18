package com.systeemontwerp.professorservice.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Hours {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String Id;
	
	private Time start;
	
	private Time end;
	
	private LocalDate day;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professor_employee_number", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Professor professor;
	
	private Hours() {}
	
	public Hours(Time start, Time end, 
			LocalDate day, Professor professor) {
		this.start = start;
		this.end = end;
		this.day = day;
		this.professor = professor;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		this.Id = id;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end = end;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
}
