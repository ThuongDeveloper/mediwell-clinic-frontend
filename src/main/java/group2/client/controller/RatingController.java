/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Rating;
import group2.client.entities.Patient;
import group2.client.entities.Doctor;
import group2.client.repository.AdminRepository;
import group2.client.repository.CasherRepository;
import group2.client.repository.DoctorRepository;
import group2.client.repository.PatientRepository;
import group2.client.service.AuthService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Ann
 */
@Controller
@RequestMapping("/admin/rating")
public class RatingController {

    private String apiUrl = "http://localhost:8888/api/rating/";
    private String apiUrlDoctor = "http://localhost:8888/api/doctor/";
    private String apiUrlPatient = "http://localhost:8888/api/patient/";
    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private AuthService authService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CasherRepository casherRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @RequestMapping("")
    public String page(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            ResponseEntity<List<Rating>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Rating>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Rating> listRating = response.getBody();
                
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listRating", listRating);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "admin/rating/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Rating>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Rating>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Rating> listRating = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listRating", listRating);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "admin/rating/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<Rating>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Rating>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Rating> listRating = response.getBody();

                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listRating", listRating);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "admin/rating/index";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            ResponseEntity<List<Doctor>> doctorResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            if (doctorResponse.getStatusCode().is2xxSuccessful() && patientResponse.getStatusCode().is2xxSuccessful()) {
                List<Doctor> listDoctor = doctorResponse.getBody();
                List<Patient> listPatient = patientResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentAdmin", currentAdmin);
            }

            model.addAttribute("rating", new Rating());
            return "admin/rating/create";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("DOCTOR")) {
            ResponseEntity<List<Doctor>> doctorResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            if (doctorResponse.getStatusCode().is2xxSuccessful() && patientResponse.getStatusCode().is2xxSuccessful()) {
                List<Doctor> listDoctor = doctorResponse.getBody();
                List<Patient> listPatient = patientResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentDoctor", currentDoctor);
            }

            model.addAttribute("rating", new Rating());
            return "admin/rating/create";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("CASHER")) {
            ResponseEntity<List<Doctor>> doctorResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            if (doctorResponse.getStatusCode().is2xxSuccessful() && patientResponse.getStatusCode().is2xxSuccessful()) {
                List<Doctor> listDoctor = doctorResponse.getBody();
                List<Patient> listPatient = patientResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("currentCasher", currentCasher);
            }

            model.addAttribute("rating", new Rating());
            return "admin/rating/create";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, Rating rating, @RequestParam("doctorID") String doctorID, @RequestParam("patientID") String patientID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra nếu comment chứa từ tục tĩu hoặc bậy bạ
        if (containsProfanity(rating.getComment())) {
            model.addAttribute("error", "Comment contains profanity. Please provide a valid comment.");
            // Handle the error as needed, for example, show an error message to the user.
            return "admin/rating/create";
        }

        // Lấy danh sách phiếu khám hiện có từ API server
        ResponseEntity<List<Rating>> responseList = restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Rating>>() {
        });
        List<Rating> existingRating = responseList.getBody();

        Doctor newDoctor = new Doctor();
        newDoctor.setId(Integer.parseInt(doctorID));
        rating.setDoctorId(newDoctor);
        var a = rating.getDoctorId().getId();

        Patient newPatient = new Patient();
        newPatient.setId(Integer.parseInt(patientID));
        rating.setPatientId(newPatient);
        var b = rating.getPatientId();

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Rating> request = new HttpEntity<>(rating, headers);

        ResponseEntity<Rating> response = restTemplate.exchange(apiUrl + "create", HttpMethod.POST, request, Rating.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Chuyển hướng về trang danh sách Taophieukham
            model.addAttribute("rating", new Rating());
            return "redirect:/admin/rating";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/rating/create";
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") int id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            Rating rating = restTemplate.getForObject(apiUrl + "/" + id, Rating.class);
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });
            ResponseEntity<List<Doctor>> doctorResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });
            if (patientResponse.getStatusCode().is2xxSuccessful() && doctorResponse.getStatusCode().is2xxSuccessful()) {
                List<Patient> listPatient = patientResponse.getBody();
                List<Doctor> listDoctor = doctorResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("rating", rating);
                model.addAttribute("currentAdmin", currentAdmin);
                return "/admin/rating/edit";
            } else {
                return "redirect:/admin/rating";
            }
        } else if (currentAdmin != null && currentAdmin.getRole().equals("DOCTOR")) {
            Rating rating = restTemplate.getForObject(apiUrl + "/" + id, Rating.class);
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });
            ResponseEntity<List<Doctor>> doctorResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });
            if (patientResponse.getStatusCode().is2xxSuccessful() && doctorResponse.getStatusCode().is2xxSuccessful()) {
                List<Patient> listPatient = patientResponse.getBody();
                List<Doctor> listDoctor = doctorResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("rating", rating);
                model.addAttribute("currentDoctor", currentDoctor);
                return "/admin/rating/edit";
            } else {
                return "redirect:/admin/rating";
            }
        } else if (currentAdmin != null && currentAdmin.getRole().equals("CASHER")) {
            Rating rating = restTemplate.getForObject(apiUrl + "/" + id, Rating.class);
            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });
            ResponseEntity<List<Doctor>> doctorResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });
            if (patientResponse.getStatusCode().is2xxSuccessful() && doctorResponse.getStatusCode().is2xxSuccessful()) {
                List<Patient> listPatient = patientResponse.getBody();
                List<Doctor> listDoctor = doctorResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("listPatient", listPatient);
                model.addAttribute("rating", rating);
                model.addAttribute("currentCasher", currentCasher);
                return "/admin/rating/edit";
            } else {
                return "redirect:/admin/rating";
            }
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute Rating updatedRating, @RequestParam String patientID, @RequestParam String doctorID) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra nếu comment chứa từ tục tĩu hoặc bậy bạ
        if (containsProfanity(updatedRating.getComment())) {
            model.addAttribute("error", "Comment contains profanity. Please provide a valid comment.");
            // Handle the error as needed, for example, show an error message to the user.
            return "admin/rating/edit";
        }

        // Lấy phiếu khám hiện có từ API server
        Rating existingRating = restTemplate.getForObject(apiUrl + "/" + updatedRating.getId(), Rating.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "edit/" + updatedRating.getId(); // Đảm bảo URL đúng

        // Tạo một HttpEntity với thông tin phiếu khám cập nhật để gửi yêu cầu PUT
        HttpEntity<Rating> request = new HttpEntity<>(updatedRating, headers);

        // Thực hiện PUT request để cập nhật phiếu khám
        restTemplate.exchange(url, HttpMethod.PUT, request, Rating.class);

        // Sau khi cập nhật thành công, chuyển hướng về trang danh sách phiếu khám
        return "redirect:/admin/rating";
    }

// Hàm kiểm tra xem comment có chứa từ tục tĩu hoặc bậy bạ không
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

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        restTemplate.delete(apiUrl + "delete/" + id);
        // Thực hiện thêm xử lý sau khi xóa Taophieukham thành công (nếu cần)

        // Chuyển hướng về trang danh sách Taophieukham
        return "redirect:/admin/rating";
    }

}
