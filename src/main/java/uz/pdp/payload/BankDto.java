package uz.pdp.payload;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.entity.User;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankDto {

	@NotNull(message = "BankName is mandatory")
	private String name;
	
	private Set<BankAutomatDto> bankAutomatDtos;
	
	private Set<CardDto> cardDtos;
	
	private List<UserDto> users;
	
	@NotNull(message = "NameOfStreet is mandatory")
	private String nameOfStreet;
	
	@NotNull(message = "NameOfCity is mandatory")
	private String nameOfCity;
	
	
	@NotNull(message ="chooseYourKindOfCard is mandatory: you can select number 1 for HUMO, 2 for UZCARD 3 for VISA")
	private Integer chooseYourKindOfCard;
	
}
