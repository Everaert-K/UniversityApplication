package com.systeemontwerp.restaurantservice.Persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.systeemontwerp.restaurantservice.Domain.MenuItem;
import com.systeemontwerp.restaurantservice.Domain.Type;

@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, String> {
		
	@Query("select m from MenuItem m where m.name=?1")
	List<MenuItem> giveMenuItem(String naam);
	
	@Query("select m from MenuItem m where m.quantity=?1")
	List<MenuItem> giveItemsWithQuantity(int hoeveelheid);
	
	@Query("select m from MenuItem m where m.type=?1")
	List<MenuItem> giveItemsWithType(Type type);
	
	@Query("select m from MenuItem m where m.quantity>=?1")
	List<MenuItem> giveItemsWithQuantityBiggerOrEqual(int hoeveelheid);
	
}
