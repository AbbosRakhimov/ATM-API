package uz.pdp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID>{

	boolean existsByUserFullNameOrCode(String userFullName, String code);
	
	boolean existsByUserFullNameAndCode(String userFullName, String code);
	
	List<Card> findAllByUserFullName(String UserFullName);
	
	Optional<Card>findByUserFullName(String userFullName);
	
	Optional<Card> findByCode(String code);
	
	Optional<Card> findByIban(String iban);
	
//	Optional<Card> findByUuid(UUID uuid);
	
//	Optional<Card> findById(UUID id);
	
	@Query(value = "select c.code from card c", nativeQuery = true)
	List<Card> getAllCode();
	
	@Query(value = "select * from card c where c.uuid=:uuid", nativeQuery = true)
	Card getByIdd(UUID uuid);
}
