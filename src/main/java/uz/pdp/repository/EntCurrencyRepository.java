package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.EntCurrency;

@Repository
public interface EntCurrencyRepository extends JpaRepository<EntCurrency, Integer>{

}
