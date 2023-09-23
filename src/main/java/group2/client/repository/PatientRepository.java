/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package group2.client.repository;

import group2.client.entities.Patient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 *
 * @author hokim
 */
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    public Patient findByEmail(String email);
    public Patient findByName(String name);
    public Patient findByUsername(String username);

    boolean existsByEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE Patient p SET p.password = :newPassword WHERE p.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("newPassword") String newPassword);

    public List<Patient> findByRole(String role);

  boolean existsByEmailAndPhone(String email, String phone);

}
