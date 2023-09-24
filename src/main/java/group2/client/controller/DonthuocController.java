package group2.client.controller;


import group2.client.dto.HoaDonThuocDAO;
import group2.client.dto.ListHoaDonThuocDAO;
import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.Donthuoc;
import group2.client.entities.Patient;
import group2.client.entities.Thuoc;
import group2.client.entities.Typethuoc;
import group2.client.service.AuthService;
import java.util.ArrayList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Controller
@RequestMapping("/admin/donthuoc")
public class DonthuocController {

    private String apiUrl_Donthuoc = "http://localhost:8888/api/donthuoc/";
    RestTemplate restTemplate = new RestTemplate();
    
     @Autowired
    private AuthService authService;

    @RequestMapping("")
    public String page(Model model, HttpServletRequest request) {
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
        if(currentPatient != null && currentPatient.getRole().equals("PATIENT")){
            return "redirect:/forbien";
        }else if(currentAdmin != null && currentAdmin.getRole().equals("ADMIN")){
             ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Donthuoc>>() {
                });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Donthuoc> listDonthuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDonthuoc", listDonthuoc);
            }
            return "admin/donthuoc/index";
        }else if(currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")){
             ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Donthuoc>>() {
                });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Donthuoc> listDonthuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDonthuoc", listDonthuoc);
            }
            return "admin/donthuoc/index";
        }else if(currentCasher != null && currentCasher.getRole().equals("CASHER")){
             ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Donthuoc>>() {
                });

            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Donthuoc> listDonthuoc = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listDonthuoc", listDonthuoc);
            }
            return "admin/donthuoc/index";
        }else {
            return "redirect:/login";
        }
        
       
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, Thuoc thuoc, HttpServletRequest request) {
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        
        if(currentPatient != null && currentPatient.getRole().equals("PATIENT")){
            return "redirect:/forbien";
        }else if(currentAdmin != null && currentAdmin.getRole().equals("ADMIN")){
               //Lấy List Type Donthuoc
                ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Donthuoc>>() {
                        });
                List<Donthuoc> listDonthuoc = response.getBody();
                model.addAttribute("listDonthuoc", listDonthuoc);

                model.addAttribute("donthuoc", new Donthuoc());
                return "admin/donthuoc/create";
        }else if(currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")){
            //Lấy List Type Donthuoc
                ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Donthuoc>>() {
                        });
                List<Donthuoc> listDonthuoc = response.getBody();
                model.addAttribute("listDonthuoc", listDonthuoc);

                model.addAttribute("donthuoc", new Donthuoc());
                return "admin/donthuoc/create";
        }else if(currentCasher != null && currentCasher.getRole().equals("CASHER")){
            //Lấy List Type Donthuoc
                ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Donthuoc>>() {
                        });
                List<Donthuoc> listDonthuoc = response.getBody();
                model.addAttribute("listDonthuoc", listDonthuoc);

                model.addAttribute("donthuoc", new Donthuoc());
                return "admin/donthuoc/create";
        }else {
            return "redirect:/login";
        }

     
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model,int[] thuocID,int[] price,int[] quantity,String name,String phone) {
            List<HoaDonThuocDAO> list = new ArrayList<HoaDonThuocDAO>();
            for(int i = 0; i < thuocID.length;i++){
                HoaDonThuocDAO obj = new HoaDonThuocDAO(thuocID[i],price[i],quantity[i]);
                list.add(obj);
            }
            ListHoaDonThuocDAO listHDTD = new ListHoaDonThuocDAO();
            listHDTD.setListHDT(list);
            listHDTD.setName(name);
            listHDTD.setPhone(phone);
            
                HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<ListHoaDonThuocDAO> request = new HttpEntity<>(listHDTD, headers);

        ResponseEntity<ListHoaDonThuocDAO> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.POST, request, ListHoaDonThuocDAO.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/donthuoc";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "redirect:/admin/donthuoc";
        }      
    }
    
}
