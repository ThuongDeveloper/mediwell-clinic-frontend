/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Taophieukham;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Ann
 */
@Controller
public class TaophieukhamController {
    
    private String apiUrl = "http://localhost:8888/api/taophieukham";

    RestTemplate restTemplate = new RestTemplate();

    
    @RequestMapping("/admin/phieukham")
    public String page(Model model) {
        ResponseEntity<List<Taophieukham>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Taophieukham>>() {});
              
        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Taophieukham> listTaophieukham = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listTaophieukham", listTaophieukham);
        }
        return "admin/phieukham/index";
    }
    
}
