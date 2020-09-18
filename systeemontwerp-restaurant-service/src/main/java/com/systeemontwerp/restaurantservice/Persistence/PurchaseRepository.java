package com.systeemontwerp.restaurantservice.Persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.systeemontwerp.restaurantservice.Domain.Purchase;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, String> {
		
	@Query("select p from Purchase p where p.from_id=?1")
	List<Purchase> getPurchasesFrom(String from);
	
	@Query("select p from Purchase p where p.to_id=?1")
	List<Purchase> getPurchasesTo(String to);
}
