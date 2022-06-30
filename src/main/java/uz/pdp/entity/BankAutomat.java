package uz.pdp.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.entity.enums.KindOfCard;

@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Data
@Getter
@Setter
public class BankAutomat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	private Set<KindOfCardEnum> kindOfCardEnum;
	
	@Column(nullable = false)
	private Double maxMoney;
	
	@Transient
	private Double commissionOwn;
	
	@Transient
	private Double commissionStrange;
	
	@Column(nullable = false)
	private Double minMoney;
	
//	@OneToOne
//	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "bank_automat_ent_curry", joinColumns = {@JoinColumn(name="bank_automat_id")}, inverseJoinColumns = {@JoinColumn(name="ent_currency_id")})
	private Set<EntCurrency> entCurrency;
	
//	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL )
//	@JoinTable(name = "bank_automat_card",joinColumns = {@JoinColumn(name="bank_automat_id")},inverseJoinColumns = {@JoinColumn(name="card_id")})
//	private Set<Card> cards;
	
	@JsonIgnore
	@OneToMany(mappedBy = "automat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<CardAndBankAutomat> andBankAutomats;
	
	@JsonIgnore
	@ManyToOne(optional = false)
	private Bank bank;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Adresse adresse;
}
