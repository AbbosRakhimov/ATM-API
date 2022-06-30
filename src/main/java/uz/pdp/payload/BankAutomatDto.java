package uz.pdp.payload;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankAutomatDto {

	@NotNull(message = "MaxMoney is mandatory")
	private Double maxMoney;
	
	@NotNull(message = "CommissionOwn is mandatory")
	private Double commissionOwn;
	
	@NotNull(message = "CommissionStrange is mandatory")
	private Double commissionStrange;
	
	@NotNull(message = "MinMoney is mandatory")
	private Double minMoney;
	
	@NotNull(message = "NameOfStreet is mandatory")
	private String nameOfStreet;
	
	@NotNull(message = "NameOfCity is mandatory")
	private String nameOfCity;
}
