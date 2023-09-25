/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package group2.client.repository;

import group2.client.entities.Thuoc;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ann
 */
public interface ThuocRepository extends JpaRepository<Thuoc, Integer> {
    
}
