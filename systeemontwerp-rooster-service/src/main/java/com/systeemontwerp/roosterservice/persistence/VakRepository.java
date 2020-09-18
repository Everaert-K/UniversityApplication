package com.systeemontwerp.roosterservice.persistence;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.systeemontwerp.roosterservice.domain.Vak;
import com.systeemontwerp.roosterservice.domain.VakStatus;

@Repository
public interface VakRepository extends MongoRepository<Vak, String>{
	
	List<Vak> findByProfId(String profId);
	
	List<Vak> findByLokaalId(String lokaalId);
	
	List<Vak> findByStatus(VakStatus vakstatus);

}
