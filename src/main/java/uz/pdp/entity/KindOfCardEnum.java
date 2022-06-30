package uz.pdp.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.entity.enums.KindOfCard;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class KindOfCardEnum{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private KindOfCard kindOfCard;
	
	public String getKindOfCardName() {
		return kindOfCard.name();
	}
	
	@JsonIgnore
	@ManyToMany(mappedBy = "kindOfCardEnum")
	private Set<Card> card;
}
