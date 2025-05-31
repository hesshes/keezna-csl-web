package com.keezna.webfolio.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.keezna.webfolio.db.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
