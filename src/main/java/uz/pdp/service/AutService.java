package uz.pdp.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uz.pdp.entity.Card;
import uz.pdp.entity.User;
import uz.pdp.exception.CardBloced;
import uz.pdp.payload.LoginDto;
import uz.pdp.repository.CardRepository;
import uz.pdp.repository.UserRepository;
import uz.pdp.security.JwtProvider;

@Service
public class AutService implements UserDetailsService {

	@Autowired
	CardRepository cardRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtProvider provider;
	
	Card cardd = new Card();
	
	public ApiResponse login(LoginDto loginDto) {
		try {
			boolean isFound = false;
			List<Card> caCods = cardRepository.findAll();
			String iban = null;
			for (Card card : caCods) {
				if (passwordEncoder.matches(loginDto.getCode(), card.getCode())) {
					iban=card.getIban();
					isFound=true;
					break;
				}
			}
			if(isFound==false) {
				cardd.setPasswordBloced(1);
				if (cardd.getPasswordBloced()==1 || cardd.getPasswordBloced()==2 )
					return new ApiResponse("Passwort not exit"
							+ " please try again:", false);
			}
				if (cardd.getPasswordBloced()>2) {
					return new ApiResponse("Card is bloced,Card is blocked because you entered the wrong password twice,"
							+ " please contact your Bank:", false);
			}
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(iban, loginDto.getCode()));
			User user1=(User)authenticate.getPrincipal();
			String token = provider.generateToken(iban, user1.getRoles());
			return new ApiResponse("Token", true, token);
		} catch (BadCredentialsException badCredentialsException) {
			return new ApiResponse("Parol is wrong", false);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String iban) throws UsernameNotFoundException {

		Optional<Card> cOptional = cardRepository.findByIban(iban);
			if (cOptional.isPresent()) {
				Optional<User> uOptional = userRepository.findByCardUuid(cOptional.get().getUuid());
				User user = uOptional.get();
				user.setEnabled(true);
				User user1 = userRepository.save(user);
				return user1;
			}
		throw new UsernameNotFoundException("Card not found:" + iban);
	}
}
