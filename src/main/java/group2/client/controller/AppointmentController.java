/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Admin;
import group2.client.entities.Appointment;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Lichlamviec;
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
 * @author hokim
 */
@Controller
public class AppointmentController {

    String apiUrl = "http://localhost:8888/api/appointment/";
    private String apiUrlDoctor = "http://localhost:8888/api/doctor/";
    private String apiUrlPatient = "http://localhost:8888/api/patient/";
    RestTemplate restTemplate = new RestTemplate();
    
     @Autowired
    private AuthService authService;

    @RequestMapping("/admin/appointment")
    public String page(Model model, HttpServletRequest request) {
        
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
         if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            ResponseEntity<List<Appointment>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Appointment>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Appointment> listAppointment = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listAppointment", listAppointment);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "/admin/appointment/index";
        }else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Appointment>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Appointment>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Appointment> listAppointment = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listAppointment", listAppointment);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "/admin/appointment/index";
        }else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
             ResponseEntity<List<Appointment>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Appointment>>() {
            });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Appointment> listAppointment = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listAppointment", listAppointment);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "/admin/appointment/index";
        }else {
            return "redirect:/login";
        }
        
        
    }

    @RequestMapping("/admin/appointment/create")
    public String create(Model model) {
        ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
        });
        
         ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Patient>>() {
        });
        
        if (DResponse.getStatusCode().is2xxSuccessful() && patientResponse.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
             List<Patient> listPatient = patientResponse.getBody();
            model.addAttribute("listPatient", listPatient);
            List<Doctor> listDoctor = DResponse.getBody();
            model.addAttribute("listDoctor", listDoctor);
            // Chuyển hướng về trang danh sách Casher
            
        } 
        // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
        model.addAttribute("appointment", new Appointment());
        return "/admin/appointment/create";
    }

    @RequestMapping(value = "/admin/appointment/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Appointment appointment, @RequestParam("doctor") String doctorID, @RequestParam("patient") String patientID, @RequestParam("time_select") String timeSelect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        
        Doctor newDoctor = new Doctor();
        newDoctor.setId(Integer.parseInt(doctorID));
        appointment.setDoctorId(newDoctor);
        
        Patient newPatient = new Patient();
        newPatient.setId(Integer.parseInt(patientID));
        appointment.setPatientId(newPatient);
        
         if(timeSelect.equals("7:00 - 12:00")){
            appointment.setStarttime("7:00");
            appointment.setEndtime("12:00");
        }
        if(timeSelect.equals("12:00 - 17:00")){
            appointment.setStarttime("12:00");
            appointment.setEndtime("17:00");
        }
        if(timeSelect.equals("17:00 - 21:00")){
            appointment.setStarttime("17:00");
            appointment.setEndtime("21:00");
        }

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Appointment> request = new HttpEntity<>(appointment, headers);

        ResponseEntity<Appointment> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Appointment.class);
       
        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
            // Chuyển hướng về trang danh sách Casher
             model.addAttribute("appointment", new Appointment());
            return "redirect:/admin/appointment";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/appointment/create";
        }
    }

    @RequestMapping(value = "/admin/appointment/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id) {
        Appointment appointment = restTemplate.getForObject(apiUrl + "/" + id, Appointment.class);
//        model.addAttribute("lich", lich);
         ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
        });
     

        if (DResponse.getStatusCode().is2xxSuccessful()) {
            
            List<Doctor> listDoctor = DResponse.getBody();
            model.addAttribute("listDoctor", listDoctor);
            model.addAttribute("appointment", appointment);
            return "/admin/appointment/edit";
        } else {
            return "redirect:/admin/appointment";
        }
    }

    @RequestMapping(value = "/admin/appointment/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Appointment updatedAppointment, @RequestParam("DoctorID") String doctorID, @RequestParam("time_select") String timeSelect) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
////
////        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
//        Appointment existingAppointment = restTemplate.getForObject(apiUrl + "/" + updatedAppointment.getId(), Appointment.class);
////
        Doctor newD = new Doctor();
        newD.setId(Integer.parseInt(doctorID));
        updatedAppointment.setDoctorId(newD);
        
        
        
        if(timeSelect.equals("7:00 - 12:00")){
            updatedAppointment.setStarttime("7:00");
            updatedAppointment.setEndtime("12:00");
        }
        if(timeSelect.equals("12:00 - 17:00")){
            updatedAppointment.setStarttime("12:00");
            updatedAppointment.setEndtime("17:00");
        }
        if(timeSelect.equals("17:00 - 21:00")){
            updatedAppointment.setStarttime("17:00");
            updatedAppointment.setEndtime("21:00");
        }

////        
//        // Bổ sung id vào URL khi thực hiện PUT
//        String url = apiUrl + "/" + existingAppointment.getId();
//
//        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
//        HttpEntity<Appointment> request = new HttpEntity<>(existingAppointment, headers);
//
//        try {
//            restTemplate.exchange(url, HttpMethod.PUT, request, Appointment.class);
//            return "redirect:/admin/appointment";
//
//        } catch (RestClientException e) {
//            model.addAttribute("appointment", existingAppointment);
//            return "/admin/appointment/edit";
//        }
            restTemplate.put(apiUrl + updatedAppointment.getId(), updatedAppointment);
            return "redirect:/admin/appointment";
    }

    @RequestMapping(value = "/admin/appointment/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id) {
        restTemplate.delete(apiUrl + "/" + id);
        // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

        // Chuyển hướng về trang danh sách Casher
        return "redirect:/admin/appointment";
    }

   


}