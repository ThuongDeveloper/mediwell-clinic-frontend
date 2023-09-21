/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Feedback;
import group2.client.entities.Patient;
import group2.client.service.AuthService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author admin
 */
@Controller
public class FeedbackController {
    
    String apiUrl = "http://localhost:8888/api/feedback";
    private String apiUrlPatient = "http://localhost:8888/api/patient/";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AuthService authService;

    @RequestMapping("/admin/feedback")
    public String page(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            ResponseEntity<List<Feedback>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Feedback>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Feedback> listFeedback = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listFeedback", listFeedback);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "/admin/feedback/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Feedback>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Feedback>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Feedback> listFeedback = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listFeedback", listFeedback);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "/admin/feedback/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Feedback>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Feedback>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Feedback> listFeedback = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listFeedback", listFeedback);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "/admin/feedback/index";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping("/admin/feedback/create")
    public String create(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Patient>>() {
        });
        
        if (patientResponse.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
             List<Patient> listPatient = patientResponse.getBody();
            model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentAdmin", currentAdmin);
            // Chuyển hướng về trang danh sách Casher
            
        } 
            model.addAttribute("feedback", new Feedback());
            return "/admin/feedback/create";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Patient>>() {
        });
        
        if (patientResponse.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
             List<Patient> listPatient = patientResponse.getBody();
            model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentDoctor", currentDoctor);
            // Chuyển hướng về trang danh sách Casher
            
        } 
            model.addAttribute("feedback", new Feedback());
            return "/admin/feedback/create";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Patient>>() {
        });
        
        if (patientResponse.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
             List<Patient> listPatient = patientResponse.getBody();
            model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentCasher", currentCasher);
            // Chuyển hướng về trang danh sách Casher
            
        } 
            model.addAttribute("feedback", new Feedback());
            return "/admin/feedback/create";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/admin/feedback/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Feedback feedback,@RequestParam("patient") String patientID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Patient newPatient = new Patient();
        newPatient.setId(Integer.parseInt(patientID));
        feedback.setPatientId(newPatient);
        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Feedback> request = new HttpEntity<>(feedback, headers);

        ResponseEntity<Feedback> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Feedback.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/feedback";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/feedback/create";
        }
    }

    @RequestMapping(value = "/admin/feedback/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            Feedback feedback = restTemplate.getForObject(apiUrl + "/" + id, Feedback.class);
            ResponseEntity<List<Patient>> DResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Patient>>() {
        });
     
            
        if (DResponse.getStatusCode().is2xxSuccessful()) {
            
            List<Patient> listPatient = DResponse.getBody();
            model.addAttribute("listPatient", listPatient);
            model.addAttribute("feedback", feedback);
                model.addAttribute("currentAdmin", currentAdmin);
            return "/admin/feedback/edit";
        } else {
            return "redirect:/admin/feedback";
        }
//            model.addAttribute("feedback", feedback);
//            return "/admin/feedback/edit";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            Feedback feedback = restTemplate.getForObject(apiUrl + "/" + id, Feedback.class);
            ResponseEntity<List<Patient>> DResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Patient>>() {
        });
     
            
        if (DResponse.getStatusCode().is2xxSuccessful()) {
            
            List<Patient> listPatient = DResponse.getBody();
            model.addAttribute("listPatient", listPatient);
            model.addAttribute("feedback", feedback);
                model.addAttribute("currentDoctor", currentDoctor);
            return "/admin/feedback/edit";
        } else {
            return "redirect:/admin/feedback";
        }
//            Feedback feedback = restTemplate.getForObject(apiUrl + "/" + id, Feedback.class);
//            model.addAttribute("feedback", feedback);
//            return "/admin/feedback/edit";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            Feedback feedback = restTemplate.getForObject(apiUrl + "/" + id, Feedback.class);
            ResponseEntity<List<Patient>> DResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Patient>>() {
        });
     
            
        if (DResponse.getStatusCode().is2xxSuccessful()) {
            
            List<Patient> listPatient = DResponse.getBody();
            model.addAttribute("listPatient", listPatient);
            model.addAttribute("feedback", feedback);
                model.addAttribute("currentCasher", currentCasher);
            return "/admin/feedback/edit";
        } else {
            return "redirect:/admin/feedback";
        }
//            Feedback feedback = restTemplate.getForObject(apiUrl + "/" + id, Feedback.class);
//            model.addAttribute("feedback", feedback);
//            return "/admin/feedback/edit";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/admin/feedback/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Feedback updatedFeedback) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Feedback existingFeedback = restTemplate.getForObject(apiUrl + "/" + updatedFeedback.getId(), Feedback.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "/" + updatedFeedback.getId();

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<Feedback> request = new HttpEntity<>(updatedFeedback, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, Feedback.class);
            return "redirect:/admin/feedback";

        } catch (RestClientException e) {
            model.addAttribute("feedback", existingFeedback);
            return "/admin/feedback/edit";
        }
    }

    @RequestMapping(value = "/admin/feedback/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/feedback";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/feedback";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/feedback";
        } else {
            return "redirect:/login";
        }

    }
    
}
