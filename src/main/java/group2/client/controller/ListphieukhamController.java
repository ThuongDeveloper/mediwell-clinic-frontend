package group2.client.controller;

import group2.client.entities.*;
import group2.client.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Controller
@RequestMapping("/admin/listphieukham")
public class ListphieukhamController {
    private String apiUrllist = "http://localhost:8888/api/listphieukham/";
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
            ResponseEntity<List<Taophieukham>> response = restTemplate.exchange(apiUrl , HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Taophieukham>>() {
                    });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Taophieukham> listTaophieukham = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listTaophieukham", listTaophieukham);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "admin/listphieukham/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            if (currentDoctor.getTypeDoctorId() != null) {
                Integer typedoctorId = currentDoctor.getTypeDoctorId().getId();

                ResponseEntity<List<Taophieukham>> response = restTemplate.exchange(apiUrllist + typedoctorId, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Taophieukham>>() {
                        });

                // Kiểm tra mã trạng thái của phản hồi
                if (response.getStatusCode().is2xxSuccessful()) {
                    List<Taophieukham> listTaophieukham = response.getBody();

                    // Xử lý dữ liệu theo nhu cầu của bạn
                    model.addAttribute("listTaophieukham", listTaophieukham);
                    model.addAttribute("currentDoctor", currentDoctor);
                }
            }
            return "admin/listphieukham/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Taophieukham>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Taophieukham>>() {
                    });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Taophieukham> listTaophieukham = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listTaophieukham", listTaophieukham);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "admin/listphieukham/index";
        } else {
            return "redirect:/login";
        }

    }
}
