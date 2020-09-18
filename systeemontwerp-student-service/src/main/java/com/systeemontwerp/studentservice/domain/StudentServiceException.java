package com.systeemontwerp.studentservice.domain;

public class StudentServiceException extends Exception {
	
	public StudentServiceException(String message) {
		super("Student does not exist: " + message);
	}
	
}
