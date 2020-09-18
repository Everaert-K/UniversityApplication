package com.systeemontwerp.cursusservice.persistence;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.systeemontwerp.cursusservice.domain.LiteratureItem;

@Repository
public interface LiteratureItemRepository extends MongoRepository<LiteratureItem, String>{

	public Optional<LiteratureItem> findByTitleAndAuthor(String title, String author);
}
