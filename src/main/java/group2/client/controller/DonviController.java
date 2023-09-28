/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.*;
import group2.client.service.AuthService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author dochi
 */
@Controller
@RequestMapping("/admin/donvi")
public class DonviController {

    private String apiUrl = "http://localhost:8888/api/donvi/";

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
            ResponseEntity<List<Donvi>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Donvi> listDonvi = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDonvi", listDonvi);
                model.addAttribute("currentAdmin", currentAdmin);
            }

            //Kiểm tra các thông báo
            if(MessageCreate != null){
                model.addAttribute("messageCreate", MessageCreate);
            }
            return "admin/donvi/index";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Donvi>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Donvi> listDonvi = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDonvi", listDonvi);
                model.addAttribute("currentDoctor", currentDoctor);
            }

            //Kiểm tra các thông báo
            if(MessageCreate != null){
                model.addAttribute("messageCreate", MessageCreate);
            }
            return "admin/donvi/index";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Donvi>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Donvi>>() {
                    });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Donvi> listDonvi = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDonvi", listDonvi);
                model.addAttribute("currentCasher", currentCasher);
            }

            //Kiểm tra các thông báo
            if(MessageCreate != null){
                model.addAttribute("messageCreate", MessageCreate);
            }
            return "admin/donvi/index";
        }else {
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
            model.addAttribute("donvi", new Donvi());
            model.addAttribute("currentAdmin", currentAdmin);

            return "admin/donvi/create";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            model.addAttribute("donvi", new Donvi());
            model.addAttribute("currentDoctor", currentDoctor);

            return "admin/donvi/create";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            model.addAttribute("donvi", new Donvi());
            model.addAttribute("currentCasher", currentCasher);


            return "admin/donvi/create";
        }else {
            return "redirect:/login";
        }


    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, Donvi donvi, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            // Send a POST request to the API to create a new medicine type
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl + "/create", donvi, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                // Creation was successful
                session.setAttribute("msg", "Create successful");
//                redirectAttributes.addFlashAttribute("MessageCreate", "Creation successful");
                return "redirect:/admin/donvi"; // Redirect to index page
            } else {
                // Server error from the API
                session.setAttribute("msg", "Server error when creating Unit.");
//                redirectAttributes.addFlashAttribute("MessageCreate", "Server error when creating Unit.");
            }
        } catch (HttpClientErrorException e) {
            // The API returned a bad request (400) status
            String responseBody = e.getResponseBodyAsString();
            if ("Name already exists in the database.".equals(responseBody)) {
                session.setAttribute("msg", "Unit already exists in the database.");
//                redirectAttributes.addFlashAttribute("MessageCreateError", "Unit already exists in the database.");
            } else {
                session.setAttribute("msg", "Undefined error when creating Unit.");
//                redirectAttributes.addFlashAttribute("MessageCreateError", "Undefined error when creating Unit.");
            }
        }

        return "redirect:/admin/donvi/create";
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
            ResponseEntity<Donvi> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", Donvi.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Donvi donvi = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("donvi", donvi);
                model.addAttribute("currentAdmin", currentAdmin);

                return "admin/donvi/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/donvi";
            }
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<Donvi> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", Donvi.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Donvi donvi = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("donvi", donvi);
                model.addAttribute("currentDoctor", currentDoctor);

                return "admin/donvi/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/donvi";
            }
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<Donvi> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", Donvi.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Donvi donvi = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("donvi", donvi);
                model.addAttribute("currentCasher", currentCasher);

                return "admin/donvi/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/donvi";
            }
        }else {
            return "redirect:/login";
        }


    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String update(Model model, @PathVariable("id") Integer id, Donvi updatedDonvi, HttpSession session, RedirectAttributes redirectAttributes) {
        // Gửi yêu cầu API để cập nhật thông tin TypeDoctor trong cơ sở dữ liệu

        restTemplate.put(apiUrl + "/edit", updatedDonvi);
        // Chú ý rằng, phương thức put trả về void (không có phản hồi từ server)
        session.setAttribute("msg", "Update successful");
        // Điều hướng về trang danh sách TypeDoctor với thông báo thành công
//        redirectAttributes.addFlashAttribute("MessageCreate", "Update successful");
        return "redirect:/admin/donvi";
    }



    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Integer id,RedirectAttributes redirectAttributes, HttpSession session, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            try {

                restTemplate.delete(apiUrl+"/delete/"+id);
                session.setAttribute("msg", "Delete successful");
                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Delete successful");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                session.setAttribute("msg", "Delete fail");
                redirectAttributes.addFlashAttribute("MessageCreate", "Delete fail");
            }

            return "redirect:/admin/donvi";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            try {

                restTemplate.delete(apiUrl+"/delete/"+id);
                session.setAttribute("msg", "Delete successful");
                // Nếu không có lỗi, tức là xóa thành công
//                redirectAttributes.addFlashAttribute("MessageCreate", "Delete successful");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                session.setAttribute("msg", "Delete fail");
//                redirectAttributes.addFlashAttribute("MessageCreate", "Delete fail");
            }

            return "redirect:/admin/donvi";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            try {

                restTemplate.delete(apiUrl+"/delete/"+id);
                session.setAttribute("msg", "Delete successful");
                // Nếu không có lỗi, tức là xóa thành công
//                redirectAttributes.addFlashAttribute("MessageCreate", "Delete successful");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                session.setAttribute("msg", "Delete fail");
//                redirectAttributes.addFlashAttribute("MessageCreate", "Delete fail");
            }

            return "redirect:/admin/donvi";
        }else {
            return "redirect:/login";
        }

    }


}
