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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/admin/typethuoc")
public class TypethuocController {

    private String apiUrl = "http://localhost:8888/api/typethuoc/";

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
             ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Typethuoc>>() {
                });

                // Kiểm tra mã trạng thái của phản hồi
                if (response.getStatusCode().is2xxSuccessful()) {
                    List<Typethuoc> listTypeThuoc = response.getBody();

                    // Xử lý dữ liệu theo nhu cầu của bạn
                    model.addAttribute("listTypeThuoc", listTypeThuoc);
                    model.addAttribute("currentAdmin", currentAdmin);
                }

                //Kiểm tra các thông báo 
                if(MessageCreate != null){
                       model.addAttribute("messageCreate", MessageCreate);
                }
                return "admin/typethuoc/index";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
             ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Typethuoc>>() {
                });

                // Kiểm tra mã trạng thái của phản hồi
                if (response.getStatusCode().is2xxSuccessful()) {
                    List<Typethuoc> listTypeThuoc = response.getBody();

                    // Xử lý dữ liệu theo nhu cầu của bạn
                    model.addAttribute("listTypeThuoc", listTypeThuoc);
                    model.addAttribute("currentDoctor", currentDoctor);
                }

                //Kiểm tra các thông báo 
                if(MessageCreate != null){
                       model.addAttribute("messageCreate", MessageCreate);
                }
                return "admin/typethuoc/index";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
             ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Typethuoc>>() {
                });

                // Kiểm tra mã trạng thái của phản hồi
                if (response.getStatusCode().is2xxSuccessful()) {
                    List<Typethuoc> listTypeThuoc = response.getBody();

                    // Xử lý dữ liệu theo nhu cầu của bạn
                    model.addAttribute("listTypeThuoc", listTypeThuoc);
                    model.addAttribute("currentCasher", currentCasher);
                }

                //Kiểm tra các thông báo 
                if(MessageCreate != null){
                       model.addAttribute("messageCreate", MessageCreate);
                }
                return "admin/typethuoc/index";
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
             model.addAttribute("typethuoc", new Typethuoc());
             model.addAttribute("currentAdmin", currentAdmin);

            return "admin/typethuoc/create";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
              model.addAttribute("typethuoc", new Typethuoc());
             model.addAttribute("currentDoctor", currentDoctor);

            return "admin/typethuoc/create";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
             model.addAttribute("typethuoc", new Typethuoc());
             model.addAttribute("currentCasher", currentCasher);


             return "admin/typethuoc/create";
        }else {
            return "redirect:/login";
        }

       
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, Typethuoc typethuoc, RedirectAttributes redirectAttributes) {
        try {
            // Send a POST request to the API to create a new medicine type
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl + "/create", typethuoc, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                // Creation was successful
                redirectAttributes.addFlashAttribute("MessageCreate", "Creation successful");
                return "redirect:/admin/typethuoc"; // Redirect to index page
            } else {
                // Server error from the API
                redirectAttributes.addFlashAttribute("MessageCreate", "Server error when creating medicine type.");
            }
        } catch (HttpClientErrorException e) {
            // The API returned a bad request (400) status
            String responseBody = e.getResponseBodyAsString();
            if ("Name already exists in the database.".equals(responseBody)) {
                redirectAttributes.addFlashAttribute("MessageCreateError", "Name already exists in the database.");
            } else {
                redirectAttributes.addFlashAttribute("MessageCreateError", "Undefined error when creating medicine type.");
            }
        }

        return "redirect:/admin/typethuoc/create";
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
            ResponseEntity<Typethuoc> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", Typethuoc.class, id);
       
            if (response.getStatusCode().is2xxSuccessful()) {
                Typethuoc typethuoc = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("typethuoc", typethuoc);
                model.addAttribute("currentAdmin", currentAdmin);

                return "admin/typethuoc/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/typethuoc";
            }
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<Typethuoc> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", Typethuoc.class, id);
       
            if (response.getStatusCode().is2xxSuccessful()) {
                Typethuoc typethuoc = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("typethuoc", typethuoc);
                model.addAttribute("currentDoctor", currentDoctor);

                return "admin/typethuoc/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/typethuoc";
            }
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<Typethuoc> response = restTemplate.getForEntity(apiUrl + "/edit/{id}", Typethuoc.class, id);
       
            if (response.getStatusCode().is2xxSuccessful()) {
                Typethuoc typethuoc = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("typethuoc", typethuoc);
                model.addAttribute("currentCasher", currentCasher);

                return "admin/typethuoc/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/typethuoc";
            }
        }else {
            return "redirect:/login";
        }

          
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String update(Model model, @PathVariable("id") Integer id, Typethuoc updatedTypethuoc, RedirectAttributes redirectAttributes) {
        // Gửi yêu cầu API để cập nhật thông tin TypeDoctor trong cơ sở dữ liệu
       
         restTemplate.put(apiUrl + "/edit", updatedTypethuoc);
        // Chú ý rằng, phương thức put trả về void (không có phản hồi từ server)

        // Điều hướng về trang danh sách TypeDoctor với thông báo thành công
        redirectAttributes.addFlashAttribute("MessageCreate", "Cập nhật thành công");
        return "redirect:/admin/typethuoc";
    }
    
    
    
       @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Integer id,RedirectAttributes redirectAttributes, HttpServletRequest request) {

         Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
                try {
      
                    restTemplate.delete(apiUrl+"/delete/"+id);

                    // Nếu không có lỗi, tức là xóa thành công
                    redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
                } catch (Exception e) {
                     // Xử lý lỗi nếu có
                  redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
                }

              return "redirect:/admin/typethuoc";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
             try {
      
                    restTemplate.delete(apiUrl+"/delete/"+id);

                    // Nếu không có lỗi, tức là xóa thành công
                    redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
                } catch (Exception e) {
                     // Xử lý lỗi nếu có
                  redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
                }

              return "redirect:/admin/typethuoc";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            try {
      
                    restTemplate.delete(apiUrl+"/delete/"+id);

                    // Nếu không có lỗi, tức là xóa thành công
                    redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
                } catch (Exception e) {
                     // Xử lý lỗi nếu có
                  redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
                }

              return "redirect:/admin/typethuoc";
        }else {
            return "redirect:/login";
        }
 
    }


}
