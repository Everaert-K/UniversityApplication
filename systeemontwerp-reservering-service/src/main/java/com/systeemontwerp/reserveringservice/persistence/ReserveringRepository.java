package com.systeemontwerp.reserveringservice.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.systeemontwerp.reserveringservice.domain.Reservering;
import com.systeemontwerp.reserveringservice.domain.ReserveringType;
import com.systeemontwerp.reserveringservice.domain.Time;

@Repository
public interface ReserveringRepository extends MongoRepository<Reservering, String>{

	List<Reservering> findByLokaalId(String lokaalId);
	
	List<Reservering> findByProfId(String proflId);
	
	List<Reservering> findByDate(LocalDate date);
	
	@Query("{ 'lokaalId' : ?0, 'date' : ?1}")
	List<Reservering> findByLokaalIdAndDate(String lokaalId, LocalDate date);
	
	@Query("{ 'lokaalId' : ?0, 'date' : ?1, 'time' : ?2}" )
	List<Reservering> findByLokaalIdAndDateAndTime(String lokaalId, LocalDate date, Time time);
}
