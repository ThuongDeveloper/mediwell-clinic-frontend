package group2.client.controller;

import group2.client.dto.HoaDonThuocDAO;
import group2.client.dto.ListToaThuocDAO;
import group2.client.dto.ToaThuocDAO;
import group2.client.entities.Thuoc;
import group2.client.entities.Toathuoc;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/toathuoc")
public class ToathuocController {
    private String apiUrl_Toathuoc = "http://localhost:8888/api/toathuoc/";
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("")
    public String page(Model model) {
        ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Toathuoc>>() {
                });

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Toathuoc> listToathuoc = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("listToathuoc", listToathuoc);
        }
        return "admin/toathuoc/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, Thuoc thuoc) {

        //Lấy List Type Donthuoc
        ResponseEntity<List<Toathuoc>> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Toathuoc>>() {
                });
        List<Toathuoc> listToathuoc = response.getBody();
        model.addAttribute("listToathuoc", listToathuoc);

        model.addAttribute("toathuoc", new Toathuoc());
        return "admin/toathuoc/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model,int[] thuocID,int[] quantity,int[] sang,int[] trua,int[] chieu,int[] toi,String name,String phone,String address, String sympton, String description,Boolean state) {
        List<ToaThuocDAO> list = new ArrayList<ToaThuocDAO>();
        for(int i = 0; i < thuocID.length;i++){
            ToaThuocDAO obj = new ToaThuocDAO(thuocID[i],quantity[i],sang[i],trua[i],chieu[i],toi[i]);
            list.add(obj);
        }
        if (state == null) {
            state = false;
        }
        ListToaThuocDAO listTT = new ListToaThuocDAO();
        listTT.setListTT(list);
        listTT.setName(name);
        listTT.setPhone(phone);
        listTT.setAddress(address);
        listTT.setDescription(description);
        listTT.setSymptom(sympton);
        listTT.setState(state);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<ListToaThuocDAO> request = new HttpEntity<>(listTT, headers);

        ResponseEntity<ListToaThuocDAO> response = restTemplate.exchange(apiUrl_Toathuoc, HttpMethod.POST, request, ListToaThuocDAO.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/toathuoc";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "redirect:/admin/toathuoc";
        }
    }
}
