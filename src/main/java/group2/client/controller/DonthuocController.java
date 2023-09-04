package group2.client.controller;


import group2.client.dto.HoaDonThuocDAO;
import group2.client.dto.ListHoaDonThuocDAO;
import group2.client.entities.Donthuoc;
import group2.client.entities.Thuoc;
import group2.client.entities.Typethuoc;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model,int[] thuocID,int[] price,int[] quantity) {
            List<HoaDonThuocDAO> list = new ArrayList<HoaDonThuocDAO>();
            for(int i = 0; i < thuocID.length;i++){
                HoaDonThuocDAO obj = new HoaDonThuocDAO(thuocID[i],price[i],quantity[i]);
                list.add(obj);
            }
            ListHoaDonThuocDAO listHDTD = new ListHoaDonThuocDAO();
            listHDTD.setListHDT(list);
            listHDTD.setName("Trần Minh Khôi");
            
                HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<ListHoaDonThuocDAO> request = new HttpEntity<>(listHDTD, headers);

        ResponseEntity<ListHoaDonThuocDAO> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.POST, request, ListHoaDonThuocDAO.class);

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
    
}
