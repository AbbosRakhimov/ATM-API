package uz.pdp.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import uz.pdp.service.AutService;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	AutService autService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			String authorization = request.getHeader("Authorization");
			if(authorization!=null && authorization.startsWith("Bearer")) {
				authorization=authorization.substring(7);
				String iban=jwtProvider.getIbanFromToken(authorization);
				if(iban!=null) {
					UserDetails userDetails = autService.loadUserByUsername(iban);
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		filterChain.doFilter(request, response);
	}

}
