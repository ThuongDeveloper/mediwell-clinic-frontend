/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package group2.client.repository;

import group2.client.entities.Casher;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hokim
 */
public interface CasherRepository extends JpaRepository<Casher, Integer> {
    public Casher findByEmail(String email);
    public List<Casher> findByRole(String role);
}
