package com.systeemontwerp.financialservice.persistence;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.systeemontwerp.financialservice.domain.PaymentRecord;
import com.systeemontwerp.financialservice.domain.PaymentStatus;

@Repository
public interface FinancialRecordRepository extends CrudRepository<PaymentRecord, String> {
	@Query("select r from PaymentRecord r where r.fromId=?1")
	List<PaymentRecord> findRecordsFrom(String userId);
	
	@Query("select r from PaymentRecord r where r.toId=?1")
	List<PaymentRecord> findRecordsTo(String userId);
	
	@Query("select r from PaymentRecord r where r.status=?1")
	List<PaymentRecord> findPaymentsWithStatus(PaymentStatus s);
}
