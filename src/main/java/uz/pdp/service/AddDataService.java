package uz.pdp.service;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.validation.Valid;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.NoArgsConstructor;
import uz.pdp.entity.Adresse;
import uz.pdp.entity.Bank;
import uz.pdp.entity.BankAutomat;
import uz.pdp.entity.Card;
import uz.pdp.entity.EntCurrency;
import uz.pdp.entity.KindOfCardEnum;
import uz.pdp.entity.Role;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.KindOfCard;
import uz.pdp.entity.enums.RoleName;
import uz.pdp.payload.BankAutomatDto;
import uz.pdp.payload.BankDto;
import uz.pdp.payload.CardDto;
import uz.pdp.payload.UserDto;
import uz.pdp.repository.AdresseRepository;
import uz.pdp.repository.BankRepository;
import uz.pdp.repository.CardRepository;
import uz.pdp.repository.EntCurrencyRepository;
import uz.pdp.repository.KindOfCardEnumRepository;
import uz.pdp.repository.RoleRepository;
import uz.pdp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDataService {

	@Autowired
	BankRepository bankRepository;

	@Autowired
	EntCurrencyRepository entCurrencyRepository;

	@Autowired
	KindOfCardEnumRepository kindOfCardEnumRepository;

	@Autowired
	CardRepository cardRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	MethodeHelp methodeHelp;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AdresseRepository adresseRepository;

	/**
	 * This is the Class that adds json data to database
	 * 
	 * @param bankDto
	 * @return ApiResponse
	 */

	public ApiResponse addAllU(BankDto bankDto) {
		if (bankDto.getCardDtos().size() == bankDto.getUsers().size()) {
			if (bankRepository.existsByName(bankDto.getName()) && adresseRepository
					.existsByNameOfCityAndNameOfStreet(bankDto.getNameOfCity(), bankDto.getNameOfStreet()))
				return new ApiResponse("Bank already exist", false);
			Bank bank = new Bank();
			bank.setName(bankDto.getName());

			// Adresse Entity has been added
			Adresse adresse = new Adresse();
			adresse.setNameOfCity(bankDto.getNameOfCity());
			adresse.setNameOfStreet(bankDto.getNameOfStreet());

			bank.setAdresse(adresse);

			// collects Card Object for User Entity
			List<Card> cardsForUser = new ArrayList<>();

			// Card Entity has been added
			Set<Card> cards = new HashSet<>();
			if (!bankDto.getCardDtos().isEmpty()) {

				List<CardDto> cardss = new ArrayList<>(bankDto.getCardDtos());
				if (cardss.size() > 1) {
					for (int i = 0; i < bankDto.getCardDtos().size() - 1; i++) {
						if (cardss.get(i).getCode().equals(cardss.get(i + 1).getCode()))
							return new ApiResponse("Password must not be the same", false);
					}
				}
				for (CardDto card : bankDto.getCardDtos()) {
					if (card.getCode().length() < 4 || card.getCode().length() > 4 || card.getCode().length() == 0)
						return new ApiResponse(
								"Code length cannot empty and be longer or shorter than four characters ", false);
					List<Card> userFullNames = cardRepository.findAllByUserFullName(card.getUserFullName());
					if (!userFullNames.isEmpty()) {
						for (Card card2 : userFullNames) {
							if (passwordEncoder.matches(card.getCode(), card2.getCode()))
								return new ApiResponse("Card already exist", false);
						}
					}
					Card card2 = new Card();
					card2.setBank(bank);

					card2.setCode(passwordEncoder.encode(card.getCode()));
					card2.setUserFullName(card.getUserFullName());
					if (bankDto.getChooseYourKindOfCard() == 1) {
						card2.setKindOfCardEnum(
								Collections.singleton(kindOfCardEnumRepository.findByKindOfCard(KindOfCard.HUMO)));
					} else if (bankDto.getChooseYourKindOfCard() == 2) {
						card2.setKindOfCardEnum(
								Collections.singleton(kindOfCardEnumRepository.findByKindOfCard(KindOfCard.UZCARD)));
					}

					else if (bankDto.getChooseYourKindOfCard() == 3) {
						card2.setKindOfCardEnum(
								Collections.singleton(kindOfCardEnumRepository.findByKindOfCard(KindOfCard.VISA)));
					} else {
						return new ApiResponse(
								" You can only choose for the Field (KIndOfCardEnum) a number between 1 and 3", false);
					}
					card2.setCodeCvv(methodeHelp.getThreeDigitsRandomNumber());
					card2.setIban((Iban.random(CountryCode.DE)).toString());
					card2.setDateForExpire(methodeHelp.getexpired_date());
					cardsForUser.add(card2);
					cards.add(card2);
				}
			}
			bank.setCards(cards);

			// User Entity has been added
			List<User> users = new ArrayList<User>();
			
			//contains User with Role Employer
			List<User> userR=new ArrayList<User>();
			
			if (!bankDto.getUsers().isEmpty()) {

				if (bankDto.getUsers().size() > 1) {
					for (int i = 0; i < bankDto.getUsers().size() - 1; i++) {
						if (bankDto.getUsers().get(i).getEmail().equals(bankDto.getUsers().get(i + 1).getEmail()))
							return new ApiResponse("Email must not be the same", false);
					}
				}

				for (int t = 0; t < bankDto.getUsers().size(); t++) {
					UserDto user1 = bankDto.getUsers().get(t);
//					user1.setEmail(user1.getEmail());
					if (userRepository.existsByEmail(user1.getEmail())) {
						return new ApiResponse("User with this Email already exist", false);
					}
					User user2= new User();
					user2.setBank(bank);
					user2.setEmail(user1.getEmail());
					for (int i = t; i <= t; i++) {
						Card card = cardsForUser.get(i);
						user2.setFullName(card.getUserFullName());
						user2.setCard(card);
						user2.setCode(card.getCode());
						user2.setIban(card.getIban());
					}
//			if (userRepository.findAll().isEmpty()) {
//				user1.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIREKTOR)));
//			} else {
//				if (userRepository.getUserWithRole(bankDto.getName(), RoleName.ROLE_DIREKTOR).isEmpty()) {
//					user1.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIREKTOR)));
//				}
//				if (userRepository.getUserWithRole(bankDto.getName(), RoleName.ROLE_MANAGER).isEmpty()) {
//					user1.setRoles(Set.of(roleRepository.findByRoleName(RoleName.ROLE_MANAGER)));
//				}
//				user1.setRoles(Set.of(roleRepository.findByRoleName(RoleName.ROLE_CUSTOM)));
//
//			}
					if ( user1.getChooseRole()== 1) {
						user2.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIREKTOR)));
					} else if (user1.getChooseRole() == 2) {
						user2.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_EMPLOYER)));
						userR.add(user2);
					}

					else if (user1.getChooseRole() == 3) {
						user2.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_CUSTOM)));
					} else {
						return new ApiResponse(
								" You can only choose for the Field (ChooseRole) a number between 1 and 3", false);
					}
					users.add(user2);
				}
			}

			bank.setUsers(users);

			// BankAutomat Entity has been added
			Set<BankAutomat> bankAutomats = new HashSet<>();
			List<BankAutomatDto> bankAutomatDtos = new ArrayList<>(bankDto.getBankAutomatDtos());
			if (!bankDto.getBankAutomatDtos().isEmpty() || bankDto.getBankAutomatDtos().iterator().next() != null) {
				for (BankAutomatDto bankAutomatDto : bankDto.getBankAutomatDtos()) {
					BankAutomat bankAutomat = new BankAutomat();
					bankAutomat.setBank(bank);
					if (bankDto.getChooseYourKindOfCard() == 1) {
						bankAutomat.setKindOfCardEnum(
								Collections.singleton(kindOfCardEnumRepository.findByKindOfCard(KindOfCard.HUMO)));
					} else if (bankDto.getChooseYourKindOfCard() == 2) {
						bankAutomat.setKindOfCardEnum(
								Collections.singleton(kindOfCardEnumRepository.findByKindOfCard(KindOfCard.UZCARD)));
					}

					else if (bankDto.getChooseYourKindOfCard() == 3) {
						bankAutomat.setKindOfCardEnum(
								Collections.singleton(kindOfCardEnumRepository.findByKindOfCard(KindOfCard.VISA)));
					} else {
						return new ApiResponse(
								" You can only choose for the Field (KIndOfCardEnum) a number between 1 and 3", false);
					}

					bankAutomat.setEntCurrency(methodeHelp.getCurrencies());
					bankAutomat.setCommissionOwn(bankAutomatDto.getCommissionOwn());
					bankAutomat.setCommissionStrange(bankAutomatDto.getCommissionStrange());
					bankAutomat.setMaxMoney(bankAutomatDto.getMaxMoney());
					bankAutomat.setMinMoney(bankAutomatDto.getMinMoney());
					
					// Adresse Entity has been added
					if (bankAutomatDtos.size() > 1) {
						for (int i = 0; i < bankAutomatDtos.size() - 1; i++) {
							if (bankAutomatDtos.get(i).getNameOfStreet().equals(bankAutomatDtos.get(i+1).getNameOfStreet()))
								return new ApiResponse("NameOfStreet must not be the same", false);
						}
					}
					Adresse adresseF = new Adresse();
					adresseF.setNameOfCity(bankAutomatDto.getNameOfCity());
					adresseF.setNameOfStreet(bankAutomatDto.getNameOfStreet());
					
					Adresse adressa= adresseRepository.save(adresseF);
					bankAutomat.setAdresse(adressa);
					
//					if (bankAutomatDtos.size() > 1) {
//						for (int i = 0; i < bankAutomatDtos.size() - 1; i++) {
//							Adresse adresseF = new Adresse();
//							adresseF.setNameOfCity(bankAutomatDtos.get(i).getNameOfCity());
//							adresseF.setNameOfStreet(bankAutomatDtos.get(i).getNameOfStreet());
//							
//							Adresse adressa= adresseRepository.save(adresseF);
//							bankAutomat.setAdresse(adressa);
//						}
//					} 
					
					if(!userR.isEmpty()) {
						if(bankDto.getBankAutomatDtos().size()==1) {
							bankAutomat.setUser(userR.get(0));
						}
						for(int i=0; i<bankDto.getBankAutomatDtos().size()-1; i++) {
							bankAutomat.setUser(userR.get(i));
						}
					}
					List<User> getListWithRole=userRepository.getUserWithRole(RoleName.ROLE_EMPLOYER.name());
					if(!getListWithRole.isEmpty()) {
						for(int i=0; i<bankDto.getBankAutomatDtos().size()-1; i++) {
							bankAutomat.setUser(getListWithRole.get(i));
						}
					}
					bankAutomats.add(bankAutomat);
					
				}
			}
			bank.setBankAutomats(bankAutomats);
			bankRepository.save(bank);
			return new ApiResponse("Three Entity succussfuly added", true);
		} else {
			return new ApiResponse("The number of cards must be the same as the number of users. "
					+ "You are either low on cards or low on users", false);
		}
	}
}
