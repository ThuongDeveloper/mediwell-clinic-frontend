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
import group2.client.entities.Typethuoc;
import group2.client.service.AuthService;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author dochi
 */
@Controller
@RequestMapping("/admin/thuoc")
public class ThuocController {

    private String apiUrl_Thuoc = "http://localhost:8888/api/thuoc/";
    private String apiUrl_TypeThuoc = "http://localhost:8888/api/typethuoc/";
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
            ResponseEntity<List<Thuoc>> response = restTemplate.exchange(apiUrl_Thuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Thuoc>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Thuoc> listThuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listThuoc", listThuoc);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "admin/thuoc/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Thuoc>> response = restTemplate.exchange(apiUrl_Thuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Thuoc>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Thuoc> listThuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listthuoc", listThuoc);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "admin/thuoc/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Thuoc>> response = restTemplate.exchange(apiUrl_Thuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Thuoc>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Thuoc> listThuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listthuoc", listThuoc);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "admin/thuoc/index";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, Thuoc thuoc, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            //Lấy List Type Doctor
            ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl_TypeThuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Typethuoc>>() {
            });
            List<Typethuoc> listTypeThuoc = response.getBody();
            model.addAttribute("listTypeThuoc", listTypeThuoc);
            model.addAttribute("currentAdmin", currentAdmin);
            model.addAttribute("thuoc", new Thuoc());
            return "admin/thuoc/create";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            //Lấy List Type Doctor
            ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl_TypeThuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Typethuoc>>() {
            });
            List<Typethuoc> listTypeThuoc = response.getBody();
            model.addAttribute("listTypeThuoc", listTypeThuoc);
            model.addAttribute("currentDoctor", currentDoctor);
            model.addAttribute("thuoc", new Thuoc());
            return "admin/thuoc/create";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            //Lấy List Type Doctor
            ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl_TypeThuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Typethuoc>>() {
            });
            List<Typethuoc> listTypeThuoc = response.getBody();
            model.addAttribute("listTypeThuoc", listTypeThuoc);
            model.addAttribute("currentCasher", currentCasher);
            model.addAttribute("thuoc", new Thuoc());
            return "admin/thuoc/create";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, Thuoc thuoc, @RequestParam("typeThuocID") String typethuocID) {

        //Set các giá trị còn thiếu
        Typethuoc newTT = new Typethuoc();
        newTT.setId(Integer.parseInt(typethuocID));

        thuoc.setCreateAt(new Date());
        thuoc.setTypethuocId(newTT);

        var a = thuoc.getTypethuocId().getId();
        var response = restTemplate.postForObject(apiUrl_Thuoc + "/create", thuoc, Boolean.class);

        if (response) {
            System.out.println("Kết quả là True");
        } else {
            System.out.println("Kết quả là False");
        }

        //Lấy List Type Doctor
        model.addAttribute("thuoc", new Thuoc());
        return "redirect:/admin/thuoc";
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
            //Lấy List Type Doctor
            ResponseEntity<List<Typethuoc>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeThuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Typethuoc>>() {
            });
            List<Typethuoc> listTypeThuoc = responseTypeDoctor.getBody();
            model.addAttribute("listTypeThuoc", listTypeThuoc);

            //Lấy One Doctor theo ID
            ResponseEntity<Thuoc> response = restTemplate.getForEntity(apiUrl_Thuoc + "/edit/{id}", Thuoc.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Thuoc objThuoc = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objThuoc", objThuoc);
                model.addAttribute("currentAdmin", currentAdmin);
                return "admin/thuoc/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/thuoc";
            }
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            //Lấy List Type Doctor
            ResponseEntity<List<Typethuoc>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeThuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Typethuoc>>() {
            });
            List<Typethuoc> listTypeThuoc = responseTypeDoctor.getBody();
            model.addAttribute("listTypeThuoc", listTypeThuoc);

            //Lấy One Doctor theo ID
            ResponseEntity<Thuoc> response = restTemplate.getForEntity(apiUrl_Thuoc + "/edit/{id}", Thuoc.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Thuoc objThuoc = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objThuoc", objThuoc);
                model.addAttribute("currentDoctor", currentDoctor);
                return "admin/thuoc/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/thuoc";
            }
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            //Lấy List Type Doctor
            ResponseEntity<List<Typethuoc>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeThuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Typethuoc>>() {
            });
            List<Typethuoc> listTypeThuoc = responseTypeDoctor.getBody();
            model.addAttribute("listTypeThuoc", listTypeThuoc);

            //Lấy One Doctor theo ID
            ResponseEntity<Thuoc> response = restTemplate.getForEntity(apiUrl_Thuoc + "/edit/{id}", Thuoc.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Thuoc objThuoc = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objThuoc", objThuoc);
                model.addAttribute("currentCasher", currentCasher);
                return "admin/thuoc/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/thuoc";
            }
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String update(Model model, Thuoc objThuoc, @RequestParam String typeThuocID, RedirectAttributes redirectAttributes) {

        Typethuoc newTD = new Typethuoc();
        newTD.setId(Integer.parseInt(typeThuocID));
        objThuoc.setTypethuocId(newTD);

        restTemplate.put(apiUrl_Thuoc + "/edit", objThuoc);
        // Chú ý rằng, phương thức put trả về void (không có phản hồi từ server)

        // Điều hướng về trang danh sách TypeDoctor với thông báo thành công
        redirectAttributes.addFlashAttribute("MessageCreate", "Cập nhật thành công");
        return "redirect:/admin/thuoc";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Integer id, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            try {

                restTemplate.delete(apiUrl_Thuoc + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/thuoc";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            try {

                restTemplate.delete(apiUrl_Thuoc + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/thuoc";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            try {

                restTemplate.delete(apiUrl_Thuoc + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/thuoc";
        } else {
            return "redirect:/login";
        }

    }

}
