/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package group2.client.repository;

import group2.client.entities.Patient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hokim
 */
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    public Patient findByEmail(String email);
    public Patient findByName(String name);
    public Patient findByUsername(String username);
    public List<Patient> findByRole(String role);
}
