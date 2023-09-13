/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.service;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import group2.client.repository.AdminRepository;
import group2.client.repository.CasherRepository;
import group2.client.repository.DoctorRepository;
import group2.client.repository.PatientRepository;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author hokim
 */
@Component
public class AuthService {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private CasherRepository casherRepo;

    @Autowired
    private JwtService jwtService;

    
    public Admin isAuthenticatedAdmin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    String role = jwtService.extractRole(cookie.getValue());
                    if (role.equals("ADMIN")) {
                        Admin admin = adminRepo.findByRole(role);
                        return admin;

                    }

                }
            }
        }
        return null;
    }
    
 
       
    
    public Doctor isAuthenticatedDoctor(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    String role = jwtService.extractRole(cookie.getValue());
                    if (role.equals("DOCTOR")) {
                        Doctor doctor = doctorRepo.findByRole(role);
                        return doctor;

                    } 

                }
            }
        }
        return null;
    }

    

    
    public Casher isAuthenticatedCasher(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    String role = jwtService.extractRole(cookie.getValue());
                     String email = jwtService.extractRole(cookie.getValue());
                
                    if (role.equals("CASHER")) {
                        Casher casher = casherRepo.findByRole(role);
                        return casher;

                    } 

                }
            }
        }
        return null;
    }
    
  
      
    
    public Patient isAuthenticatedPatient(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    String role = jwtService.extractRole(cookie.getValue());
                    if (role.equals("PATIENT")) {
                        String username = jwtService.extractUsername(cookie.getValue());
                        Patient patientByUserName = patientRepo.findByEmail(username);
                        
                        if(patientByUserName != null){
                            return patientByUserName;
                        }
                        Patient patient = patientRepo.findByRole(role);
                        return patient;
                        
                                                  

                    } 

                }
            }
        }
        return null;
    }

    

}

