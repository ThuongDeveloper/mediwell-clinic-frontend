/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Casher;
import group2.client.entities.Taophieukham;
import group2.client.entities.TypeDoctor;
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
@RequestMapping("/admin/phieukham")
public class TaophieukhamController {

    private String apiUrl = "http://localhost:8888/api/taophieukham/";
    private String apiUrlCasher = "http://localhost:8888/api/casher/";
    private String apiUrlTypeDoctor = "http://localhost:8888/api/typedoctor/";
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("")
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
        return "admin/phieukham/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        // Lấy danh sách Casher từ API server
        ResponseEntity<List<Casher>> casherResponse = restTemplate.exchange(apiUrlCasher, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Casher>>() {
        });
        ResponseEntity<List<TypeDoctor>> TDResponse = restTemplate.exchange(apiUrlTypeDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TypeDoctor>>() {
        });

        if (casherResponse.getStatusCode().is2xxSuccessful() && TDResponse.getStatusCode().is2xxSuccessful()) {
            List<Casher> listCasher = casherResponse.getBody();
            List<TypeDoctor> listTypeDoctor = TDResponse.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("listCasher", listCasher);
        }

        model.addAttribute("taophieukham", new Taophieukham());
        return "admin/phieukham/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, Taophieukham taophieukham, @RequestParam("casherID") String casherID, @RequestParam("typeDoctorID") String typeDoctorID) {
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

        Casher newCasher = new Casher();
        newCasher.setId(Integer.parseInt(casherID));
        taophieukham.setCasherId(newCasher);
        var a = taophieukham.getCasherId().getId();

        TypeDoctor newTD = new TypeDoctor();
        newTD.setId(Integer.parseInt(typeDoctorID));
        taophieukham.setTypeDoctorId(newTD);
        var b = taophieukham.getTypeDoctorId().getId();

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Taophieukham> request = new HttpEntity<>(taophieukham, headers);

        ResponseEntity<Taophieukham> response = restTemplate.exchange(apiUrl + "create", HttpMethod.POST, request, Taophieukham.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Chuyển hướng về trang danh sách Taophieukham
            model.addAttribute("taophieukham", new Taophieukham());
            return "redirect:/admin/phieukham";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/phieukham/create";
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") int id) {
        Taophieukham taophieukham = restTemplate.getForObject(apiUrl + "/" + id, Taophieukham.class);

        ResponseEntity<List<Casher>> casherResponse = restTemplate.exchange(apiUrlCasher, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Casher>>() {
        });
        ResponseEntity<List<TypeDoctor>> TDResponse = restTemplate.exchange(apiUrlTypeDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TypeDoctor>>() {
        });

        if (casherResponse.getStatusCode().is2xxSuccessful() && TDResponse.getStatusCode().is2xxSuccessful()) {
            List<Casher> listCasher = casherResponse.getBody();
            List<TypeDoctor> listTypeDoctor = TDResponse.getBody();
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("listCasher", listCasher);
            model.addAttribute("taophieukham", taophieukham);
            return "/admin/phieukham/edit";
        } else {
            return "redirect:/admin/phieukham";
        }

    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Taophieukham updatedTaophieukham, @RequestParam String casherID, @RequestParam String typeDoctorID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Lấy phiếu khám hiện có từ API server
        Taophieukham existingTaophieukham = restTemplate.getForObject(apiUrl + "/" + updatedTaophieukham.getId(), Taophieukham.class);

        TypeDoctor newTD = new TypeDoctor();
        newTD.setId(Integer.parseInt(typeDoctorID));
        existingTaophieukham.setTypeDoctorId(newTD);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "edit/" + updatedTaophieukham.getId(); // Đảm bảo URL đúng

        // Tạo một HttpEntity với thông tin phiếu khám cập nhật để gửi yêu cầu PUT
        HttpEntity<Taophieukham> request = new HttpEntity<>(updatedTaophieukham, headers);

        // Thực hiện PUT request để cập nhật phiếu khám
        restTemplate.exchange(url, HttpMethod.PUT, request, Taophieukham.class);

        // Sau khi cập nhật thành công, chuyển hướng về trang danh sách phiếu khám
        return "redirect:/admin/phieukham";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        restTemplate.delete(apiUrl + "delete/" + id);
        // Thực hiện thêm xử lý sau khi xóa Taophieukham thành công (nếu cần)

        // Chuyển hướng về trang danh sách Taophieukham
        return "redirect:/admin/phieukham";
    }
}
