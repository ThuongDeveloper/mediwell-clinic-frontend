/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Patient;
import java.util.List;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author admin
 */
@Controller
@RequestMapping("/admin/patient")
public class PatientController {
    
//    @RequestMapping("/url")
//    public String page(Model model) {
//        model.addAttribute("attribute", "value");
//        return "view.name";
//    }
    
    
    String apiUrl = "http://localhost:8888/api/patient";
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("")
    public String page(Model model) {
        ResponseEntity<List<Patient>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Patient>>() {
        });

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Patient> listPatient = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listPatient", listPatient);
        }
        return "/admin/patient/index";
    }

    @RequestMapping("/create")
    public String create(Model model) {
        // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
        model.addAttribute("patient", new Patient());
        return "/admin/patient/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Patient patient) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Patient> request = new HttpEntity<>(patient, headers);

        ResponseEntity<Patient> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Patient.class);

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
    public String edit(Model model, @PathVariable("id") Integer id) {
        Patient patient = restTemplate.getForObject(apiUrl + "/" + id, Patient.class);
        model.addAttribute("patient", patient);
        return "/admin/patient/edit";
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
    public String delete(@PathVariable("id") Integer id) {
        restTemplate.delete(apiUrl + "/" + id);
        // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

        // Chuyển hướng về trang danh sách Casher
        return "redirect:/admin/patient";
    }
    
}
