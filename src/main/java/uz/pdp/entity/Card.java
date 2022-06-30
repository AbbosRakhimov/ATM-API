package uz.pdp.entity;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ManyToAny;
import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import uz.pdp.entity.enums.KindOfCard;

@NoArgsConstructor
@AllArgsConstructor
//@Data
@Getter
@Setter
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nameofbank","codeCvv"}))
//@EqualsAndHashCode()
public class Card {

	@Id
	@GeneratedValue
	private UUID uuid;
	
	@Column(nullable = false)
	private String iban;
	
	@Column(nullable = false)
	private Integer codeCvv;
	
	@Column(nullable = false)
//	@Size(min = 3, max = 3)
	private String userFullName;
	
	@Column(nullable = false)
	private Date dateForExpire;
	
	@Column(nullable = false)
	private String code;
	
	@Column
	private Double balance;
	
	@Transient
	private int passwordBloced=0;
	
	@Column
	private Boolean cardBloced;
	
	@Transient
	public void setPasswordBloced(int count) {
		passwordBloced+=count;
	}
	

//	@Column(nullable = false)
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name= "card_kind_of_card_enum",joinColumns = @JoinColumn(name="card_id"), inverseJoinColumns = @JoinColumn(name="kind_of_card_enum_id"))
//	@OneToOne
	private Set<KindOfCardEnum> kindOfCardEnum;
	
//	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "cards")
//	private BankAutomat automat;
	
	@JsonIgnore
	@OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<CardAndBankAutomat> andBankAutomats;
	
	@JsonIgnore
	@ManyToOne(optional = false)
	private Bank bank;
	
}
