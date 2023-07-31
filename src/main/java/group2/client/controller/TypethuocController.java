/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Thuoc;
import group2.client.entities.Typethuoc;
import java.util.List;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author dochi
 */
@Controller
public class TypethuocController {

    private String apiUrl = "http://localhost:8888/typethuoc/";

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/admin/typethuoc")
    public String page(Model model) {
        ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Typethuoc>>() {
        });

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Typethuoc> listTypethuoc = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listTypethuoc", listTypethuoc);
        }
        return "admin/typethuoc/index";
    }

    @GetMapping("/admin/create-typethuoc")
    public String showCreateTypeThuocForm(Model model) {
        model.addAttribute("typethuoc", new Typethuoc());

        return "admin/typethuoc/create";
    }

    @PostMapping("/admin/create-typethuoc")
    public String createTypeThuoc(@ModelAttribute Typethuoc typethuoc, Model model, RedirectAttributes redirectAttributes) {
        // Gửi yêu cầu POST tới server để tạo thuốc mới
        ResponseEntity<Typethuoc> response = restTemplate.postForEntity(apiUrl, typethuoc, Typethuoc.class);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            // Lấy thuốc đã tạo thành công từ response
            Typethuoc createdTypethuoc = response.getBody();

            // Thêm thuốc vào model để hiển thị trong view
            redirectAttributes.addFlashAttribute("createdTypethuoc", createdTypethuoc);

            // Chuyển hướng tới trang page
            return "redirect:/admin/typethuoc";
        } else {
            // Xử lý lỗi tạo thuốc
            return "error";
        }
    }

    @GetMapping("/admin/edit-typethuoc/{id}")
    public String editTypeThuocPage(@PathVariable Integer id, Model model) {
        // Gọi API để lấy thông tin của Typethuoc theo ID
        ResponseEntity<Typethuoc> response = restTemplate.exchange(apiUrl + "/" + id, HttpMethod.GET, null, Typethuoc.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            Typethuoc typethuoc = response.getBody();
            model.addAttribute("typethuoc", typethuoc);
            return "admin/typethuoc/edit";
        } else {

            return "admin/typethuoc/edit";
        }

    }

    // Hàm để xử lý yêu cầu cập nhật thông tin Typethuoc
    @PostMapping("/admin/edit-typethuoc/{id}")
    public ResponseEntity<Typethuoc> editTypeThuoc(Model model, @PathVariable Integer id, @ModelAttribute Typethuoc typethuoc1, RedirectAttributes redirectAttributes) {
        // Thiết lập ID cho đối tượng typethuoc
        typethuoc1.setId(id);

        // Gửi yêu cầu PUT tới server để cập nhật thông tin Typethuoc
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Typethuoc> request = new HttpEntity<>(typethuoc1, headers);
        ResponseEntity<Typethuoc> response = restTemplate.exchange(apiUrl + "/" + id, HttpMethod.PUT, request, Typethuoc.class);
        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật Typethuoc thành công!");
            return ResponseEntity.status(HttpStatus.SEE_OTHER).header(HttpHeaders.LOCATION, "/admin/typethuoc").body(response.getBody());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật Typethuoc.");
            return response;
        }

    }

    @PostMapping("/admin/delete-typethuoc/{id}")
    public String deleteTypeThuoc(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Gọi API để xóa Typethuoc với id được truyền vào
            restTemplate.delete(apiUrl + "/" + id);

            // Thông báo xóa thành công
            redirectAttributes.addFlashAttribute("successMessage", "Xóa Typethuoc thành công!");
        } catch (Exception e) {
            // Xử lý lỗi xóa Typethuoc
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa Typethuoc.");
        }

        // Chuyển hướng tới trang index sau khi xóa thành công hoặc không thành công
        return "redirect:/admin/typethuoc";
    }

}
