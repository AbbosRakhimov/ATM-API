package uz.pdp.payload;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

	@NotNull(message="chooseYourKindOfCard is mandatory: you can select number 1 for HUMO, 2 for UZCARD 3 for VISA")
	private Integer chooseRole;
	
	@NotNull(message="Email is mandatory")
	private String email;
}
