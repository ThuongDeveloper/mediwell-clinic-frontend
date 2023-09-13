package group2.client.controller.Client;

import group2.client.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;

@Controller
public class ForgotPasswordController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/client/forgotPassword")
    public String showForgotPasswordForm() {
        return "client/forgotPassword/index"; // Trả về trang HTML cho form "Quên mật khẩu"
    }

    @PostMapping("/client/forgotPassword/sendResetEmail")
    public ModelAndView sendResetEmail(@RequestParam("email") String email) {
        // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu hay không
        // Nếu không tồn tại, bạn có thể hiển thị thông báo lỗi
        // Nếu tồn tại, tiếp tục xử lý

        String newPassword = generateRandomPassword();

        // Cập nhật mật khẩu mới cho người dùng trong cơ sở dữ liệu
        boolean passwordUpdated = patientService.updatePasswordByEmail(email, newPassword);

        if (passwordUpdated) {
            // Gửi email với mật khẩu mới
            boolean emailSent = sendEmail(email, newPassword);

            if (emailSent) {
                ModelAndView modelAndView = new ModelAndView("client/forgotPassword/confirm");
                modelAndView.addObject("message", "Mật khẩu mới đã được gửi đến email của bạn.");
                return modelAndView;
            } else {
                // Xử lý lỗi gửi email
                ModelAndView modelAndView = new ModelAndView("client/forgotPassword/error");
                modelAndView.addObject("message", "Đã xảy ra lỗi khi gửi email.");
                return modelAndView;
            }
        } else {
            // Xử lý lỗi cập nhật mật khẩu
            ModelAndView modelAndView = new ModelAndView("client/forgotPassword/error");
            modelAndView.addObject("message", "Không thể cập nhật mật khẩu. Vui lòng thử lại sau.");
            return modelAndView;
        }
    }

    private String generateRandomPassword() {
        // Hãy viết mã để tạo mật khẩu ngẫu nhiên ở đây
        // Ví dụ: sử dụng SecureRandom để tạo mật khẩu bảo mật
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder newPassword = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(characters.length());
            newPassword.append(characters.charAt(index));
        }

        return newPassword.toString();
    }

    private boolean sendEmail(String toEmail, String newPassword) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject("Mật khẩu mới của bạn");
            helper.setTo(toEmail);
            helper.setText("Mật khẩu mới của bạn là: " + newPassword);

            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            // Xử lý lỗi gửi email
            e.printStackTrace();
            return false;
        }
    }
}
