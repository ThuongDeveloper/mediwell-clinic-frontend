/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Ann
 */
@Controller
public class CasherController {
    
    private String apiUrl = "http://localhost:8888/api/casher";

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/admin/casher")
    public String page(Model model) {
        ResponseEntity<List<Casher>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Casher>>() {});
              
        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Casher> listCasher = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listCasher", listCasher);
        }
        return "admin/casher/index";
    }
    
}
