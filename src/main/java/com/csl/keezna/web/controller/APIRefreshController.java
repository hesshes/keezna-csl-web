package com.csl.keezna.web.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csl.keezna.web.util.CustomJWTException;
import com.csl.keezna.web.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class APIRefreshController {

	@RequestMapping("/api/member/refresh")
	public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, @RequestParam("refreshToken") String refreshToken) {
		if (refreshToken == null) {
			throw new CustomJWTException("NULL_REFRASH");
		}

		if (authHeader == null || authHeader.length() < 7) {
			throw new CustomJWTException("INVALID_STRING");
		}

		String accessToken = authHeader.substring(7);

		// Access 토크이 만료되지 않았다면
		if (checkExpiredToken(accessToken) == false) {
			return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
		}

		// Refresh 토큰 검증
		Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

		log.info("refresh ... claims : {}", claims);

		String newAccessToken = JWTUtil.generateToken(claims, 10);

		String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60 * 24)
				: refreshToken;

		return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
	}

	// 시간이 1시간 미만으로 남았다면
	private boolean checkTime(Integer exp) {
		java.util.Date expDate = new java.util.Date((long) exp * (1000));
		long gap = expDate.getTime() - System.currentTimeMillis();
		long leftMin = gap / (1000 * 60);
		return leftMin < 60;
	}

	private boolean checkExpiredToken(String token) {
		try {
			JWTUtil.validateToken(token);
		} catch (CustomJWTException ce) {
			if (ce.getMessage().equals("Expired")) {
				return true;
			}
		}
		return false;
	}
}
