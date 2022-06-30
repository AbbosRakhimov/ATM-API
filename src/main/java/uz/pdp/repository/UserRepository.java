package uz.pdp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.Bank;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.RoleName;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

//	@Query(value="select u.bank_id, u.uuid  from users u join bank b on u.bank_id=b.id join users_role ur on ur.users_id = u.uuid "
//			+ " join role r on r.id=ur.role_id where b.name=:bankName and r.role_name=(:roleName is null OR r.role_name=cast(:roleName as r.role_name))", nativeQuery=true)
//	List<User> getUserWithRole(String bankName, RoleName roleName );
	
	@Query(value="select * from users u join users_role ur on u.uuid=ur.users_id join role r on r.id=ur.role_id "
			+ "  where r.role_name=:roleName ", nativeQuery=true)
	List<User> getUserWithRole(String roleName );
	
//	List<User>findAllByRoles(Integer id, RoleName roleName);
	
	Optional<User> findByCardUuid(UUID id);
	
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
}
