package group2.client.controller.Client;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import group2.client.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/client/register")
public class RegisterController {
    String apiUrl = "http://localhost:8888/api/patient";
    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private AuthService authService;

    @RequestMapping("")
    public String home(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);



        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            model.addAttribute("patient", currentPatient);
            return "client/register/index";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            return "client/register/index";
        }

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Patient patient) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một HttpEntity với thông tin Patient để gửi yêu cầu POST
        HttpEntity<Patient> request = new HttpEntity<>(patient, headers);

        ResponseEntity<Patient> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Patient.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("attribute", "value");
            return "/client/login/login";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/client/home";
        }
    }
}
