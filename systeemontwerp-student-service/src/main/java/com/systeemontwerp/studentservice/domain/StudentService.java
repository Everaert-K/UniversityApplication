package com.systeemontwerp.studentservice.domain;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systeemontwerp.studentservice.adapters.messaging.PaymentMethod;
import com.systeemontwerp.studentservice.adapters.messaging.PaymentRequest;
import com.systeemontwerp.studentservice.adapters.messaging.StudentMessageGateway;
import com.systeemontwerp.studentservice.persistence.StudentRepository;

@Service
public class StudentService {
	
	private final Logger logger = Logger.getLogger(StudentService.class);
	private final StudentRepository studentRepository;
	private final StudentMessageGateway messageGateway;
	private List<PaymentCompleteListener> listeners;
	
	private int registrationPrice = 900;
	
	@Autowired
	private StudentService(StudentRepository studentRepository, StudentMessageGateway messageGateway) {
		this.studentRepository = studentRepository;
		this.messageGateway = messageGateway;
		this.listeners = new ArrayList<>();
	}
	
	public void registerStudent(String studentId) throws StudentServiceException {
		// double check if the student exists
		if(!this.studentRepository.existsById(studentId)) {
			throw new StudentServiceException(studentId);
		}
		
		PaymentRequest registrationPayment = new PaymentRequest(studentId, studentId, 
				"schoolID", registrationPrice, PaymentMethod.BANCONTACT);
		
		this.messageGateway.createPayment(registrationPayment);
	}
	
	public void registerListener(PaymentCompleteListener listener) {
		this.listeners.add(listener);
	}
	
	public synchronized void paymentSuccessful(String studentId) {
		Student student = studentRepository.findById(studentId).get();
		student.setStatus(RegistrationStatus.SUCCESS);
		studentRepository.save(student);
		this.onPaymentCompleted(student, RegistrationStatus.SUCCESS);
	}
	
	public synchronized void paymentFailed(String studentId) {
		Student student = studentRepository.findById(studentId).get();
		student.setStatus(RegistrationStatus.FAILED);
		studentRepository.save(student);
		this.onPaymentCompleted(student, RegistrationStatus.FAILED);
	}
	
	public void onPaymentCompleted(Student student, RegistrationStatus status) {
		this.listeners.forEach(listener -> listener.onPaymentComplete(student));
		
		if(status == RegistrationStatus.SUCCESS) {
			this.messageGateway.emitRegistrationSuccessful(student);
		}
	}
	
	public boolean doesStudentExist(String studentId) {
		return this.studentRepository.existsById(studentId);
	}
	
}
