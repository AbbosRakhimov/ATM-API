package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.KindOfCardEnum;
import uz.pdp.entity.enums.KindOfCard;

@Repository
public interface KindOfCardEnumRepository extends JpaRepository<KindOfCardEnum, Integer>{

	KindOfCardEnum findByKindOfCard(KindOfCard kindOfCard);
}
