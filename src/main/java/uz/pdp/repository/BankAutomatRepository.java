package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.BankAutomat;

@Repository
// @RepositoryRestResource(path = "")
public interface BankAutomatRepository extends JpaRepository<BankAutomat, Integer> {

}
