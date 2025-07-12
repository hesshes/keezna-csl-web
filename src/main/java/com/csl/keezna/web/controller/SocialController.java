package com.csl.keezna.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csl.keezna.web.db.dto.MemberDTO;
import com.csl.keezna.web.db.dto.MemberModifyDTO;
import com.csl.keezna.web.service.MemberService;
import com.csl.keezna.web.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SocialController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/api/member/kakao")
	public Map<String, Object> getMemberFromKakao(@RequestParam("accessToken") String accessToken) {
		log.info("access Token : {}", accessToken);

		MemberDTO memberDTO = memberService.getKakaoMember(accessToken);

		Map<String, Object> claims = memberDTO.getClaims();

		String jwtAccessToken = JWTUtil.generateToken(claims, 10);
		String jwtRefreString = JWTUtil.generateToken(claims, 60 * 24);

		claims.put("accessToken", jwtAccessToken);
		claims.put("refreshToken", jwtRefreString);

		return claims;
	}

	@PutMapping("/api/member/modify")
	public Map<String, String> modify(@RequestBody MemberModifyDTO memberModifyDTO) {
		log.info("member modify: {} ", memberModifyDTO);

		memberService.modifyMember(memberModifyDTO);

		return Map.of("result", "modified");
	}
}
