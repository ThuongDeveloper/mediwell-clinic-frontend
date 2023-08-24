/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Lichlamviec;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author hokim
 */
@Controller
public class LichlamviecController {

String apiUrl = "http://localhost:8888/api/lichlamviec/";
private String apiUrlDoctor = "http://localhost:8888/api/doctor/";
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/admin/lichlamviec")
    public String page(Model model) {
        ResponseEntity<List<Lichlamviec>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Lichlamviec>>() {
        });

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Lichlamviec> listLich = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listLich", listLich);
        }
        return "/admin/lichlamviec/index";
    }

    @RequestMapping("/admin/lichlamviec/create")
    public String create(Model model) {
        ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
        });
        
        if (DResponse.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
            List<Doctor> listDoctor = DResponse.getBody();
            model.addAttribute("listDoctor", listDoctor);
            // Chuyển hướng về trang danh sách Casher
            
        } 
        // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
        model.addAttribute("lich", new Lichlamviec());
        return "/admin/lichlamviec/create";
    }

    @RequestMapping(value = "/admin/lichlamviec/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Lichlamviec lich, @RequestParam("doctor") String doctorID, @RequestParam("time_select") String timeSelect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        
        Doctor newDoctor = new Doctor();
        newDoctor.setId(Integer.parseInt(doctorID));
        lich.setDoctorId(newDoctor);
        
        if(timeSelect.equals("7:00 - 12:00")){
            lich.setStarttime("7:00");
            lich.setEndtime("12:00");
        }
        if(timeSelect.equals("12:00 - 17:00")){
            lich.setStarttime("12:00");
            lich.setEndtime("17:00");
        }
        if(timeSelect.equals("17:00 - 21:00")){
            lich.setStarttime("17:00");
            lich.setEndtime("21:00");
        }

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Lichlamviec> request = new HttpEntity<>(lich, headers);

        ResponseEntity<Lichlamviec> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Lichlamviec.class);
       
        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
            // Chuyển hướng về trang danh sách Casher
             model.addAttribute("lich", new Lichlamviec());
            return "redirect:/admin/lichlamviec";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/lichlamviec/create";
        }
    }

    @RequestMapping(value = "/admin/lichlamviec/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id) {
        Lichlamviec lich = restTemplate.getForObject(apiUrl + "/" + id, Lichlamviec.class);
//        model.addAttribute("lich", lich);
         ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
        });

        if (DResponse.getStatusCode().is2xxSuccessful()) {
            
            List<Doctor> listDoctor = DResponse.getBody();
            model.addAttribute("listDoctor", listDoctor);
            model.addAttribute("lich", lich);
            return "/admin/lichlamviec/edit";
        } else {
            return "redirect:/admin/lichlamviec";
        }
    }

    @RequestMapping(value = "/admin/lichlamviec/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Lichlamviec updatedLich, @RequestParam("DoctorID") String doctorID, @RequestParam("time_select") String timeSelect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Lichlamviec existingLich = restTemplate.getForObject(apiUrl + "/" + updatedLich.getId(), Lichlamviec.class);

        Doctor newD = new Doctor();
        newD.setId(Integer.parseInt(doctorID));
        updatedLich.setDoctorId(newD);
        
         if(timeSelect.equals("7:00 - 12:00")){
            updatedLich.setStarttime("7:00");
            updatedLich.setEndtime("12:00");
        }
        if(timeSelect.equals("12:00 - 17:00")){
            updatedLich.setStarttime("12:00");
            updatedLich.setEndtime("17:00");
        }
        if(timeSelect.equals("17:00 - 21:00")){
            updatedLich.setStarttime("17:00");
            updatedLich.setEndtime("21:00");
        }
        
        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "/" + updatedLich.getId();

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<Lichlamviec> request = new HttpEntity<>(updatedLich, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, Lichlamviec.class);
            return "redirect:/admin/lichlamviec";

        } catch (RestClientException e) {
            model.addAttribute("lich", existingLich);
            return "/admin/lichlamviec/edit";
        }
    }

    @RequestMapping(value = "/admin/lichlamviec/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id) {
        restTemplate.delete(apiUrl + "/" + id);
        // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

        // Chuyển hướng về trang danh sách Casher
        return "redirect:/admin/lichlamviec";
    }

   


}

