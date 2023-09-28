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
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 */
@Controller
@RequestMapping("/admin/patient")
public class PatientController {

    String apiUrl = "http://localhost:8888/api/patient/";
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
            ResponseEntity<List<Patient>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Patient> listPatient = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "/admin/patient/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Patient>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Patient> listPatient = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "/admin/patient/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Patient>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Patient> listPatient = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "/admin/patient/index";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping("/create")
    public String create(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            model.addAttribute("patient", new Patient());
            model.addAttribute("currentAdmin", currentAdmin);
            return "/admin/patient/create";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            model.addAttribute("patient", new Patient());
            model.addAttribute("currentDoctor", currentDoctor);
            return "/admin/patient/create";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Patient>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Patient> listPatient = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "/admin/patient/index";
        } else {
            return "redirect:/login";
        }
        // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Patient patient, @RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("patient", patient);
        body.add("file", fileResource);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Patient> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Patient.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/patient";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/patient/create";
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            Patient patient = restTemplate.getForObject(apiUrl + "/" + id, Patient.class);
            model.addAttribute("patient", patient);
            model.addAttribute("currentAdmin", currentAdmin);
            return "/admin/patient/edit";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            Patient patient = restTemplate.getForObject(apiUrl + "/" + id, Patient.class);
            model.addAttribute("patient", patient);
            model.addAttribute("currentDoctor", currentDoctor);
            return "/admin/patient/edit";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            Patient patient = restTemplate.getForObject(apiUrl + "/" + id, Patient.class);
            model.addAttribute("patient", patient);
            model.addAttribute("currentCasher", currentCasher);
            return "/admin/patient/edit";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Patient updatedPatient) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Patient existingPatient = restTemplate.getForObject(apiUrl + "/" + updatedPatient.getId(), Patient.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "/" + updatedPatient.getId();

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<Patient> request = new HttpEntity<>(updatedPatient, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, Patient.class);
            return "redirect:/admin/patient";

        } catch (RestClientException e) {
            model.addAttribute("patient", existingPatient);
            return "/admin/patient/edit";
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
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
            return "redirect:/admin/patient";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/patient";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/patient";
        } else {
            return "redirect:/login";
        }

    }

}
