package group2.client.controller.Client;

import group2.client.dto.ListToaThuocDAO;
import group2.client.dto.RatingDAO;
import group2.client.entities.Appointment;
import group2.client.entities.Doctor;
import group2.client.entities.Patient;
import group2.client.entities.Rating;
import group2.client.repository.DoctorRepository;
import group2.client.repository.PatientRepository;
import group2.client.service.AuthService;
import group2.client.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class RatingDoctorController {
    private String apiUrl = "http://localhost:8888/api/rating/";
    String apiUrlAppointment = "http://localhost:8888/api/appointment/";
    private String apiUrlDoctor = "http://localhost:8888/api/doctor/";
    private String apiUrlPatient = "http://localhost:8888/api/patient";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    //Validate comment tục tiểu
    private boolean containsProfanity(String comment) {
        String[] profanityWords = {
                // Danh sách các từ tục tiểu ở đây
                "buồi", "buoi", "dau buoi", "daubuoi", "caidaubuoi", "nhucaidaubuoi", "dau boi", "bòi", "dauboi", "caidauboi", "đầu bòy", "đầu bùi", "dau boy", "dauboy", "caidauboy",
                "cặc", "cak", "kak", "kac", "cac", "concak", "nungcak", "bucak", "caiconcac", "caiconcak", "cu", "cặk", "cak", "dái", "giái", "zái", "kiu",
                "cứt", "cuccut", "cutcut", "cứk", "cuk", "cười ỉa", "cười ẻ",
                "đéo", "đếch", "đếk", "dek", "đết", "đệt", "đách", "dech", "đ'", "deo", "d'", "đel", "đél", "del", "dell ngửi", "dell ngui", "dell chịu", "dell chiu", "dell hiểu", "dell hieu", "dellhieukieugi", "dell nói", "dell noi", "dellnoinhieu", "dell biết", "dell biet", "dell nghe", "dell ăn", "dell an", "dell được", "dell duoc", "dell làm", "dell lam", "dell đi", "dell di", "dell chạy", "dell chay", "deohieukieugi",
                "địt", "đm", "dm", "đmm", "dmm", "đmmm", "dmmm", "đmmmm", "dmmmm", "đmmmmm", "dmmmmm", "đcm", "dcm", "đcmm", "dcmm", "đcmmm", "dcmmm", "đcmmmm", "dcmmmm", "đệch", "đệt", "dit", "dis", "diz", "đjt", "djt", "địt mẹ", "địt mịe", "địt má", "địt mía", "địt ba", "địt bà", "địt cha", "địt con", "địt bố", "địt cụ", "dis me", "disme", "dismje", "dismia", "dis mia", "dis mie", "đis mịa", "đis mịe", "ditmemayconcho", "ditmemay", "ditmethangoccho", "ditmeconcho", "dmconcho", "dmcs", "ditmecondi", "ditmecondicho",
                "đù", "đút đít", "chổng mông", "banh háng", "xéo háng", "xhct", "xephinh", "la liếm", "đổ vỏ", "xoạc", "xoac", "chich choac", "húp sò",
                "chịch", "vãi", "v~", "đụ", "nứng", "nug", "đút đít", "chổng mông", "banh háng", "xéo háng", "xhct", "xephinh", "la liếm", "đổ vỏ", "xoạc", "xoac", "chich choac", "húp sò", "fuck", "fck", "đụ", "bỏ bú", "buscu",
                "ngu", "óc chó", "occho", "lao cho", "láo chó", "bố láo", "chó má", "cờ hó", "sảng", "thằng chó", "thang cho'", "thang cho", "chó điên", "thằng điên", "thang dien", "đồ điên", "sủa bậy", "sủa tiếp", "sủa đi", "sủa càn",
                "mẹ bà","tiên sư","con bà mày","con mẹ mày","má mày","mẹ mày", "mẹ cha mày", "me cha may", "mẹ cha anh", "mẹ cha nhà anh", "mẹ cha nhà mày", "me cha nha may", "mả cha mày", "mả cha nhà mày", "ma cha may", "ma cha nha may", "mả mẹ", "mả cha", "kệ mẹ", "kệ mịe", "kệ mịa", "kệ mje", "kệ mja", "ke me", "ke mie", "ke mia", "ke mja", "ke mje", "bỏ mẹ", "bỏ mịa", "bỏ mịe", "bỏ mja", "bỏ mje", "bo me", "bo mia", "bo mie", "bo mje", "bo mja", "chetme", "chet me", "chết mẹ", "chết mịa", "chết mja", "chết mịe", "chết mie", "chet mia", "chet mie", "chet mja", "chet mje", "thấy mẹ", "thấy mịe", "thấy mịa", "thay me", "thay mie", "thay mia", "tổ cha", "bà cha mày", "cmn", "cmnl", "tiên sư nhà mày", "tiên sư bố", "tổ sư"
        };

        for (String word : profanityWords) {
            if (comment.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/rating/{patientID}")
    public String editProfile(Model model, @PathVariable("patientID") Integer patientID, HttpServletRequest request) {
        Patient currentPatient = authService.isAuthenticatedPatient(request);

        ResponseEntity<List<Appointment>> appointmentsResponse = restTemplate.exchange(apiUrlAppointment + "?patient_id=" + patientID, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Appointment>>() {});

        if (appointmentsResponse.getStatusCode().is2xxSuccessful()) {
            List<Appointment> appointments = appointmentsResponse.getBody();

            // Tạo danh sách các bác sĩ đã có cuộc hẹn với bệnh nhân
            List<Doctor> listDoctor = new ArrayList<>();

            for (Appointment appointment : appointments) {
                if (appointment.getPatientId().getId().equals(patientID)) {
                    // Lấy đối tượng Doctor từ cuộc hẹn và thêm vào danh sách
                    Doctor doctor = appointment.getDoctorId();
                    listDoctor.add(doctor);
                }
            }

            if (listDoctor.isEmpty()) {
                // Nếu không có cuộc hẹn, thêm thông báo vào model
                model.addAttribute("noAppointmentsMessage", "No appointments found for this patient.");
                model.addAttribute("currentPatient", currentPatient);
            } else {
                // Thêm danh sách bác sĩ vào model nếu có cuộc hẹn
                model.addAttribute("listDoctor", listDoctor);
            }
        }

        // Thêm thông tin bệnh nhân vào model
        model.addAttribute("patientProfile", currentPatient);
        model.addAttribute("patient", currentPatient);

        // Thêm đối tượng Rating vào model để người dùng có thể đánh giá
        model.addAttribute("rating", new Rating());

        return "client/rating/index";
    }

    @PostMapping("/rating/{patientId}")
    public String create(Model model, Double rating, String comment, int doctorId,
                         @PathVariable("patientId") int patientId, HttpServletRequest requestPatient, RedirectAttributes redirectAttributes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Patient currentPatient = authService.isAuthenticatedPatient(requestPatient);

        if (currentPatient == null) {
            // Xử lý lỗi nếu không có bệnh nhân đăng nhập
            model.addAttribute("error", "No authenticated patient.");
            return "client/rating/index";
        }

        // Kiểm tra nếu rating là null thì gán giá trị mặc định là 0
        if (rating == null) {
            rating = 0.0;
        }
        if (containsProfanity(comment)) {
            redirectAttributes.addFlashAttribute("error", "Comment contains profanity.");
            redirectAttributes.addFlashAttribute("patient", currentPatient);
            return "redirect:/rating/" + patientId;
        }
        RatingDAO ratingDAO = new RatingDAO();
        ratingDAO.setRating(rating);
        ratingDAO.setComment(comment);
        ratingDAO.setDoctor_id(doctorId);
        ratingDAO.setPatient_id(currentPatient.getId());

        HttpEntity<RatingDAO> requestEntity = new HttpEntity<>(ratingDAO, headers);

        try {
            ResponseEntity<RatingDAO> responseEntity = restTemplate.exchange(apiUrl + "createRating", HttpMethod.POST, requestEntity, RatingDAO.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                // Chuyển hướng về trang danh sách đánh giá cho bác sĩ
                model.addAttribute("rating", new Rating());
                redirectAttributes.addFlashAttribute("successMessage", "Rating created successfully."); // Thêm thông báo thành công
                return "redirect:/rating/" + patientId;
            } else {
                // Xử lý lỗi nếu cần thiết
                model.addAttribute("error", "Error creating rating.");
                return "client/rating/index";
            }
        } catch (Exception ex) {
            // Xử lý lỗi nếu có lỗi trong quá trình gửi yêu cầu
            model.addAttribute("error", "Error creating rating: " + ex.getMessage());
            return "client/rating/index";
        }
    }


}



