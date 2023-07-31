/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Casher;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Ann
 */
@Controller
public class CasherController {

    String apiUrl = "http://localhost:8888/api/casher";
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/admin/casher")
    public String page(Model model) {
        ResponseEntity<List<Casher>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Casher>>() {
        });

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Casher> listCasher = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listCasher", listCasher);
        }
        return "/admin/casher/index";
    }

    @RequestMapping("/admin/casher/create")
    public String create(Model model) {
        // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
        model.addAttribute("casher", new Casher());
        return "/admin/casher/create";
    }

    @RequestMapping(value = "/admin/casher/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Casher casher) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Casher> request = new HttpEntity<>(casher, headers);

        ResponseEntity<Casher> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Casher.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/casher";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/casher/create";
        }
    }

    @RequestMapping(value = "/admin/casher/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id) {
        Casher casher = restTemplate.getForObject(apiUrl + "/" + id, Casher.class);
        model.addAttribute("casher", casher);
        return "/admin/casher/edit";
    }

    @RequestMapping(value = "/admin/casher/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Casher updatedCasher) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Casher existingCasher = restTemplate.getForObject(apiUrl + "/" + updatedCasher.getId(), Casher.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "/" + updatedCasher.getId();

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<Casher> request = new HttpEntity<>(updatedCasher, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, Casher.class);
            return "redirect:/admin/casher";

        } catch (RestClientException e) {
            model.addAttribute("casher", existingCasher);
            return "/admin/casher/edit";
        }
    }

    @RequestMapping(value = "/admin/casher/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id) {
        restTemplate.delete(apiUrl + "/" + id);
        // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

        // Chuyển hướng về trang danh sách Casher
        return "redirect:/admin/casher";
    }
}
