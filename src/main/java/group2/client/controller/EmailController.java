package group2.client.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/email")
public class EmailController {
    @Autowired
    private JavaMailSender javaMailSender;

    @RequestMapping("")
    public String showForm() {
        return "admin/email/index";
    }

    @PostMapping("/send")
    public String sendMail(@RequestParam(value = "to") String to,
                           @RequestParam(value = "subject") String subject,
                           @RequestParam(value = "content") String content) {

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("nova1x1996@gmail.com");
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(content);

            javaMailSender.send(msg);
            // Gửi email thành công
            return "admin/email/noti";
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi khi gửi email
            return "admin/email/index";
        }


    }
}
