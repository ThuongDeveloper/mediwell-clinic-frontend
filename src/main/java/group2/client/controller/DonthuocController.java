package group2.client.controller;

import group2.client.dto.HoaDonThuocDAO;
import group2.client.dto.ListHoaDonThuocDAO;
import group2.client.entities.Casher;
import group2.client.entities.Donthuoc;
import group2.client.entities.Thuoc;
import group2.client.entities.Typethuoc;
import group2.client.repository.ThuocRepository;
import group2.client.service.AuthService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/admin/donthuoc")
public class DonthuocController {

    private String apiUrl_Thuoc = "http://localhost:8888/api/thuoc/";
    private String apiUrl_Donthuoc = "http://localhost:8888/api/donthuoc/";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ThuocRepository thuocRepository;

    @Autowired
    private AuthService authService;

    @RequestMapping("")
    public String page(Model model, HttpServletRequest request) {
        Casher currentCasher = authService.isAuthenticatedCasher(request);
        ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Donthuoc>>() {
        });

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Donthuoc> listDonthuoc = response.getBody();

            // Xử lý dữ liệu theo nhu cầu của bạn
            model.addAttribute("currentCasher", currentCasher);
            model.addAttribute("listDonthuoc", listDonthuoc);
        }
        return "admin/donthuoc/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, Thuoc thuoc, HttpServletRequest request) {
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        // Lấy List Type Donthuoc
        ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Donthuoc>>() {
        });
        List<Donthuoc> listDonthuoc = response.getBody();
        model.addAttribute("listDonthuoc", listDonthuoc);
        model.addAttribute("currentCasher", currentCasher);
        model.addAttribute("donthuoc", new Donthuoc());
        return "admin/donthuoc/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
<<<<<<< HEAD
    public String create(Model model, int[] thuocID, int[] price, int[] quantity, HttpSession session, String name, String phone, HttpServletRequest requestCurent) {
=======
    public String create( Model model, int[] thuocID, int[] price, int[] quantity, HttpSession session, String name, String phone, HttpServletRequest requestCurent) {
>>>>>>> b07e42e6f6ea862391a0a0f7e0aa14d7405208e4

        String hienloiQuantity = "";

        Casher currentCasher = authService.isAuthenticatedCasher(requestCurent);

        ResponseEntity<List<Thuoc>> response1 = restTemplate.exchange(apiUrl_Thuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Thuoc>>() {
        });
<<<<<<< HEAD
=======
    
//    public String create(Model model, int[] thuocID, int[] price, int[] quantity, HttpSession session, String name,
//            String phone) {
//
//        String hienloiQuantity = "";
//
//        ResponseEntity<List<Thuoc>> response1 = restTemplate.exchange(apiUrl_Thuoc, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<Thuoc>>() {
//                });
>>>>>>> b07e42e6f6ea862391a0a0f7e0aa14d7405208e4
        var flagQuantity = true;

        List<Thuoc> listThuoc = response1.getBody();

        for (int i = 0; i < thuocID.length; i++) {
            for (var item : listThuoc) {
                if (item.getId() == thuocID[i]) {
                    if (item.getQuantity() < quantity[i]) {
                        flagQuantity = false;
                        hienloiQuantity += "Insufficient quantity of medicine: " + item.getName() + " ";
                        break;
                    }
                }
            }
<<<<<<< HEAD
        }

=======

        }


        

>>>>>>> b07e42e6f6ea862391a0a0f7e0aa14d7405208e4
        if (flagQuantity == false) {
            session.setAttribute("error", hienloiQuantity);
            return "admin/donthuoc/create";
        }

        List<HoaDonThuocDAO> list = new ArrayList<HoaDonThuocDAO>();
        for (int i = 0; i < thuocID.length; i++) {
            HoaDonThuocDAO obj = new HoaDonThuocDAO(thuocID[i], price[i], quantity[i]);
            list.add(obj);
        }
        ListHoaDonThuocDAO listHDTD = new ListHoaDonThuocDAO();
        listHDTD.setListHDT(list);
        listHDTD.setName(name);
        listHDTD.setPhone(phone);
<<<<<<< HEAD
        listHDTD.setCasherId(currentCasher);

=======

        listHDTD.setCasherId(currentCasher);

//        if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
//            donthuoc.setCasherId(currentCasher);
//        }


>>>>>>> b07e42e6f6ea862391a0a0f7e0aa14d7405208e4
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<ListHoaDonThuocDAO> request = new HttpEntity<>(listHDTD, headers);

        ResponseEntity<ListHoaDonThuocDAO> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.POST, request,
                ListHoaDonThuocDAO.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Sau khi lưu đơn thuốc
// Lặp qua danh sách các thuốc trong đơn thuốc
            for (int i = 0; i < thuocID.length; i++) {
                for (var item : listThuoc) {
                    if (item.getId() == thuocID[i]) {
                        if (item.getQuantity() >= quantity[i]) {
                            // Trừ số lượng thuốc từ số lượng hiện có
                            int newQuantity = item.getQuantity() - quantity[i];
                            item.setQuantity(newQuantity);
                            // Cập nhật lại thông tin thuốc trong cơ sở dữ liệu
                            thuocRepository.save(item);
                        }
                    }
                }
            }

            return "redirect:/admin/donthuoc";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "redirect:/admin/donthuoc";
        }
<<<<<<< HEAD
=======

>>>>>>> b07e42e6f6ea862391a0a0f7e0aa14d7405208e4
    }

}
