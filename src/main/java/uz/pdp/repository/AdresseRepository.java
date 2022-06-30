package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.Adresse;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Integer> {

	boolean existsByNameOfCityAndNameOfStreet(String nameOfCity, String nameOfStreet);
}
