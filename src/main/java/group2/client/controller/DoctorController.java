/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import group2.client.entities.TypeDoctor;
import group2.client.service.AuthService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/admin/doctor")
public class DoctorController {

    private String apiUrl_Doctor = "http://localhost:8888/api/doctor/";
    private String apiUrl_TypeDoctor = "http://localhost:8888/api/typedoctor/";
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
            ResponseEntity<List<Doctor>> response = restTemplate.exchange(apiUrl_Doctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Doctor> listDoctor = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "admin/doctor/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Doctor>> response = restTemplate.exchange(apiUrl_Doctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Doctor> listDoctor = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "admin/doctor/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Doctor>> response = restTemplate.exchange(apiUrl_Doctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Doctor> listDoctor = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "admin/doctor/index";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, Doctor Doctor, HttpServletRequest request) {

        
        
        
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            //Lấy List Type Doctor
            ResponseEntity<List<TypeDoctor>> response = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = response.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentAdmin", currentAdmin);

            model.addAttribute("Doctor", new Doctor());
            return "admin/doctor/create";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            //Lấy List Type Doctor
            ResponseEntity<List<TypeDoctor>> response = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = response.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentDoctor", currentDoctor);

            model.addAttribute("Doctor", new Doctor());
            return "admin/doctor/create";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            //Lấy List Type Doctor
            ResponseEntity<List<TypeDoctor>> response = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = response.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentCasher", currentCasher);

            model.addAttribute("Doctor", new Doctor());
            return "admin/doctor/create";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, Doctor Doctor, @RequestParam("typeDoctorID") String typeDoctorID, @RequestParam("file") MultipartFile file,HttpSession session) throws IOException {

        
        
        String fileName = file.getOriginalFilename();
        //Set các giá trị còn thiếu
        TypeDoctor newTD = new TypeDoctor();
        newTD.setId(Integer.parseInt(typeDoctorID));

        Doctor.setCreateAt(new Date());
        Doctor.setTypeDoctorId(newTD);

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("Doctor", Doctor);
        body.add("file", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        var response = restTemplate.exchange(apiUrl_Doctor + "/create",HttpMethod.POST, requestEntity, String.class );

        if (response.getStatusCode() == HttpStatus.OK) {
            if(response.getBody().equals("EmailTonTai")){
                        session.setAttribute("notifyDoctor", "Email already exists !!"); 

           return "redirect:/admin/doctor/create";
           
            }else{
                  session.setAttribute("notifyDoctor", "Create new account successful !!");
            }
          
        } else if(response.getStatusCode() == HttpStatus.NOT_FOUND){
            System.out.println("Kết quả là False");
        }

        //Lấy List Type Doctor
        model.addAttribute("Doctor", new Doctor());
        return "redirect:/admin/doctor";
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
            ResponseEntity<List<TypeDoctor>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = responseTypeDoctor.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentAdmin", currentAdmin);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                Doctor objDoctor = response.getBody();

            // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
//                model.addAttribute("objDoctor", objDoctor);
            //Lấy One Doctor theo ID
            ResponseEntity<Doctor> response = restTemplate.getForEntity(apiUrl_Doctor + "/edit/{id}", Doctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Doctor objDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objDoctor", objDoctor);

                return "admin/doctor/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/doctor";
            }
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            //Lấy List Type Doctor
            ResponseEntity<List<TypeDoctor>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = responseTypeDoctor.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentDoctor", currentDoctor);

            //Lấy One Doctor theo ID
            ResponseEntity<Doctor> response = restTemplate.getForEntity(apiUrl_Doctor + "/edit/{id}", Doctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Doctor objDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objDoctor", objDoctor);

                return "admin/doctor/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/doctor";
            }
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            //Lấy List Type Doctor
            ResponseEntity<List<TypeDoctor>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = responseTypeDoctor.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentCasher", currentCasher);

            //Lấy One Doctor theo ID
            ResponseEntity<Doctor> response = restTemplate.getForEntity(apiUrl_Doctor + "/edit/{id}", Doctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Doctor objDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objDoctor", objDoctor);

                return "admin/doctor/edit";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/doctor";
            }
        } else {
            return "redirect:/login";

        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String update(Model model,HttpSession session, @RequestParam("file") MultipartFile file, Doctor objDoctor, @RequestParam String typeDoctorID, RedirectAttributes redirectAttributes) throws IOException {

        
        
        String fileName = file.getOriginalFilename();
        TypeDoctor newTD = new TypeDoctor();
        newTD.setId(Integer.parseInt(typeDoctorID));
        objDoctor.setTypeDoctorId(newTD);
        if(fileName =="" || fileName == null){
                HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
           HttpEntity<Doctor> request = new HttpEntity<>(objDoctor, headers);
                    ResponseEntity<Doctor> response = restTemplate.exchange(apiUrl_Doctor + "/editnotanh", HttpMethod.POST, request, Doctor.class);

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
             session.setAttribute("notifyDoctor", "Update successfully !!!"); 
                   return "redirect:/admin/doctor";
            }
      
             session.setAttribute("notifyDoctorError", "Can't Update"); 
                   return "redirect:/admin/doctor";
            
   
   
      

    
        }else{
             ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("Doctor", objDoctor);
        body.add("file", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        var response = restTemplate.exchange(apiUrl_Doctor + "/edit",HttpMethod.POST, requestEntity, String.class );

//aaaaa
        }
//aaaaa
   





            // Chú ý rằng, phương thức put trả về void (không có phản hồi từ server)

            // Điều hướng về trang danh sách TypeDoctor với thông báo thành công
                    session.setAttribute("notifyDoctor", "Update successfully !!!"); 
        redirectAttributes.addFlashAttribute("MessageCreate", "Update Success !");
        return "redirect:/admin/doctor";
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

                restTemplate.delete(apiUrl_Doctor + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/doctor";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            try {

                restTemplate.delete(apiUrl_Doctor + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/doctor";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            try {

                restTemplate.delete(apiUrl_Doctor + "/delete/" + id);

                // Nếu không có lỗi, tức là xóa thành công
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thành công");
            } catch (Exception e) {
                // Xử lý lỗi nếu có
                redirectAttributes.addFlashAttribute("MessageCreate", "Xóa thất bại");
            }

            return "redirect:/admin/doctor";
        } else {
            return "redirect:/login";
        }
    }

     @RequestMapping(value = "/changepass/{id}", method = RequestMethod.GET)
    public String changepass(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            //Lấy List Type Doctor
            ResponseEntity<List<TypeDoctor>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = responseTypeDoctor.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentAdmin", currentAdmin);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                Doctor objDoctor = response.getBody();

            // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
//                model.addAttribute("objDoctor", objDoctor);
            //Lấy One Doctor theo ID
            ResponseEntity<Doctor> response = restTemplate.getForEntity(apiUrl_Doctor + "/edit/{id}", Doctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Doctor objDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objDoctor", objDoctor);

                return "admin/doctor/changepassword";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/doctor";
            }
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            //Lấy List Type Doctor
            ResponseEntity<List<TypeDoctor>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = responseTypeDoctor.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentDoctor", currentDoctor);

            //Lấy One Doctor theo ID
            ResponseEntity<Doctor> response = restTemplate.getForEntity(apiUrl_Doctor + "/edit/{id}", Doctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Doctor objDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objDoctor", objDoctor);

                return "admin/doctor/changepassword";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/doctor";
            }
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            //Lấy List Type Doctor
            ResponseEntity<List<TypeDoctor>> responseTypeDoctor = restTemplate.exchange(apiUrl_TypeDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<TypeDoctor>>() {
            });
            List<TypeDoctor> listTypeDoctor = responseTypeDoctor.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("currentCasher", currentCasher);

            //Lấy One Doctor theo ID
            ResponseEntity<Doctor> response = restTemplate.getForEntity(apiUrl_Doctor + "/edit/{id}", Doctor.class, id);

            if (response.getStatusCode().is2xxSuccessful()) {
                Doctor objDoctor = response.getBody();

                // Truyền thông tin TypeDoctor vào model để hiển thị trên trang edit.html
                model.addAttribute("objDoctor", objDoctor);

                return "admin/doctor/changepassword";
            } else {
                // Xử lý khi không tìm thấy TypeDoctor cần chỉnh sửa
                return "redirect:/admin/doctor";
            }
        } else {
            return "redirect:/login";

        }
    }

@RequestMapping(value = "/changepass", method = RequestMethod.POST)
    public String changepass(Model model,HttpSession session, Doctor objDoctor) throws IOException {

        
        
       
                HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
           HttpEntity<Doctor> request = new HttpEntity<>(objDoctor, headers);
                    ResponseEntity<Doctor> response = restTemplate.exchange(apiUrl_Doctor + "/editnotanh", HttpMethod.POST, request, Doctor.class);

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
             session.setAttribute("notifyDoctor", "Update password successfully !!!"); 
                   return "redirect:/admin/doctor";
            }
      
             session.setAttribute("notifyDoctorError", "Can't Update"); 
                   return "redirect:/admin/doctor";
            
   
   
      

    
        
       
    }
}
