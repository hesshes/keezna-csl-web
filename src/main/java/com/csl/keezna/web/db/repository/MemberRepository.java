package com.csl.keezna.web.db.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csl.keezna.web.db.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	@EntityGraph(attributePaths = { "memberRoleList" })
	@Query("select m from Member m where m.email = :email")
	Member getWithRoles(@Param("email") String email);
}
