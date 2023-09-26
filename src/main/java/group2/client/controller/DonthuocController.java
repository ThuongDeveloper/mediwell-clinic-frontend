package group2.client.controller;

import group2.client.dto.HoaDonThuocDAO;
import group2.client.dto.ListHoaDonThuocDAO;
import group2.client.entities.Casher;
import group2.client.entities.Donthuoc;
import group2.client.entities.DonthuocDetails;
import group2.client.entities.Taophieukham;
import group2.client.entities.Thuoc;
import group2.client.entities.Typethuoc;
import group2.client.repository.ThuocRepository;
import group2.client.service.AuthService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/donthuoc")
public class DonthuocController {

    private String apiUrl = "http://localhost:8888/api/taophieukham/";
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


    @RequestMapping(value = "/create/{id}", method = RequestMethod.GET)
    public String create(Model model, Thuoc thuoc, @PathVariable("id") int id, HttpServletRequest request) {
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        // Lấy List Type Donthuoc
        ResponseEntity<List<Donthuoc>> response = restTemplate.exchange(apiUrl_Donthuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Donthuoc>>() {
        });
        Taophieukham taophieukham = restTemplate.getForObject(apiUrl + "/" + id, Taophieukham.class);

        List<Donthuoc> listDonthuoc = response.getBody();
        model.addAttribute("taophieukham", taophieukham);
        model.addAttribute("listDonthuoc", listDonthuoc);
        model.addAttribute("currentCasher", currentCasher);
        model.addAttribute("donthuoc", new Donthuoc());
        return "admin/donthuoc/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Taophieukham taophieukham, int[] thuocID, int[] price, int[] quantity, HttpSession session, HttpServletRequest requestCurent) {

        String hienloiQuantity = "";

        Casher currentCasher = authService.isAuthenticatedCasher(requestCurent);

        ResponseEntity<List<Thuoc>> response1 = restTemplate.exchange(apiUrl_Thuoc, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Thuoc>>() {
        });
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
        }
        if (flagQuantity == false) {
            session.setAttribute("error", hienloiQuantity);
            return "admin/donthuoc/create";
        }

        List<HoaDonThuocDAO> list = new ArrayList<HoaDonThuocDAO>();
        for (int i = 0; i < thuocID.length; i++) {
            HoaDonThuocDAO obj = new HoaDonThuocDAO(thuocID[i], price[i], quantity[i]);
            list.add(obj);
        }

        Taophieukham existTPK = restTemplate.getForObject(apiUrl + "/" + taophieukham.getId(), Taophieukham.class);

        ListHoaDonThuocDAO listHDTD = new ListHoaDonThuocDAO();
        listHDTD.setListHDT(list);
        listHDTD.setName(existTPK.getName());
        listHDTD.setPhone(existTPK.getPhone());
        listHDTD.setCasherId(currentCasher);

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
    }

    @RequestMapping(value = "/export-pdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportToPDF(@RequestParam("donthuocId") int donthuocId) {

        if (donthuocId <= 0) {
            return ResponseEntity.badRequest().body("Invalid donthuocId".getBytes());
        }

        // Gọi API từ máy chủ backend để lấy tài liệu PDF
        ResponseEntity<byte[]> response = restTemplate.exchange(apiUrl_Donthuoc + "/export-pdf?donthuocId=" + donthuocId, HttpMethod.GET, null, byte[].class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            byte[] pdfBytes = response.getBody();

            // Thiết lập header và trả về file PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "prescription.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } else {
            // Xử lý lỗi nếu không thể lấy tài liệu PDF từ API
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] createPhieukhamPDF(Donthuoc donthuoc) throws IOException {
        // Tạo một tài liệu PDF mới
        PDDocument document = new PDDocument();

        // Tạo một trang mới cho tài liệu
        PDPage page = new PDPage();
        document.addPage(page);

        // Tạo một luồng nội dung cho trang
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Thực hiện viết nội dung PDF ở đây (ví dụ: ghi thông tin từ `toathuoc` vào tài liệu)
        // Đóng luồng nội dung
        contentStream.close();

        // Lưu trang và đóng tài liệu
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

}
