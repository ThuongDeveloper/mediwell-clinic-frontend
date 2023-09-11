/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller.Client;

import group2.client.entities.*;
import group2.client.repository.PatientRepository;
import group2.client.service.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;

/**
 *
 * @author Ann
 */
@Controller
public class ClientController {

    String apiUrl = "http://localhost:8888/api/appointment/";
    private String apiUrlDoctor = "http://localhost:8888/api/doctor/";
    private String apiUrlPatient = "http://localhost:8888/api/patient/";
    private String apiUrlTypeDoctor = "http://localhost:8888/api/typedoctor/";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PatientRepository patientRepository;

    @RequestMapping("/")
    public String home(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
       

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            model.addAttribute("patient", currentPatient);
            return "/client/home";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            return "/client/home";
        }

    }
    
    @RequestMapping("/listDoctors")
    public String listDoctors(Model model, HttpServletRequest request) {

        Patient currentPatient = authService.isAuthenticatedPatient(request);
        
        ResponseEntity<List<Doctor>> response = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
        });
        ResponseEntity<List<TypeDoctor>> responseTD = restTemplate.exchange(apiUrlTypeDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TypeDoctor>>() {
        });
        if (response.getStatusCode().is2xxSuccessful() && responseTD.getStatusCode().is2xxSuccessful() && currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            List<Doctor> listDoctor = response.getBody();
            List<TypeDoctor> listTypeDoctor = responseTD.getBody();
            model.addAttribute("listDoctor", listDoctor);
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("patient", currentPatient);
        } else if (response.getStatusCode().is2xxSuccessful() && responseTD.getStatusCode().is2xxSuccessful() && currentPatient == null) {
            List<Doctor> listDoctor = response.getBody();
            List<TypeDoctor> listTypeDoctor = responseTD.getBody();
            model.addAttribute("listDoctor", listDoctor);
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("listDoctor", listDoctor);
        }
        return "/client/listDoctors";

    }

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
    public String editProfile(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {
        
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        
        ResponseEntity<Patient> response = restTemplate.exchange(apiUrlPatient + "/" + id, HttpMethod.GET, null,
                new ParameterizedTypeReference<Patient>() {
        });
        if (response.getStatusCode().is2xxSuccessful() && currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            Patient patient = response.getBody();
            model.addAttribute("patientProfile", patient);
            model.addAttribute("patient", currentPatient);
        }

        return "/client/profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Patient updatedPatient) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Patient existingPatient = restTemplate.getForObject(apiUrlPatient + "/" + updatedPatient.getId(), Patient.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrlPatient + "/" + updatedPatient.getId();

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<Patient> request = new HttpEntity<>(updatedPatient, headers);

        restTemplate.exchange(url, HttpMethod.PUT, request, Patient.class);
        return "/client/profile";
    }

    @RequestMapping("/appointment/create")
    public String create(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {


            model.addAttribute("currentPatient", currentPatient);

            ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });

            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            if (DResponse.getStatusCode().is2xxSuccessful() && patientResponse.getStatusCode().is2xxSuccessful()) {
                // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
                List<Patient> listPatient = patientResponse.getBody();
                model.addAttribute("listPatient", listPatient);
                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);

                // Chuyển hướng về trang danh sách Casher
            }
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("appointment", new Appointment());
            return "/client/appointment";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/appointment/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Appointment appointment, @RequestParam("doctor") String doctorID, @RequestParam("patient") int patientID, @RequestParam("time_select") String timeSelect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Doctor newDoctor = new Doctor();
        newDoctor.setId(Integer.parseInt(doctorID));
        appointment.setDoctorId(newDoctor);

        Patient newPatient = new Patient();
        newPatient.setId(patientID);
        appointment.setPatientId(newPatient);

        if (timeSelect.equals("7:00 - 12:00")) {
            appointment.setStarttime("7:00");
            appointment.setEndtime("12:00");
        }
        if (timeSelect.equals("12:00 - 17:00")) {
            appointment.setStarttime("12:00");
            appointment.setEndtime("17:00");
        }
        if (timeSelect.equals("17:00 - 21:00")) {
            appointment.setStarttime("17:00");
            appointment.setEndtime("21:00");
        }

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Appointment> request = new HttpEntity<>(appointment, headers);

        ResponseEntity<Appointment> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Appointment.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
            // Chuyển hướng về trang danh sách Casher
            model.addAttribute("appointment", new Appointment());
            return "redirect:/appointment/create";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/appointment/create";
        }
    }

    @RequestMapping("/previousUrl")
    public String previousUrl(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/admin";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/admin";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/admin";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/forbien")
    public String forbien(Model model) {
        return "/auth/forbien";
    }

//    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    public String register(Model model) {
//       
//        return "/auth/register";
//
//    }
}
