package uz.pdp.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {

	@NotNull(message = "Code is mandatory")
	private String code;
	
//	@Email
//	@NotNull(message = "Email is mandatory")
//	@NotBlank(message= " Blank is not allowed")
//	private String UserEmail;
}
