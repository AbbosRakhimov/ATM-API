package uz.pdp.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.entity.Adresse;
import uz.pdp.entity.BankAutomat;
import uz.pdp.entity.Card;
import uz.pdp.entity.CardAndBankAutomat;
import uz.pdp.entity.EntCurrency;
import uz.pdp.entity.enums.Currency;
import uz.pdp.payload.CardAndBankAutomatDto;
import uz.pdp.repository.BankAutomatRepository;
import uz.pdp.repository.CardAndBankAutomatRepo;
import uz.pdp.repository.CardRepository;
import uz.pdp.repository.EntCurrencyRepository;

@Component
public class MethodeHelp {

	@Autowired
	EntCurrencyRepository entCurrencyRepository;
	
	 @Autowired
	 JavaMailSender javaMailSender;
	 
	 @Autowired
	 BankAutomatRepository bankAutomatRepository;
	 
	 @Autowired
	 CardRepository cardRepository;
	 
	 @Autowired
	 CardAndBankAutomatRepo cardAndBankAutomatRepo;
	 
	 LinkedList<Currency> currenciesForDeposit;
	 LinkedList<Currency> currenciesForPayOut;
	
	//Methode returns all Currency Enums
	public Set<EntCurrency> getCurrencies(){
		Set<EntCurrency> entCurrenciesS = new HashSet<>();
		List<EntCurrency> entCurrencies = entCurrencyRepository.findAll();
		entCurrenciesS.addAll(entCurrencies);
//		for (EntCurrency entCurrency : entCurrencies) {
//			entCurrenciesS.add(entCurrency);
//		}
		return entCurrenciesS;
	}
	// retuns random enums of KindOfCard enums
//	public Set<KindOfCardEnum> getCardEnums(){
//		Set<KindOfCardEnum> kindOfCardEnums = new HashSet<>();
//		Random random = new Random();
//		List<KindOfCardEnum> kindOfCardEnumss = kindOfCardEnumRepository.findAll();
//		kindOfCardEnums.add(kindOfCardEnumss.get(random.nextInt(kindOfCardEnumss.size())));
//		return kindOfCardEnums;
//	}
	// returns expire date
	public Date getexpired_date() {
		Calendar cal = Calendar.getInstance();
		Date expired_date = new Date();
		cal.setTime(expired_date);
		cal.add(Calendar.YEAR, 3);
		expired_date = cal.getTime();
		return expired_date;
	}
	
	public int getThreeDigitsRandomNumber() {
		Random random = new Random();
		
		return random.nextInt(899)+100;			
	
	}
	
	public Boolean sendEmailForTask(String email, Adresse adresse, Double maxMoney, Double minMoney) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom("Ali@pdp.com");
			mailMessage.setTo(email);
			mailMessage.setSubject("message about BankAutomat");
			mailMessage.setText("you have received a new Task with TaskName "+adresse+" from "
			+maxMoney+" must be completed by "+maxMoney);
			javaMailSender.send(mailMessage);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public ApiResponse executiTransfer(String email, Card card, Integer amount, BankAutomat bankAutomat, 
			Integer chooseYourAmuontForDepositOrPayout) {
		
		if(card.getDateForExpire().compareTo(new Date())<0) {
			return new ApiResponse("Ruleind date expired", false);
		}
		if(bankAutomat.getMinMoney()>bankAutomat.getMaxMoney()) {
			sendEmailForTask(email, bankAutomat.getAdresse(), bankAutomat.getMaxMoney(), bankAutomat.getMinMoney());
		}
		
		return setBalance(amount, card.getBalance(), chooseYourAmuontForDepositOrPayout, card, bankAutomat);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public ApiResponse setBalance(Integer amount, Double cardBalance, Integer chooseYourAmuontForDepositOrPayout,
			Card card, BankAutomat bankAutomat) {

		if (chooseYourAmuontForDepositOrPayout == 0) {
			if (executeDeposit(amount).isSuccesfuly()) {
				currenciesForDeposit = executeDeposit(amount).getCurrencies();
				if (cardBalance == null) {
					card.setBalance(Double.valueOf(amount));
					cardRepository.save(card);
					Double maxMany = bankAutomat.getMaxMoney();
					maxMany += amount;
					bankAutomat.setMaxMoney(maxMany);
					bankAutomatRepository.save(bankAutomat);
				} else {
					cardBalance += amount;
					card.setBalance(cardBalance);
					cardRepository.save(card);
					Double maxMany = bankAutomat.getMaxMoney();
					maxMany += amount;
					bankAutomat.setMaxMoney(maxMany);
					bankAutomatRepository.save(bankAutomat);
				}

				return new ApiResponse("Deposit succesfully ", true);
			} else {
				return new ApiResponse("Deposit cannot be executed because no suitable currency exists", false, amount);
			}
		} else if (chooseYourAmuontForDepositOrPayout == 1) {
			if (amount > cardBalance)
					return new ApiResponse("you don't have enough money in your account", false);
				if (executeDeposit(amount).isSuccesfuly()) {
					currenciesForPayOut = executeDeposit(amount).getCurrencies();
					cardBalance -= amount;
					card.setBalance(cardBalance);
					cardRepository.save(card);
					Double maxMany = bankAutomat.getMaxMoney();
					maxMany -= amount;
					bankAutomat.setMaxMoney(maxMany);
					bankAutomatRepository.save(bankAutomat);
					
					return new ApiResponse("PayOut succesfully executed ", true, currenciesForPayOut);
				}else {
					return new ApiResponse("PayOut cannot be executed because no suitable currency exists", false);
				}

			}else {
				return new ApiResponse("you can choose only 0 for Deposit and 1 for Payout", false);
			}
	}
	public ApiResponse executeDeposit(Integer amount) {
		LinkedList<Currency> currencies = new LinkedList<>();
		
		for(int i=0; i<Currency.values().length; i++){
			Currency currentMony=Currency.values()[i];
			if(currentMony.getCurrency()<=amount) {
				currencies.add(currentMony);
				amount-= currentMony.getCurrency();
			}
		}
		if(amount!=0) {
			return new ApiResponse(false);
		}
		return new ApiResponse(true, currencies);
	}
	
	public ApiResponse saveCardAndBankAutomatWithCurrency(CardAndBankAutomat cardAndBankAutomat, 
			Integer ChooseYourAmuontForDepositOrPayout, Card card, BankAutomat bankAutomat, Integer amount ) {
		
		Integer countHunderd=0;
		Integer countFifty=0;
		Integer countTwenty=0;
		Integer countTen=0;
		Integer countFive=0;
		Integer countOne=0;
		
		if(ChooseYourAmuontForDepositOrPayout==0) {
			for(int i=0; i<currenciesForDeposit.size(); i++){
				if(currenciesForDeposit.get(i).getCurrency()==100)
					countHunderd++;
				if(currenciesForDeposit.get(i).getCurrency()==50)
					countFifty++;
				if(currenciesForDeposit.get(i).getCurrency()==20)
					countTwenty++;
				if(currenciesForDeposit.get(i).getCurrency()==10)
					countTen++;
				if(currenciesForDeposit.get(i).getCurrency()==5)
					countFifty++;
				if(currenciesForDeposit.get(i).getCurrency()==1)
					countOne++;				
			}
			cardAndBankAutomat.setAutomat(bankAutomat);
			cardAndBankAutomat.setCard(card);
			cardAndBankAutomat.setCountFifty(countFifty);
			cardAndBankAutomat.setCountHundred(countHunderd);
			cardAndBankAutomat.setCountOne(countOne);
			cardAndBankAutomat.setCountTen(countTen);
			cardAndBankAutomat.setCountTwenty(countTwenty);
			cardAndBankAutomat.setCountFive(countFive);
			cardAndBankAutomat.setMoneyInput(amount);
			cardAndBankAutomatRepo.save(cardAndBankAutomat);
			
			return new ApiResponse("Deposit succesfuly saved", true);
		}
		else {
			if(ChooseYourAmuontForDepositOrPayout==1) {
				for(int i=0; i<currenciesForPayOut.size(); i++){
					if(currenciesForPayOut.get(i).getCurrency()==100)
						countHunderd++;
					if(currenciesForPayOut.get(i).getCurrency()==50)
						countFifty++;
					if(currenciesForPayOut.get(i).getCurrency()==20)
						countTwenty++;
					if(currenciesForPayOut.get(i).getCurrency()==10)
						countTen++;
					if(currenciesForPayOut.get(i).getCurrency()==5)
						countFifty++;
					if(currenciesForPayOut.get(i).getCurrency()==1)
						countOne++;				
				}
				cardAndBankAutomat.setAutomat(bankAutomat);
				cardAndBankAutomat.setCard(card);
				cardAndBankAutomat.setCountFifty(countFifty);
				cardAndBankAutomat.setCountHundred(countHunderd);
				cardAndBankAutomat.setCountOne(countOne);
				cardAndBankAutomat.setCountTen(countTen);
				cardAndBankAutomat.setCountTwenty(countTwenty);
				cardAndBankAutomat.setCountFive(countFive);
				cardAndBankAutomat.setMoneyOutput(amount);
				cardAndBankAutomatRepo.save(cardAndBankAutomat);
				
			}
			return new ApiResponse("Payout succesfuly saved", true, currenciesForPayOut);
		}
	}

}
