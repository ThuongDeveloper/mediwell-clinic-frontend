/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package group2.client.repository;

import group2.client.entities.Appointment;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hokim
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    public List<Appointment> findByDate(Date date);
    public Appointment findByStarttimeAndEndtime(String starttime, String endtime);
}
