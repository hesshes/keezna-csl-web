package com.keezna.webfolio.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.keezna.webfolio.db.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@EntityGraph(attributePaths = "imageList")
	@Query("select p from Product p where p.pno = :pno")
	Optional<Product> selectOne(@Param("pno") Long pno);

	@Modifying
	@Query("update product p set p.pdelFlag = :flag where p.pno = :pno")
	int updateToDelete(@Param("pno") Long pno, @Param("flag") boolean flag);
}
