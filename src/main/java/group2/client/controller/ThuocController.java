/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.dto.ThuocDAO;
import group2.client.entities.Thuoc;
import group2.client.entities.Typethuoc;
import java.util.Date;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author dochi
 */
@Controller
public class ThuocController {

    private String apiUrl = "http://localhost:8888/thuoc/";
    private String apiTypethuoc = "http://localhost:8888/typethuoc/";
    private String serverBaseUrl = "http://localhost:8888";
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/admin/thuoc")
    public String page(Model model) {
        ResponseEntity<List<Thuoc>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Thuoc>>() {
        });

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Thuoc> listThuoc = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listthuoc", listThuoc);
        }

        // Lấy thuốc đã tạo thành công từ redirectAttributes và thêm vào model để hiển thị trong view (nếu có)
        if (model.containsAttribute("createdThuoc")) {
            Thuoc createdThuoc = (Thuoc) model.asMap().get("createdThuoc");
            model.addAttribute("createdThuoc", createdThuoc);
        }

        return "admin/thuoc/index";
    }

    @GetMapping("/admin/create-thuoc")
    public String showCreateThuocForm(Model model) {
        model.addAttribute("thuoc", new Thuoc());
        ResponseEntity<List<Typethuoc>> response = restTemplate.exchange(apiTypethuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Typethuoc>>() {
        });
        List<Typethuoc> listTypeThuoc = response.getBody();
        // Xử lý dữ liệu theo nhu cầu của bạn
        model.addAttribute("listTypethuoc", listTypeThuoc);
        return "admin/thuoc/create";
    }


    @PostMapping("/admin/create-thuoc")
    public String createThuoc(@ModelAttribute ThuocDAO thuoc,@RequestParam("typethuocId") int typethuocId, RedirectAttributes redirectAttributes) {
      
//        Typethuoc typethuoc = new Typethuoc();
//        typethuoc.setId(typethuocId);
        thuoc.setTypethuocId(typethuocId);
   
        ResponseEntity<ThuocDAO> response = restTemplate.postForEntity(apiUrl +"create", thuoc, ThuocDAO.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
//            // Lấy "Thuoc" đã tạo thành công từ phản hồi
//            Thuoc createdThuoc = response.getBody();    

            // Thêm "Thuoc" vào model để hiển thị trong view hoặc thực hiện các hành động khác
//            redirectAttributes.addFlashAttribute("createdThuoc", createdThuoc);

            // Chuyển hướng tới trang hiển thị thông tin "Thuoc" (ví dụ: /thuoc/{id})
            return "admin/thuoc/index" ;
        } else {
            // Xử lý lỗi tạo "Thuoc" (ví dụ: hiển thị trang lỗi)
            return "redirect:/admin/thuoc";
        }
    }

    @PostMapping("/admin/delete-thuoc/{id}")
    public String deleteThuoc(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.delete(apiUrl + "/" + id);

            redirectAttributes.addFlashAttribute("successMessage", "Xóa thuoc thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa thuoc.");
        }

        // Chuyển hướng tới trang index sau khi xóa thành công hoặc không thành công
        return "redirect:/admin/thuoc";
    }

}
