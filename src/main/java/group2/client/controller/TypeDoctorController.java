/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import group2.client.entities.Thuoc;
import group2.client.entities.TypeDoctor;
import group2.client.service.AuthService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/admin/typedoctor")
public class TypeDoctorController {

    private String apiUrl = "http://localhost:8888/api/typedoctor";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AuthService authService;

    @RequestMapping("")
    public String page(Model model, @ModelAttribute("MessageCreate") String MessageCreate, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            ResponseEntity<List<TypeDoctor>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<TypeDoctor> listTypeDoctor = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listTypeDoctor", listTypeDoctor);
                model.addAttribute("currentAdmin", currentAdmin);
            }

            //Kiểm tra các thông báo 
            if (MessageCreate != null) {
                model.addAttribute("messageCreate", MessageCreate);
            }
            return "admin/typedoctor/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<TypeDoctor>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<TypeDoctor> listTypeDoctor = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listTypeDoctor", listTypeDoctor);
                model.addAttribute("currentDoctor", currentDoctor);
            }

            //Kiểm tra các thông báo 
            if (MessageCreate != null) {
                model.addAttribute("messageCreate", MessageCreate);
            }
            return "admin/typedoctor/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<TypeDoctor>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<TypeDoctor> listTypeDoctor = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listTypeDoctor", listTypeDoctor);
                model.addAttribute("currentCasher", currentCasher);
            }

            //Kiểm tra các thông báo 
            if (MessageCreate != null) {
                model.addAttribute("messageCreate", MessageCreate);
            }
            return "admin/typedoctor/index";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            model.addAttribute("typeDoctor", new TypeDoctor());
            model.addAttribute("currentAdmin", currentAdmin);

            return "admin/typedoctor/create";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            model.addAttribute("typeDoctor", new TypeDoctor());
            model.addAttribute("currentDoctor", currentDoctor);

            return "admin/typedoctor/create";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            model.addAttribute("typeDoctor", new TypeDoctor());
            model.addAttribute("currentCasher", currentCasher);

            return "admin/typedoctor/create";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, TypeDoctor typeDoctor, RedirectAttributes redirectAttributes) {

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl + "/create", typeDoctor, String.class);
        if (Boolean.parseBoolean(response.getBody()) == true) {

            redirectAttributes.addFlashAttribute("MessageCreate", "Tạo thành công");
        } else {

            redirectAttributes.addFlashAttribute("MessageCreate", "Tạo thất bại");
        }

        return "redirect:/admin/typedoctor";
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
            ResponseEntity<TypeDoctor> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", TypeDoctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                TypeDoctor typeDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("typeDoctor", typeDoctor);
                model.addAttribute("currentAdmin", currentAdmin);

                return "admin/typedoctor/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/typedoctor";
            }
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<TypeDoctor> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", TypeDoctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                TypeDoctor typeDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("typeDoctor", typeDoctor);
                model.addAttribute("currentDoctor", currentDoctor);

                return "admin/typedoctor/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/typedoctor";
            }
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<TypeDoctor> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", TypeDoctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                TypeDoctor typeDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("typeDoctor", typeDoctor);
                model.addAttribute("currentCasher", currentCasher);

                return "admin/typedoctor/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/typedoctor";
            }
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String update(Model model, @PathVariable("id") Integer id, TypeDoctor updatedTypeDoctor, RedirectAttributes redirectAttributes) {
        // Gửi yêu cầu API để cập nhật thông tin TypeDoctor trong cơ sở dữ liệu

        restTemplate.put(apiUrl + "/edit", updatedTypeDoctor);
        // Chú ý rằng, phương thức put trả về void (không có phản hồi từ server)

        // Điều hướng về trang danh sách TypeDoctor với thông báo thành công
        redirectAttributes.addFlashAttribute("MessageCreate", "Cập nhật thành công");
        return "redirect:/admin/typedoctor";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            try {

                restTemplate.delete(apiUrl + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/typedoctor";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            try {

                restTemplate.delete(apiUrl + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/typedoctor";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            try {

                restTemplate.delete(apiUrl + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/typedoctor";
        } else {
            return "redirect:/login";
        }

    }
}
