package uz.pdp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {

	boolean existsByName(String name);
	
	boolean existsByNameAndAdresseIdAndIdNot(String name, Integer adressId, Integer id);
	
	Optional<Bank> findByName(String name);
	
}
