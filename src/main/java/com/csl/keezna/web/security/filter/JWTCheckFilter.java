package com.csl.keezna.web.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.csl.keezna.web.db.dto.MemberDTO;
import com.csl.keezna.web.util.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if (request.getMethod().equals("OPTIONS")) {
			return true;
		}
		String path = request.getRequestURI();

		log.info("check uri : {}", path);

		// api/member/ 경로의 호출은 체크하지 않음
		if (path.startsWith("/api/member/")) {
			return true;
		}

		// 이미지 조회 경로는 체크하지 않음
		if (path.startsWith("/api/products/images/**")) {
			return true;
		}

		return false;
	}

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		log.info("JWTCHeckFilter =============================================");

		String authHeaderStr = request.getHeader("Authorization");

		log.info("==================authHeaderStr : {}", authHeaderStr);
		try {
			// Bearer accesToken
			String accessToken = authHeaderStr.substring(7);
			Map<String, Object> claims = JWTUtil.validateToken(accessToken);

			log.info("==========JWT claims : {}", claims);

			String email = String.valueOf(claims.get("email"));
			String pw = String.valueOf(claims.get("pw"));
			String nickname = String.valueOf(claims.get("nickname"));
			Boolean social = (Boolean) claims.get("social");
			List<String> roleNames = (List<String>) claims.get("roleNames");

			MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(),
					roleNames);

			log.info("============================================================");
			log.info("memberDTO : {} ", memberDTO);
			log.info("memberDTO.getAuthorities() : {}", memberDTO.getAuthorities());

			UsernamePasswordAuthenticationToken authenticationToke = new UsernamePasswordAuthenticationToken(
					memberDTO, pw, memberDTO.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToke);

			filterChain.doFilter(request, response);

		} catch (Exception e) {

			log.error("JWT Check Error................");
			log.error(e.getMessage());

			Gson gson = new Gson();
			String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(msg);
			printWriter.close();
		}
	}

}
