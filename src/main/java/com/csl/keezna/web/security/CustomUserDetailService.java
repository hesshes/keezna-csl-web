package com.csl.keezna.web.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.csl.keezna.web.db.dto.MemberDTO;
import com.csl.keezna.web.db.model.Member;
import com.csl.keezna.web.db.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	MemberRepository memberRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUser by username : {} ", username);
		Member member = memberRepository.getWithRoles(username);
		if (member == null)
			throw new UsernameNotFoundException("Not Found");
		MemberDTO memberDTO = new MemberDTO(member.getEmail(), member.getPw(), member.getNickname(), member.isSocial(),
				member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));
		log.info("memberDTO : {}", memberDTO);
		return memberDTO;
	}

}
