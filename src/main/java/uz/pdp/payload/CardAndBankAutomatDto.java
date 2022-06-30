package uz.pdp.payload;

import javax.validation.constraints.NotNull;
import javax.websocket.OnMessage;

import org.hibernate.annotations.Check;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardAndBankAutomatDto {

	@NotNull(message = "BankAutomatId is mandatory")
	private Integer bankAutomatId;
	
	@NotNull(message =  "chooseYourAmuontForDepositOrPayout is mandarory: Choose 0 for Payout and 1 for Deposit")
	private Integer chooseYourAmuontForDepositOrPayout;
	
	@NotNull(message = "amount is mandatory")
	private Integer amount;
}
