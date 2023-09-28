/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import group2.client.entities.Taophieukham;
import group2.client.entities.Toathuoc;
import group2.client.service.AuthService;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Ann
 */
@Controller
@RequestMapping("/admin/listtoathuoc")
public class ListtoathuocController {

    private String apiUrllist = "http://localhost:8888/api/listtoathuoc/";
    private String apiUrl = "http://localhost:8888/api/taophieukham/";
    private String apiUrlCasher = "http://localhost:8888/api/casher/";
    private String apiUrlTypeDoctor = "http://localhost:8888/api/typedoctor/";
    private String apiUrlPatient = "http://localhost:8888/api/patient/";
    private String apiUrl_Toathuoc = "http://localhost:8888/api/toathuoc/";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AuthService authService;

    @RequestMapping("")
    public String page(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Toathuoc>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Toathuoc> listToathuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listToathuoc", listToathuoc);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "admin/listtoathuoc/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {

            ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Toathuoc>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Toathuoc> listToathuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listToathuoc", listToathuoc);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "admin/listtoathuoc/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            
            ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrllist, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Toathuoc>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Toathuoc> listToathuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listToathuoc", listToathuoc);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "admin/listtoathuoc/index";
        } else {
            return "redirect:/login";
        }

    }
}
