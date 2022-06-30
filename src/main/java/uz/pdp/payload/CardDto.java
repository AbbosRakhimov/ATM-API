package uz.pdp.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardDto {

	@NotNull(message = "Code is mandatory")
	@Length(min = 4, max = 4, message = "Code length cannot be longer or shorter than four characters ")
	private String code;
	
	@NotNull(message = "UserFullName is mandatory")
	private String userFullName;
	
}
