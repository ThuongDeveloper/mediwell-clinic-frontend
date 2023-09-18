/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package group2.client.repository;

import group2.client.entities.Doctor;
import group2.client.entities.Lichlamviec;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hokim
 */
public interface LichlamviecRepository extends JpaRepository<Lichlamviec, Integer> {
    public List<Lichlamviec> findByDoctorId(Doctor doctor);
  
}
