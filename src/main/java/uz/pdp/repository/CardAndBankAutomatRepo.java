package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.CardAndBankAutomat;

@Repository
public interface CardAndBankAutomatRepo extends JpaRepository<CardAndBankAutomat, Long> {

}
