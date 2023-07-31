/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Casher;
import group2.client.entities.Taophieukham;
import java.util.List;
import java.util.Objects;
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
public class TaophieukhamController {

    String apiUrl = "http://localhost:8888/api/taophieukham";
    String apiUrlCasher = "http://localhost:8888/api/casher";
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/admin/phieukham")
    public String page(Model model) {
        ResponseEntity<List<Taophieukham>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Taophieukham>>() {
        });

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Taophieukham> listTaophieukham = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listTaophieukham", listTaophieukham);
        }
        return "/admin/phieukham/index";
    }

    @RequestMapping("/admin/phieukham/create")
    public String create(Model model) {
        // Tạo một đối tượng Taophieukham trống để gửi thông tin tới form tạo mới
        model.addAttribute("taophieukham", new Taophieukham());
        return "/admin/phieukham/create";
    }

    @RequestMapping(value = "/admin/phieukham/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Taophieukham taophieukham) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Lấy danh sách phiếu khám hiện có từ API server
        ResponseEntity<List<Taophieukham>> responseList = restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Taophieukham>>() {
        });
        List<Taophieukham> existingTaophieukhams = responseList.getBody();

        // Tìm số thứ tự lớn nhất trong danh sách phiếu khám
        int maxSoThuTu = existingTaophieukhams.stream().mapToInt(Taophieukham::getSothutu).max().orElse(0);

        // Tăng số thứ tự lên một để tạo phiếu khám mới
        taophieukham.setSothutu(maxSoThuTu + 1);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Taophieukham> request = new HttpEntity<>(taophieukham, headers);

        ResponseEntity<Taophieukham> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Taophieukham.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Taophieukham thành công (nếu cần)

            // Chuyển hướng về trang danh sách Taophieukham
            return "redirect:/admin/phieukham";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/phieukham/create";
        }
    }

    @RequestMapping(value = "/admin/phieukham/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id) {
        Taophieukham taophieukham = restTemplate.getForObject(apiUrl + "/" + id, Taophieukham.class);
        model.addAttribute("taophieukham", taophieukham);
        return "/admin/phieukham/edit";
    }

    @RequestMapping(value = "/admin/phieukham/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Taophieukham updatedTaophieukham) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Taophieukham existingTaophieukham = restTemplate.getForObject(apiUrl + "/" + updatedTaophieukham.getId(), Taophieukham.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "/" + updatedTaophieukham.getId();

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<Taophieukham> request = new HttpEntity<>(updatedTaophieukham, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, Taophieukham.class);
            return "redirect:/admin/phieukham";

        } catch (RestClientException e) {
            model.addAttribute("taophieukham", existingTaophieukham);
            return "/admin/phieukham/edit";
        }
    }

    @RequestMapping(value = "/admin/phieukham/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        restTemplate.delete(apiUrl + "/" + id);
        // Thực hiện thêm xử lý sau khi xóa Taophieukham thành công (nếu cần)

        // Chuyển hướng về trang danh sách Taophieukham
        return "redirect:/admin/phieukham";
    }
}
