/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import group2.client.service.AuthService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Ann
 */
@Controller
public class AdminController {
    
    @Autowired
    private AuthService authService;
    
    @RequestMapping("/admin")
    public String home(Model model, HttpServletRequest request) {
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
            
        if(currentPatient != null && currentPatient.getRole().equals("PATIENT")){
            return "redirect:/forbien";
        } 
        else if(currentAdmin != null && currentAdmin.getRole().equals("ADMIN")){
            model.addAttribute("currentAdmin", currentAdmin);
            return "/admin/home";
        } 
        else if(currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")){
            model.addAttribute("currentDoctor", currentDoctor);
            return "/admin/home";
        } 
        else if(currentCasher != null && currentCasher.getRole().equals("CASHER")){
            model.addAttribute("currentCasher", currentCasher);
            return "/admin/home";
        } 
        else {
            return "redirect:/login";
        }
    }
    
}
