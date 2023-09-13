package group2.client.controller.Client;

import group2.client.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            // Nếu email và số điện thoại khớp, thì gửi yêu cầu đặt lại mật khẩu và xử lý phản hồi từ API
            ResponseEntity<String> response = restTemplate.postForEntity(passwordResetApiUrl, null, String.class);
            
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
