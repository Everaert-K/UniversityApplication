package com.systeemontwerp.cursusservice.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.systeemontwerp.cursusservice.domain.LiteratureItemReservation;

@Repository
public interface ReservationRepository extends MongoRepository<LiteratureItemReservation, String>{
	
	public List<LiteratureItemReservation> findByLiteratureIdAndReserveeId(String literatureId, String reserveeId);
}
