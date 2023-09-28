/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.*;
import group2.client.service.AuthService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private String apiUrl_Company = "http://localhost:8888/api/company/";
    private String apiUrl_Donvi = "http://localhost:8888/api/donvi/";
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
            //Lấy List Donvi
            ResponseEntity<List<Donvi>> response1 = restTemplate.exchange(apiUrl_Donvi, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });
            ResponseEntity<List<Company>> response2 = restTemplate.exchange(apiUrl_Company, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Company>>() {
                    });
            List<Company> listCompany = response2.getBody();
            model.addAttribute("listCompany", listCompany);
            List<Donvi> listDonvi = response1.getBody();
            List<Typethuoc> listTypeThuoc = response.getBody();
            model.addAttribute("listDonvi", listDonvi);
            model.addAttribute("listTypeThuoc", listTypeThuoc);
            model.addAttribute("currentAdmin", currentAdmin);
            model.addAttribute("thuoc", new Thuoc());
            return "admin/thuoc/create";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            //Lấy List Type Doctor
            ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl_TypeThuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Typethuoc>>() {
            });
            ResponseEntity<List<Company>> response2 = restTemplate.exchange(apiUrl_Company, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Company>>() {
                    });
            //Lấy List Donvi
            ResponseEntity<List<Donvi>> response1 = restTemplate.exchange(apiUrl_Donvi, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });

            List<Company> listCompany = response2.getBody();
            List<Typethuoc> listTypeThuoc = response.getBody();
            List<Donvi> listDonvi = response1.getBody();
            model.addAttribute("listCompany", listCompany);
            model.addAttribute("listDonvi", listDonvi);
            model.addAttribute("listTypeThuoc", listTypeThuoc);
            model.addAttribute("currentDoctor", currentDoctor);
            model.addAttribute("thuoc", new Thuoc());
            return "admin/thuoc/create";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            //Lấy List Type Doctor
            ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl_TypeThuoc, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Typethuoc>>() {
            });
            //Lấy List Donvi
            ResponseEntity<List<Donvi>> response1 = restTemplate.exchange(apiUrl_Donvi, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });
            ResponseEntity<List<Company>> response2 = restTemplate.exchange(apiUrl_Company, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Company>>() {
                    });
            List<Company> listCompany = response2.getBody();
            model.addAttribute("listCompany", listCompany);
            List<Donvi> listDonvi = response1.getBody();
            List<Typethuoc> listTypeThuoc = response.getBody();
            model.addAttribute("listDonvi", listDonvi);
            model.addAttribute("listTypeThuoc", listTypeThuoc);
            model.addAttribute("currentCasher", currentCasher);
            model.addAttribute("thuoc", new Thuoc());
            return "admin/thuoc/create";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            Model model,
            Thuoc thuoc,
            @RequestParam("typeThuocID") String typethuocID,
            @RequestParam("donviID") String donviID,
            @RequestParam("companyID") String companyID,
            HttpSession session
    ) {
        // Set các giá trị còn thiếu
        Typethuoc newTT = new Typethuoc();
        newTT.setId(Integer.parseInt(typethuocID));

        Company newCpn = new Company();
        newCpn.setId(Integer.parseInt(companyID));

        Donvi newDv = new Donvi();
        newDv.setId(Integer.parseInt(donviID));

        thuoc.setCreateAt(new Date());
        thuoc.setTypethuocId(newTT);
        thuoc.setCompanyId(newCpn);
        thuoc.setDonviId(newDv);
        var a = thuoc.getTypethuocId().getId();
        var response = restTemplate.postForObject(apiUrl_Thuoc + "/create", thuoc, Boolean.class);
        session.setAttribute("msg", "Create successful!");
//        if (response) {
//            System.out.println("Kết quả là True");
//        } else {
//            System.out.println("Kết quả là False");
//        }

        // Lấy List Type Doctor
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
            //Lấy List Donvi
            ResponseEntity<List<Donvi>> response1 = restTemplate.exchange(apiUrl_Donvi, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });
            List<Donvi> listDonvi = response1.getBody();
            model.addAttribute("listDonvi", listDonvi);
            ResponseEntity<List<Company>> response2 = restTemplate.exchange(apiUrl_Company, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Company>>() {
                    });
            List<Company> listCompany = response2.getBody();
            model.addAttribute("listCompany", listCompany);
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
            //Lấy List Donvi
            ResponseEntity<List<Donvi>> response1 = restTemplate.exchange(apiUrl_Donvi, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });
            List<Donvi> listDonvi = response1.getBody();
            ResponseEntity<List<Company>> response2 = restTemplate.exchange(apiUrl_Company, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Company>>() {
                    });
            List<Company> listCompany = response2.getBody();
            model.addAttribute("listCompany", listCompany);
            model.addAttribute("listDonvi", listDonvi);
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
            //Lấy List Donvi
            ResponseEntity<List<Donvi>> response1 = restTemplate.exchange(apiUrl_Donvi, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });
            List<Donvi> listDonvi = response1.getBody();
            model.addAttribute("listDonvi", listDonvi);
            ResponseEntity<List<Company>> response2 = restTemplate.exchange(apiUrl_Company, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Company>>() {
                    });
            List<Company> listCompany = response2.getBody();
            model.addAttribute("listCompany", listCompany);
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

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String update(
            Model model,
            @PathVariable("id") Integer id,
            Thuoc objThuoc,
            @RequestParam String typeThuocID,
            @RequestParam String donviID,
            @RequestParam String companyID,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        // Tạo một đối tượng Typethuoc mới dựa trên typeThuocID được truyền từ form
        Typethuoc newTD = new Typethuoc();
        newTD.setId(Integer.parseInt(typeThuocID));
        objThuoc.setTypethuocId(newTD);
        // Don vi
        Donvi newDv = new Donvi();
        newDv.setId(Integer.parseInt(donviID));
        objThuoc.setDonviId(newDv);
        // Company
        Company newCpn = new Company();
        newCpn.setId(Integer.parseInt(companyID));
        objThuoc.setCompanyId(newCpn);
        // Sử dụng RestTemplate để gửi yêu cầu PUT đến API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Thuoc> requestEntity = new HttpEntity<>(objThuoc, headers);

        ResponseEntity<Thuoc> response = restTemplate.exchange(
                apiUrl_Thuoc + "/edit/",  // Địa chỉ URL cần thêm id
                HttpMethod.PUT,
                requestEntity,
                Thuoc.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            // Nếu cập nhật thành công, thêm thông báo thành công vào flash attribute
            session.setAttribute("msg", "Update successful!");
//            redirectAttributes.addFlashAttribute("MessageCreate", "Update successful");
        } else {
            // Nếu không thành công, thêm thông báo lỗi vào flash attribute
//            redirectAttributes.addFlashAttribute("MessageCreate", "Update failed");
            session.setAttribute("msg", "Update fail!");
        }

        // Điều hướng về trang danh sách thuốc
        return "redirect:/admin/thuoc";
    }



    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Integer id, HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            try {

                restTemplate.delete(apiUrl_Thuoc + "/delete/" + id);
                session.setAttribute("msg", "Delete Successful!");
                // Nếu không có lỗi, tức là xóa thành công
//                redirectAttributes.addFlashAttribute("MessageCreate", "Delete Successful");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
//                redirectAttributes.addFlashAttribute("MessageError", "Delete fail.");
                session.setAttribute("msg", "Delete fail!");
            }

            return "redirect:/admin/thuoc";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            try {

                restTemplate.delete(apiUrl_Thuoc + "/delete/" + id);
                 session.setAttribute("msg", "Delete Successful!");
                // Nếu không có lỗi, tức là xóa thành công
//                redirectAttributes.addFlashAttribute("MessageCreate", "Delete Successful");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
//                redirectAttributes.addFlashAttribute("MessageError", "Delete fail.");
                 session.setAttribute("msg", "Delete fail!");
            }

            return "redirect:/admin/thuoc";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            try {

                restTemplate.delete(apiUrl_Thuoc + "/delete/" + id);
                session.setAttribute("msg", "Delete Successful!");
                // Nếu không có lỗi, tức là xóa thành công
//                redirectAttributes.addFlashAttribute("MessageCreate", "Delete Successful");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                session.setAttribute("msg", "Delete fail!");
//                redirectAttributes.addFlashAttribute("MessageError", "Delete fail.");
            }

            return "redirect:/admin/thuoc";
        } else {
            return "redirect:/login";
        }

    }

}
