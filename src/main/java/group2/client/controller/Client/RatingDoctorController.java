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

    private boolean containsProfanity(String comment) {
        // Thực hiện kiểm tra ở đây, ví dụ:
        String[] profanityWords = {"profanity1", "profanity2", "profanity3"};
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
                model.addAttribute("noAppointmentsMessage", "Không có cuộc hẹn nào cho bệnh nhân này.");
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
                         @PathVariable("patientId") int patientId, HttpServletRequest requestPatient) {
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
                model.addAttribute("successMessage", "Rating created successfully."); // Thêm thông báo thành công
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



