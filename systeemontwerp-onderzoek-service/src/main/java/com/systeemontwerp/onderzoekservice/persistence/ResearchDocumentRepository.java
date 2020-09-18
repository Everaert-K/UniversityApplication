package com.systeemontwerp.onderzoekservice.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.systeemontwerp.onderzoekservice.domain.ResearchDocument;
import com.systeemontwerp.onderzoekservice.domain.ReserveStatus;
import com.systeemontwerp.onderzoekservice.domain.Status;


public interface ResearchDocumentRepository extends CrudRepository<ResearchDocument,String> {
	
	@Query("select r from ResearchDocument r where r.status=?1")
	List<ResearchDocument> getResearchDocumentWithStatus(Status status);

	@Query("select r from ResearchDocument r where r.status=?1 and r.reserveStatus=?2")
	List<ResearchDocument> getResearchDocumentWithStatusAndReserveStatus(Status status, ReserveStatus reserveStatus);
	
	
}
