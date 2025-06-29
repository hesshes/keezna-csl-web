package com.csl.keezna.web.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.csl.keezna.web.db.model.Member;
import com.csl.keezna.web.db.model.MemberRole;
import com.csl.keezna.web.db.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberRepositoryTests {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncorder;

//	@BeforeAll
//	public void testInsertMember() {
//		for (int i = 0; i < 10; i++) {
//			Member member = Member.builder().email("user" + i + "@aaa.com")
//					.pw(passwordEncorder.encode("1111")).nickname("USER" + i).build();
//			member.addRole(MemberRole.USER);
//			
//			if (i >= 5) {
//				member.addRole(MemberRole.MANAGER);
//			}
//			if (i >= 8) {
//				member.addRole(MemberRole.ADMIN);
//			}
//			log.info("===== email : {} ", member.getEmail());
//			memberRepository.save(member);
//		}
//	}
//
//	@AfterAll
//	public void testDeleteMemberTable() {
//		memberRepository.deleteAll();
//	}

	@Test
	public void testRead() {
		String email = "user9@aaa.com";
		Member member = memberRepository.getWithRoles(email);
		log.info("===== member : {}", member);
		log.info("===== member.role : {}", member.getMemberRoleList());

	}
	
	@Test
	public void testInsertMember() {
	for (int i = 0; i < 10; i++) {
		Member member = Member.builder().email("user" + i + "@aaa.com")
				.pw(passwordEncorder.encode("1111")).nickname("USER" + i).build();
		member.addRole(MemberRole.USER);
		
		if (i >= 5) {
			member.addRole(MemberRole.MANAGER);
		}
		if (i >= 8) {
			member.addRole(MemberRole.ADMIN);
		}
		log.info("===== email : {} ", member.getEmail());
		memberRepository.save(member);
	}
}

}
