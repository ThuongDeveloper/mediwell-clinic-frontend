package group2.client.controller;


import group2.client.entities.Donthuoc;
import group2.client.entities.Thuoc;
import group2.client.entities.Typethuoc;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/admin/donthuoc")
public class DonthuocController {

    private String apiUrl_Donthuoc = "http://localhost:8888/api/donthuoc/";
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("")
    public String page(Model model) {
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
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, Thuoc thuoc) {

        //Lấy List Type Donthuoc
        ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Donthuoc>>() {
                });
        List<Donthuoc> listDonthuoc = response.getBody();
        model.addAttribute("listDonthuoc", listDonthuoc);

        model.addAttribute("donthuoc", new Donthuoc());
        return "admin/donthuoc/create";
    }
}
