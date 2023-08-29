/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.config;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import group2.client.repository.AdminRepository;
import group2.client.repository.CasherRepository;
import group2.client.repository.DoctorRepository;
import group2.client.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author hokim
 */
@Service
public class UserConfigService implements UserDetailsService {

  

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private CasherRepository casherRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByEmail(username);
        Patient patient = patientRepository.findByEmail(username);
        Doctor doctor = doctorRepository.findByEmail(username);
        Casher casher = casherRepository.findByEmail(username);
        
        if (admin != null) {
            return new AdminConfig(admin);
        } else if (patient != null) {
            return new PatientConfig(patient);
        } else if (doctor != null) {
            return new DoctorConfig(doctor);
        } else if (casher != null) {
            return new CasherConfig(casher);
        }
        throw new UsernameNotFoundException("User not found");

    }
}

