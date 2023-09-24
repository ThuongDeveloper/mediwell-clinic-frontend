package group2.client.controller.Client;

import group2.client.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class ForgotPasswordController {

      RestTemplate restTemplate = new RestTemplate();

      @Autowired
      private PatientService patientService;
   
    private String passwordResetApiUrl="http://localhost:8888/api/password-reset/request";

    @GetMapping("/client/forgotPassword")
    public String showForgotPasswordForm() {
        return "client/forgotPassword/index";
    }

    @PostMapping("/client/forgotPassword")
public String requestPasswordReset(@RequestParam("email") String email, 
                                   @RequestParam("phone") String phone, 
                                   Model model) {
    // Kiểm tra xem email và số điện thoại khớp với dữ liệu trong cơ sở dữ liệu
    boolean isMatched = patientService.isEmailAndPhoneMatch(email, phone);

    if (isMatched) {
        // Tạo một đối tượng để đại diện cho các tham số cần gửi đến API
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", email);
        params.add("phone", phone);

        // Tạo một đối tượng HttpHeaders để đặt tiêu đề yêu cầu nếu cần
        HttpHeaders headers = new HttpHeaders();
        // headers.add("header-name", "header-value"); // Thêm các tiêu đề nếu cần

        // Tạo đối tượng HttpEntity để đại diện cho yêu cầu
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // Gửi yêu cầu POST với thông tin email và phone
        ResponseEntity<String> response = restTemplate.exchange(passwordResetApiUrl, HttpMethod.POST, requestEntity, String.class);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("message", "Yêu cầu đặt lại mật khẩu đã được gửi đi.");
            return "client/forgotPassword/confirm";
        } else {
            model.addAttribute("message", "Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
        }
    } else {
        // Nếu email hoặc số điện thoại không khớp với dữ liệu trong cơ sở dữ liệu, hiển thị thông báo cho người dùng
        model.addAttribute("message", "Email hoặc số điện thoại không khớp với dữ liệu trong hệ thống.");
    }
    return "client/forgotPassword/index";
}
}
