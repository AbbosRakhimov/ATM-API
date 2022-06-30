package uz.pdp.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.Role;
import uz.pdp.entity.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role findByRoleName(RoleName roleName);
}
