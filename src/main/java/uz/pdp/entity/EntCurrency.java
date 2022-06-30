package uz.pdp.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.entity.enums.Currency;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EntCurrency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	@ManyToMany(mappedBy = "entCurrency")
	private Set<BankAutomat> automat;
	
	
}
