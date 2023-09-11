/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.*;
import group2.client.service.AuthService;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;

/**
 *
 * @author Ann
 */
@Controller
public class CasherController {

    String apiUrl = "http://localhost:8888/api/casher";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AuthService authService;

    @RequestMapping("/admin/casher")
    public String page(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            ResponseEntity<List<Casher>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Casher>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Casher> listCasher = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listCasher", listCasher);
            }
            return "/admin/casher/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Casher>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Casher>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Casher> listCasher = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listCasher", listCasher);
            }
            return "/admin/casher/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Casher>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Casher>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Casher> listCasher = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listCasher", listCasher);
            }
            return "/admin/casher/index";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping("/admin/casher/create")
    public String create(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("casher", new Casher());
            return "/admin/casher/create";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("casher", new Casher());
            return "/admin/casher/create";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("casher", new Casher());
            return "/admin/casher/create";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/admin/casher/create", method = RequestMethod.POST)
    public String create(Model model, @Valid @ModelAttribute Casher casher, BindingResult bindingResult) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("casher", casher);
            return "/admin/casher/create";
        }

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Casher> request = new HttpEntity<>(casher, headers);

        ResponseEntity<Casher> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Casher.class);
        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/casher";
        } else {
            model.addAttribute("casher", casher);
            return "/admin/casher/create";
        }
    }

    @RequestMapping(value = "/admin/casher/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            Casher casher = restTemplate.getForObject(apiUrl + "/" + id, Casher.class);
            model.addAttribute("casher", casher);
            return "/admin/casher/edit";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            Casher casher = restTemplate.getForObject(apiUrl + "/" + id, Casher.class);
            model.addAttribute("casher", casher);
            return "/admin/casher/edit";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            Casher casher = restTemplate.getForObject(apiUrl + "/" + id, Casher.class);
            model.addAttribute("casher", casher);
            return "/admin/casher/edit";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/admin/casher/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Casher updatedCasher) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Casher existingCasher = restTemplate.getForObject(apiUrl + "/" + updatedCasher.getId(), Casher.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "/" + updatedCasher.getId();

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<Casher> request = new HttpEntity<>(updatedCasher, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, Casher.class);
            return "redirect:/admin/casher";

        } catch (RestClientException e) {
            model.addAttribute("casher", existingCasher);
            return "/admin/casher/edit";
        }
    }

    @RequestMapping(value = "/admin/casher/delete/{id}", method = RequestMethod.GET)
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
            return "redirect:/admin/casher";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/casher";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/casher";
        } else {
            return "redirect:/login";
        }

    }
}
