package com.csl.keezna.web.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csl.keezna.web.db.dto.CartItemListDTO;
import com.csl.keezna.web.db.model.CartItem;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	@Query("select "
			+ " new com.csl.keezna.web.db.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName) "
			+ " from " + "	CartItem ci inner join Cart mc on ci.part = mc "
			+ " 	left join Product p on ci.product = p " + "	left join p.imageList pi" + " where "
			+ "	mc.owner.email = :email and pi.ord = 0 " + " order by ci desc")
	public List<CartItemListDTO> getItemOfCartDTOByEmail(@Param("email") String email);

	@Query("select " + " ci " + " from " + " 	CartItem ci inner join Cart c on ci.cart = c " + " where "
			+ " c.owner.email = :email and ci.product.pno = :pno")
	public CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);

	@Query("select new com.csl.keezna.web.db.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName )"
			+ " from " + "	 CartItem ci inner join Cart mc on ci.cart = mc "
			+ " left join Product p on ci.product = p " + " left join p.imageList pi " + " where "
			+ "mc.no = :cno and pi.ord = 0 " + "order by ci desc ")
	public List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);

}
