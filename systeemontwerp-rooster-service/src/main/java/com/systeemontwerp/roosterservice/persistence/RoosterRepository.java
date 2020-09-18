package com.systeemontwerp.roosterservice.persistence;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.systeemontwerp.roosterservice.domain.Rooster;
import com.systeemontwerp.roosterservice.domain.Vak;

@Repository
public interface RoosterRepository extends MongoRepository<Rooster, String>{
	
	List<Rooster> findByVakId(String vakId);
	
	List<Rooster> findByLokaalId(String lokaalId);

}
