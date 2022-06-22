package com.khai.edu.knysh.provide_and_order_services.repository;

import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    @Modifying
    @Query("update User u set u.role = :role where u.id = :userId")
    void setRoleToUser(UserRole role, long userId);

    @Modifying
    @Query("update User u set " +
            "u.firstName = :firstName, u.lastName = :lastName, u.email = :email, u.phoneNumber = :phoneNumber " +
            "where u.id = :userId")
    void update(String firstName, String lastName, String email, String phoneNumber, long userId);

    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);

    User findByIdAndRole(long specialistId, UserRole userRole);

    List<User> findAllByRole(UserRole role);

    List<User> findAllByRoleIsNotIn(List<UserRole> roles);

    long countAllByRole(UserRole role);

    long countAllByRoleIsNotIn(List<UserRole> roles);

}
