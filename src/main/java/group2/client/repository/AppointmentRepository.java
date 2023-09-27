/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package group2.client.repository;

import group2.client.entities.Appointment;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author hokim
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    public List<Appointment> findByDate(Date date);
    public List<Appointment> findByDoctorId(Doctor doctor);
    public List<Appointment> findByDateAndDoctorId(Date date,Doctor id);
    public List<Appointment> findByDateAndDoctorIdAndPatientId(Date date, Doctor doctor, Patient id);
    public Appointment findByStarttimeAndEndtime(String starttime, String endtime);
    @Query("SELECT appointment FROM Appointment appointment WHERE appointment.date LIKE %?1%")
    public List<Appointment> searchAppointment(String keyword);
}
