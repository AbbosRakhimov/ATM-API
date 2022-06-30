package uz.pdp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardAndBankAutomat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "card_id")
	@JsonIgnore
	private Card card;
	
	@ManyToOne
	@JoinColumn(name = "bank_automat_id")
	@JsonIgnore
	private BankAutomat automat;
	
	@Column
	private Integer moneyOutput;
	
	@Column
	private Integer moneyInput;
	
	@Column
	private Integer CountHundred;
	
	@Column
	private Integer CountFifty;
	
	@Column
	private Integer CountTwenty;
	
	@Column
	private Integer CountTen;
	
	@Column
	private Integer CountFive;
	
	@Column
	private Integer CountOne;
	

}
