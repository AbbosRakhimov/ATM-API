package uz.pdp.service;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.entity.BankAutomat;
import uz.pdp.entity.Card;
import uz.pdp.entity.CardAndBankAutomat;
import uz.pdp.entity.User;
import uz.pdp.payload.CardAndBankAutomatDto;
import uz.pdp.repository.BankAutomatRepository;
import uz.pdp.repository.CardRepository;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransferService {

	@Autowired
	CardRepository cardRepository;

	@Autowired
	BankAutomatRepository bankAutomatRepository;

	@Autowired
	MethodeHelp methodeHelp;

	public ApiResponse monyTransfer(CardAndBankAutomatDto cardAndBankAutomatDto) {

		Authentication authenticate = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authenticate.getPrincipal();
		UUID uuid = user.getCard().getUuid();

		Card card = cardRepository.getByIdd(uuid);
		
		Optional<BankAutomat> oBankAutomat = bankAutomatRepository.findById(cardAndBankAutomatDto.getBankAutomatId());
		if(!oBankAutomat.isPresent()) {
			return new ApiResponse("BankAutomat with your entered id does not exist", false);
		}

		boolean successOrNot=methodeHelp.executiTransfer(user.getEmail(), card, cardAndBankAutomatDto.getAmount(),
				oBankAutomat.get(),
				cardAndBankAutomatDto.getChooseYourAmuontForDepositOrPayout()).isSuccesfuly();
		
		CardAndBankAutomat andBankAutomat = new CardAndBankAutomat();
		
		if(successOrNot) {
		 return methodeHelp.saveCardAndBankAutomatWithCurrency(andBankAutomat, cardAndBankAutomatDto.getChooseYourAmuontForDepositOrPayout(),
				 card, oBankAutomat.get(), cardAndBankAutomatDto.getAmount());
		}
		

		return new ApiResponse("Money transfer failed, because no suitable currency exists", false);

	}
}
