/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller.Client;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import group2.client.entities.*;
import group2.client.repository.AppointmentRepository;
import group2.client.repository.DoctorRepository;
import group2.client.repository.LichlamviecRepository;
import group2.client.repository.PatientRepository;
import group2.client.service.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Ann
 */
@Controller
public class ClientController {

    String apiUrl = "http://localhost:8888/api/appointment/";
    private String apiUrlDoctor = "http://localhost:8888/api/doctor/";
    private String apiUrlPatient = "http://localhost:8888/api/patient/";
    private String apiUrlTypeDoctor = "http://localhost:8888/api/typedoctor/";
    private String apiUrlFilter = "http://localhost:8888/api/filter/";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private PaypalService service;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private LichlamviecRepository lichlamviecRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @RequestMapping("/")
    public String home(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            model.addAttribute("patient", currentPatient);
            return "/client/home";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            return "/client/home";
        }

    }

    @RequestMapping("/listDoctors")
    public String listDoctors(Model model, HttpServletRequest request) {

        Patient currentPatient = authService.isAuthenticatedPatient(request);

        ResponseEntity<List<Doctor>> response = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
        });
        ResponseEntity<List<TypeDoctor>> responseTD = restTemplate.exchange(apiUrlTypeDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TypeDoctor>>() {
        });
        if (response.getStatusCode().is2xxSuccessful() && responseTD.getStatusCode().is2xxSuccessful() && currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            List<Doctor> listDoctor = response.getBody();
            List<TypeDoctor> listTypeDoctor = responseTD.getBody();
            model.addAttribute("listDoctor", listDoctor);
            model.addAttribute("listTypeDoctor", listTypeDoctor);
            model.addAttribute("patient", currentPatient);
        } else if (response.getStatusCode().is2xxSuccessful() && responseTD.getStatusCode().is2xxSuccessful() && currentPatient == null) {
            List<Doctor> listDoctor = response.getBody();
            List<TypeDoctor> listTypeDoctor = responseTD.getBody();
            model.addAttribute("listDoctor", listDoctor);
            model.addAttribute("listTypeDoctor", listTypeDoctor);
        }
        return "/client/listDoctors";

    }

    @RequestMapping(value = "/listDoctors", method = RequestMethod.POST)
    public String filterListDoctors(Model model, HttpServletRequest request, @RequestParam("filterLDT") String filterLDT) {

        ResponseEntity<List<Doctor>> response = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Doctor>>() {
        });
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        ResponseEntity<List<TypeDoctor>> responseTD = restTemplate.exchange(apiUrlTypeDoctor, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TypeDoctor>>() {
        });

        if ("ALL".equals(filterLDT)) {
            // Trường hợp khi lựa chọn là "ALL"
            return "redirect:/listDoctors";
        } else {
            // Trường hợp khi lựa chọn không phải là "ALL"
            ResponseEntity<List<Doctor>> responseFilter = restTemplate.exchange(apiUrlFilter + "/doctor/" + filterLDT, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });
            if (response.getStatusCode().is2xxSuccessful() && responseFilter.getStatusCode().is2xxSuccessful() && responseTD.getStatusCode().is2xxSuccessful()
                    && currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
                List<Doctor> listDoctor = response.getBody();
                List<Doctor> filterListDoctor = responseFilter.getBody();
                List<TypeDoctor> listTypeDoctor = responseTD.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("filterListDoctor", filterListDoctor);
                model.addAttribute("listTypeDoctor", listTypeDoctor);
                model.addAttribute("patient", currentPatient);
            } else if (response.getStatusCode().is2xxSuccessful() && responseFilter.getStatusCode().is2xxSuccessful() && responseTD.getStatusCode().is2xxSuccessful()
                    && currentPatient == null) {
                List<Doctor> listDoctor = response.getBody();
                List<Doctor> filterListDoctor = responseFilter.getBody();
                List<TypeDoctor> listTypeDoctor = responseTD.getBody();
                model.addAttribute("listDoctor", listDoctor);
                model.addAttribute("filterListDoctor", filterListDoctor);
                model.addAttribute("listTypeDoctor", listTypeDoctor);
            }
        }
        return "/client/filterListDoctors";
    }

    @RequestMapping("/book-appointment/{id}")
    public String bookAppointment(Model model, @PathVariable(value = "id") int id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            model.addAttribute("patient", currentPatient);
            Doctor doctor = doctorRepository.findById(id).get();
            List<Lichlamviec> lichlamviec = lichlamviecRepository.findByDoctorId(doctor);
            model.addAttribute("lichlamviec", lichlamviec);
            model.addAttribute("doctor", doctor);
            return "/client/bookAppointment";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            Doctor doctor = doctorRepository.findById(id).get();
            List<Lichlamviec> lichlamviec = lichlamviecRepository.findByDoctorId(doctor);
            model.addAttribute("lichlamviec", lichlamviec);
            model.addAttribute("doctor", doctor);
            return "/client/bookAppointment";
        }

    }

    public boolean checkAlreadyBookTime(List<Appointment> appointments, String gio) {
        boolean bl = true;
        String[] parts = gio.split("-");
        String starttime = parts[0].trim();
        starttime = starttime + ":00";

        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getStarttime().equals(starttime)) {
                bl = false;
            }
        }
        return bl;
    }

    @RequestMapping("/book-appointment-time/{id}")
    public String bookAppointmentTime(Model model, @PathVariable(value = "id") int id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {

            model.addAttribute("patient", currentPatient);

            Lichlamviec lichlamviec = lichlamviecRepository.findById(id).get();

            List<Appointment> listAPPDOCTOR = appointmentRepository.findByDateAndDoctorId(lichlamviec.getDate(), lichlamviec.getDoctorId());

            List<String> lichlamviecByDate = new ArrayList<>();

            if (lichlamviec.getStarttime().equals("07:00:00")) {
                if (checkAlreadyBookTime(listAPPDOCTOR, "07:00 - 07:30")) {
                    lichlamviecByDate.add("07:00 - 07:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "07:30 - 08:00")) {
                    lichlamviecByDate.add("07:30 - 08:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "08:00 - 08:30")) {
                    lichlamviecByDate.add("08:00 - 08:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "08:30 - 09:00")) {
                    lichlamviecByDate.add("08:30 - 09:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "09:00 - 09:30")) {
                    lichlamviecByDate.add("09:00 - 09:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "09:30 - 10:00")) {
                    lichlamviecByDate.add("09:30 - 10:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "10:00 - 10:30")) {
                    lichlamviecByDate.add("10:00 - 10:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "10:30 - 11:00")) {
                    lichlamviecByDate.add("10:30 - 11:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "11:00 - 11:30")) {
                    lichlamviecByDate.add("11:00 - 11:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "11:30 - 12:00")) {
                    lichlamviecByDate.add("11:30 - 12:00");
                }

            }
            if (lichlamviec.getStarttime().equals("12:00:00")) {
                if (checkAlreadyBookTime(listAPPDOCTOR, "12:00 - 12:30")) {
                    lichlamviecByDate.add("12:00 - 12:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "12:30 - 13:00")) {
                    lichlamviecByDate.add("12:30 - 13:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "13:00 - 13:30")) {
                    lichlamviecByDate.add("13:00 - 13:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "13:30 - 14:00")) {
                    lichlamviecByDate.add("13:30 - 14:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "14:00 - 14:30")) {
                    lichlamviecByDate.add("14:00 - 14:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "14:30 - 15:00")) {
                    lichlamviecByDate.add("14:30 - 15:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "15:00 - 15:30")) {
                    lichlamviecByDate.add("15:00 - 15:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "15:30 - 16:00")) {
                    lichlamviecByDate.add("15:30 - 16:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "16:00 - 16:30")) {
                    lichlamviecByDate.add("16:00 - 16:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "16:30 - 17:00")) {
                    lichlamviecByDate.add("16:30 - 17:00");
                }

            }
            if (lichlamviec.getStarttime().equals("17:00:00")) {
                if (checkAlreadyBookTime(listAPPDOCTOR, "17:00 - 17:30")) {
                    lichlamviecByDate.add("17:00 - 17:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "17:30 - 18:00")) {
                    lichlamviecByDate.add("17:30 - 18:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "18:00 - 18:30")) {
                    lichlamviecByDate.add("18:00 - 18:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "18:30 - 19:00")) {
                    lichlamviecByDate.add("18:30 - 19:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "19:00 - 19:30")) {
                    lichlamviecByDate.add("19:00 - 19:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "19:30 - 20:00")) {
                    lichlamviecByDate.add("19:30 - 20:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "20:00 - 20:30")) {
                    lichlamviecByDate.add("20:00 - 20:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "20:30 - 21:00")) {
                    lichlamviecByDate.add("20:30 - 21:00");
                }

            }

            model.addAttribute("lichlamviec", lichlamviec);
            model.addAttribute("lichlamviecByDate", lichlamviecByDate);
            model.addAttribute("listAPPDOCTOR", listAPPDOCTOR);
            model.addAttribute("appointment", new Appointment());
            return "/client/bookAppointmentTime";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {

            Lichlamviec lichlamviec = lichlamviecRepository.findById(id).get();

            List<Appointment> listAPPDOCTOR = appointmentRepository.findByDateAndDoctorId(lichlamviec.getDate(), lichlamviec.getDoctorId());

            List<String> lichlamviecByDate = new ArrayList<>();

            if (lichlamviec.getStarttime().equals("07:00:00")) {
                if (checkAlreadyBookTime(listAPPDOCTOR, "07:00 - 07:30")) {
                    lichlamviecByDate.add("07:00 - 07:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "07:30 - 08:00")) {
                    lichlamviecByDate.add("07:30 - 08:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "08:00 - 08:30")) {
                    lichlamviecByDate.add("08:00 - 08:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "08:30 - 09:00")) {
                    lichlamviecByDate.add("08:30 - 09:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "09:00 - 09:30")) {
                    lichlamviecByDate.add("09:00 - 09:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "09:30 - 10:00")) {
                    lichlamviecByDate.add("09:30 - 10:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "10:00 - 10:30")) {
                    lichlamviecByDate.add("10:00 - 10:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "10:30 - 11:00")) {
                    lichlamviecByDate.add("10:30 - 11:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "11:00 - 11:30")) {
                    lichlamviecByDate.add("11:00 - 11:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "11:30 - 12:00")) {
                    lichlamviecByDate.add("11:30 - 12:00");
                }

            }
            if (lichlamviec.getStarttime().equals("12:00:00")) {
                if (checkAlreadyBookTime(listAPPDOCTOR, "12:00 - 12:30")) {
                    lichlamviecByDate.add("12:00 - 12:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "12:30 - 13:00")) {
                    lichlamviecByDate.add("12:30 - 13:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "13:00 - 13:30")) {
                    lichlamviecByDate.add("13:00 - 13:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "13:30 - 14:00")) {
                    lichlamviecByDate.add("13:30 - 14:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "14:00 - 14:30")) {
                    lichlamviecByDate.add("14:00 - 14:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "14:30 - 15:00")) {
                    lichlamviecByDate.add("14:30 - 15:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "15:00 - 15:30")) {
                    lichlamviecByDate.add("15:00 - 15:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "15:30 - 16:00")) {
                    lichlamviecByDate.add("15:30 - 16:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "16:00 - 16:30")) {
                    lichlamviecByDate.add("16:00 - 16:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "16:30 - 17:00")) {
                    lichlamviecByDate.add("16:30 - 17:00");
                }

            }
            if (lichlamviec.getStarttime().equals("17:00:00")) {
                if (checkAlreadyBookTime(listAPPDOCTOR, "17:00 - 17:30")) {
                    lichlamviecByDate.add("17:00 - 17:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "17:30 - 18:00")) {
                    lichlamviecByDate.add("17:30 - 18:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "18:00 - 18:30")) {
                    lichlamviecByDate.add("18:00 - 18:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "18:30 - 19:00")) {
                    lichlamviecByDate.add("18:30 - 19:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "19:00 - 19:30")) {
                    lichlamviecByDate.add("19:00 - 19:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "19:30 - 20:00")) {
                    lichlamviecByDate.add("19:30 - 20:00");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "20:00 - 20:30")) {
                    lichlamviecByDate.add("20:00 - 20:30");
                }
                if (checkAlreadyBookTime(listAPPDOCTOR, "20:30 - 21:00")) {
                    lichlamviecByDate.add("20:30 - 21:00");
                }

            }

            model.addAttribute("lichlamviec", lichlamviec);
            model.addAttribute("lichlamviecByDate", lichlamviecByDate);
            model.addAttribute("listAPPDOCTOR", listAPPDOCTOR);
            model.addAttribute("appointment", new Appointment());
            return "/client/bookAppointmentTime";
        }
    }

    @RequestMapping(value = "/book-appointment-create/{id}", method = RequestMethod.POST)
    public String bookAppointmentCreate(Model model, @PathVariable(value = "id") int id,
            @ModelAttribute Appointment appointment, HttpServletRequest request, HttpSession session,
            @RequestParam("select-hours") String selectHours, @RequestParam("symptom") String symptom) throws PayPalRESTException {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {

            Lichlamviec lichlamviec = lichlamviecRepository.findById(id).get();

            List<Appointment> appointmentByDate = appointmentRepository.findByDateAndPatientId(lichlamviec.getDate(), currentPatient);

            if (appointmentByDate.size() > 0) {
                return "redirect:/book-appointment-already/{id}va" + selectHours + "va" + symptom;
            } else {

                Doctor newDoctor = new Doctor();
                newDoctor.setId(lichlamviec.getDoctorId().getId());
                appointment.setDoctorId(newDoctor);

                Patient newPatient = new Patient();
                newPatient.setId(currentPatient.getId());
                appointment.setPatientId(newPatient);

                appointment.setDate(lichlamviec.getDate());

                if (selectHours.equals("07:00 - 07:30")) {
                    appointment.setStarttime("07:00");
                    appointment.setEndtime("07:30");

                }
                if (selectHours.equals("07:30 - 08:00")) {
                    appointment.setStarttime("07:30");
                    appointment.setEndtime("08:00");

                }
                if (selectHours.equals("08:00 - 08:30")) {
                    appointment.setStarttime("08:00");
                    appointment.setEndtime("08:30");

                }
                if (selectHours.equals("08:30 - 09:00")) {
                    appointment.setStarttime("08:30");
                    appointment.setEndtime("09:00");

                }
                if (selectHours.equals("09:00 - 09:30")) {
                    appointment.setStarttime("09:00");
                    appointment.setEndtime("09:30");

                }
                if (selectHours.equals("09:30 - 10:00")) {
                    appointment.setStarttime("09:30");
                    appointment.setEndtime("10:00");

                }
                if (selectHours.equals("10:00 - 10:30")) {
                    appointment.setStarttime("10:00");
                    appointment.setEndtime("10:30");

                }
                if (selectHours.equals("10:30 - 11:00")) {
                    appointment.setStarttime("10:30");
                    appointment.setEndtime("11:00");

                }
                if (selectHours.equals("11:00 - 11:30")) {
                    appointment.setStarttime("11:00");
                    appointment.setEndtime("11:30");

                }
                if (selectHours.equals("11:30 - 12:00")) {
                    appointment.setStarttime("11:30");
                    appointment.setEndtime("12:00");

                }
                if (selectHours.equals("12:00 - 12:30")) {
                    appointment.setStarttime("12:00");
                    appointment.setEndtime("12:30");

                }
                if (selectHours.equals("12:30 - 13:00")) {
                    appointment.setStarttime("12:30");
                    appointment.setEndtime("13:00");

                }
                if (selectHours.equals("13:00 - 13:30")) {
                    appointment.setStarttime("13:00");
                    appointment.setEndtime("13:30");

                }
                if (selectHours.equals("13:30 - 14:00")) {
                    appointment.setStarttime("13:30");
                    appointment.setEndtime("14:00");

                }
                if (selectHours.equals("14:00 - 14:30")) {
                    appointment.setStarttime("14:00");
                    appointment.setEndtime("14:30");

                }
                if (selectHours.equals("14:30 - 15:00")) {
                    appointment.setStarttime("14:30");
                    appointment.setEndtime("15:00");

                }
                if (selectHours.equals("15:00 - 15:30")) {
                    appointment.setStarttime("15:00");
                    appointment.setEndtime("15:30");

                }
                if (selectHours.equals("15:30 - 16:00")) {
                    appointment.setStarttime("15:30");
                    appointment.setEndtime("16:00");

                }
                if (selectHours.equals("16:00 - 16:30")) {
                    appointment.setStarttime("16:00");
                    appointment.setEndtime("16:30");

                }
                if (selectHours.equals("16:30 - 17:00")) {
                    appointment.setStarttime("16:30");
                    appointment.setEndtime("17:00");

                }
                if (selectHours.equals("17:00 - 17:30")) {
                    appointment.setStarttime("17:00");
                    appointment.setEndtime("17:30");

                }
                if (selectHours.equals("17:30 - 18:00")) {
                    appointment.setStarttime("17:30");
                    appointment.setEndtime("18:00");

                }
                if (selectHours.equals("18:00 - 18:30")) {
                    appointment.setStarttime("18:00");
                    appointment.setEndtime("18:30");

                }
                if (selectHours.equals("18:30 - 19:00")) {
                    appointment.setStarttime("18:30");
                    appointment.setEndtime("19:00");

                }
                if (selectHours.equals("19:00 - 19:30")) {
                    appointment.setStarttime("19:00");
                    appointment.setEndtime("19:30");

                }
                if (selectHours.equals("19:30 - 20:00")) {
                    appointment.setStarttime("19:30");
                    appointment.setEndtime("20:00");

                }
                if (selectHours.equals("20:00 - 20:30")) {
                    appointment.setStarttime("20:00");
                    appointment.setEndtime("20:30");

                }
                if (selectHours.equals("20:30 - 21:00")) {
                    appointment.setStarttime("20:30");
                    appointment.setEndtime("21:00");

                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(lichlamviec.getDate());

                Payment payment = service.createPayment(50.0, "USD", "Paypal", "SALE",
                        "http://localhost:9999/" + "id=" + lichlamviec.getId() + "/" + CANCEL_URL,
                        "http://localhost:9999/" + "patientid=" + currentPatient.getId() + "va" + "doctorid=" + lichlamviec.getDoctorId().getId()
                        + "va" + "date=" + formattedDate
                        + "va" + "starttime=" + appointment.getStarttime()
                        + "va" + "endtime=" + appointment.getEndtime()
                        + "va" + "symptom=" + symptom
                        + "/" + SUCCESS_URL
                );
                for (Links link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        return "redirect:" + link.getHref();
                    }
                }

                session.setAttribute("msg", "Bạn đã đặt lịch thành công");
                return "redirect:/book-appointment-time/{id}";
            }

        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "patientid={patientid}vadoctorid={doctorid}vadate={dateString}vastarttime={starttime}vaendtime={endtime}vasymptom={symptom}/" + SUCCESS_URL, method = RequestMethod.GET)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @ModelAttribute Appointment appointment,
            @PathVariable("patientid") Integer patientid, @PathVariable("doctorid") Integer doctorid,
            @PathVariable("dateString") String dateString,
            @PathVariable("starttime") String starttime,
            @PathVariable("endtime") String endtime,
            @PathVariable("symptom") String symptom
    ) throws ParseException {

        Doctor newDoctor = new Doctor();
        newDoctor.setId(doctorid);
        appointment.setDoctorId(newDoctor);

        Patient newPatient = new Patient();
        newPatient.setId(patientid);
        appointment.setPatientId(newPatient);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);

        appointment.setDate(date);

        appointment.setStarttime(starttime);

        appointment.setEndtime(endtime);

        appointment.setSymptom(symptom);
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                restTemplate.postForObject(apiUrl, appointment, Appointment.class);
                return "client/paypal/success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }

    @RequestMapping(value = "id={id}/" + CANCEL_URL, method = RequestMethod.GET)
    public String cancelPay(@PathVariable(value = "id") int id, HttpSession session) {
        session.setAttribute("cancel", "The transaction has been canceled by you.");
        return "redirect:/book-appointment-time/{id}";
    }

    @RequestMapping(value = "/book-appointment-already-create/{id}", method = RequestMethod.POST)
    public String bookAppointmentCreateAlready(Model model, @PathVariable(value = "id") int id, @ModelAttribute Appointment appointment, HttpServletRequest request, HttpSession session, @RequestParam("select-hours") String selectHours, @RequestParam("symptom") String symptom) throws PayPalRESTException {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {

            Lichlamviec lichlamviec = lichlamviecRepository.findById(id).get();

            Doctor newDoctor = new Doctor();
            newDoctor.setId(lichlamviec.getDoctorId().getId());
            appointment.setDoctorId(newDoctor);

            Patient newPatient = new Patient();
            newPatient.setId(currentPatient.getId());
            appointment.setPatientId(newPatient);

            appointment.setDate(lichlamviec.getDate());

            appointment.setSymptom(symptom);

            if (selectHours.equals("07:00 - 07:30")) {
                appointment.setStarttime("07:00");
                appointment.setEndtime("07:30");

            }
            if (selectHours.equals("07:30 - 08:00")) {
                appointment.setStarttime("07:30");
                appointment.setEndtime("08:00");

            }
            if (selectHours.equals("08:00 - 08:30")) {
                appointment.setStarttime("08:00");
                appointment.setEndtime("08:30");

            }
            if (selectHours.equals("08:30 - 09:00")) {
                appointment.setStarttime("08:30");
                appointment.setEndtime("09:00");

            }
            if (selectHours.equals("09:00 - 09:30")) {
                appointment.setStarttime("09:00");
                appointment.setEndtime("09:30");

            }
            if (selectHours.equals("09:30 - 10:00")) {
                appointment.setStarttime("09:30");
                appointment.setEndtime("10:00");

            }
            if (selectHours.equals("10:00 - 10:30")) {
                appointment.setStarttime("10:00");
                appointment.setEndtime("10:30");

            }
            if (selectHours.equals("10:30 - 11:00")) {
                appointment.setStarttime("10:30");
                appointment.setEndtime("11:00");

            }
            if (selectHours.equals("11:00 - 11:30")) {
                appointment.setStarttime("11:00");
                appointment.setEndtime("11:30");

            }
            if (selectHours.equals("11:30 - 12:00")) {
                appointment.setStarttime("11:30");
                appointment.setEndtime("12:00");

            }
            if (selectHours.equals("12:00 - 12:30")) {
                appointment.setStarttime("12:00");
                appointment.setEndtime("12:30");

            }
            if (selectHours.equals("12:30 - 13:00")) {
                appointment.setStarttime("12:30");
                appointment.setEndtime("13:00");

            }
            if (selectHours.equals("13:00 - 13:30")) {
                appointment.setStarttime("13:00");
                appointment.setEndtime("13:30");

            }
            if (selectHours.equals("13:30 - 14:00")) {
                appointment.setStarttime("13:30");
                appointment.setEndtime("14:00");

            }
            if (selectHours.equals("14:00 - 14:30")) {
                appointment.setStarttime("14:00");
                appointment.setEndtime("14:30");

            }
            if (selectHours.equals("14:30 - 15:00")) {
                appointment.setStarttime("14:30");
                appointment.setEndtime("15:00");

            }
            if (selectHours.equals("15:00 - 15:30")) {
                appointment.setStarttime("15:00");
                appointment.setEndtime("15:30");

            }
            if (selectHours.equals("15:30 - 16:00")) {
                appointment.setStarttime("15:30");
                appointment.setEndtime("16:00");

            }
            if (selectHours.equals("16:00 - 16:30")) {
                appointment.setStarttime("16:00");
                appointment.setEndtime("16:30");

            }
            if (selectHours.equals("16:30 - 17:00")) {
                appointment.setStarttime("16:30");
                appointment.setEndtime("17:00");

            }
            if (selectHours.equals("17:00 - 17:30")) {
                appointment.setStarttime("17:00");
                appointment.setEndtime("17:30");

            }
            if (selectHours.equals("17:30 - 18:00")) {
                appointment.setStarttime("17:30");
                appointment.setEndtime("18:00");

            }
            if (selectHours.equals("18:00 - 18:30")) {
                appointment.setStarttime("18:00");
                appointment.setEndtime("18:30");

            }
            if (selectHours.equals("18:30 - 19:00")) {
                appointment.setStarttime("18:30");
                appointment.setEndtime("19:00");

            }
            if (selectHours.equals("19:00 - 19:30")) {
                appointment.setStarttime("19:00");
                appointment.setEndtime("19:30");

            }
            if (selectHours.equals("19:30 - 20:00")) {
                appointment.setStarttime("19:30");
                appointment.setEndtime("20:00");

            }
            if (selectHours.equals("20:00 - 20:30")) {
                appointment.setStarttime("20:00");
                appointment.setEndtime("20:30");

            }
            if (selectHours.equals("20:30 - 21:00")) {
                appointment.setStarttime("20:30");
                appointment.setEndtime("21:00");

            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(lichlamviec.getDate());

            Payment payment = service.createPayment(50.0, "USD", "Paypal", "SALE",
                    "http://localhost:9999/" + "id=" + lichlamviec.getId() + "/" + CANCEL_URL,
                    "http://localhost:9999/" + "patientid=" + currentPatient.getId() + "va" + "doctorid=" + lichlamviec.getDoctorId().getId()
                    + "va" + "date=" + formattedDate
                    + "va" + "starttime=" + appointment.getStarttime()
                    + "va" + "endtime=" + appointment.getEndtime()
                    + "va" + "symptom=" + symptom
                    + "/" + SUCCESS_URL
            );
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
            return "redirect:/book-appointment-time/{id}";

        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping("/book-appointment-already/{id}va{gio}va{symptom}")
    public String bookAppointmentAlreadyCreate(Model model, @PathVariable(value = "id") int id, @PathVariable(value = "gio") String gio, @PathVariable(value = "symptom") String symptom, HttpServletRequest request) {
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        var hours = gio;
        var symptoms = symptom;

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {

            Lichlamviec lichlamviec = lichlamviecRepository.findById(id).get();
            model.addAttribute("hours", hours);
            model.addAttribute("symptoms", symptoms);
            model.addAttribute("lichlamviec", lichlamviec);
            return "/client/bookAppointmentAlreadyCreate";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
    public String editProfile(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {

        Patient currentPatient = authService.isAuthenticatedPatient(request);

        ResponseEntity<Patient> response = restTemplate.exchange(apiUrlPatient + "/" + id, HttpMethod.GET, null,
                new ParameterizedTypeReference<Patient>() {
        });

        if (response.getStatusCode().is2xxSuccessful() && currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            Patient patient = response.getBody();
            model.addAttribute("patientProfile", patient);
            model.addAttribute("patient", currentPatient);
        }

        return "/client/profile";
    }

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.POST)
    public String update(Model model, Patient updatedPatient, @PathVariable("id") Integer id, @RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        if (file != null && !file.isEmpty()) {
            // Xử lý tệp tin ảnh nếu cần
            String fileName = file.getOriginalFilename();
            // Thực hiện xử lý tệp tin ảnh ở đây nếu cần
        }
        id = updatedPatient.getId();

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("updatedPatient", updatedPatient);
        body.add("file", fileResource);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        Patient existingPatient = restTemplate.getForObject(apiUrlPatient + "/" + id, Patient.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrlPatient + "/edit/" + id;

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Patient> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Patient.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Cập nhật thành công, thực hiện chuyển hướng đến trang hồ sơ với id của người dùng
            return "redirect:/profile/" + id;
        } else {
            // Xử lý lỗi nếu cần thiết và hiển thị trang hồ sơ
            Patient patient = response.getBody();
            model.addAttribute("patientProfile", patient);
            return "/client/profile";
        }
    }

    @RequestMapping("/appointment/create")
    public String create(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {

            model.addAttribute("currentPatient", currentPatient);

            ResponseEntity<List<Doctor>> DResponse = restTemplate.exchange(apiUrlDoctor, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Doctor>>() {
            });

            ResponseEntity<List<Patient>> patientResponse = restTemplate.exchange(apiUrlPatient, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Patient>>() {
            });

            if (DResponse.getStatusCode().is2xxSuccessful() && patientResponse.getStatusCode().is2xxSuccessful()) {
                // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
                List<Patient> listPatient = patientResponse.getBody();
                model.addAttribute("listPatient", listPatient);
                List<Doctor> listDoctor = DResponse.getBody();
                model.addAttribute("listDoctor", listDoctor);

                // Chuyển hướng về trang danh sách Casher
            }
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("appointment", new Appointment());
            return "/client/appointment";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/forbien";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/forbien";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/forbien";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/appointment/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute Appointment appointment, @RequestParam("doctor") String doctorID, @RequestParam("patient") int patientID, @RequestParam("time_select") String timeSelect) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Doctor newDoctor = new Doctor();
        newDoctor.setId(Integer.parseInt(doctorID));
        appointment.setDoctorId(newDoctor);

        Patient newPatient = new Patient();
        newPatient.setId(patientID);
        appointment.setPatientId(newPatient);

        if (timeSelect.equals("7:00 - 12:00")) {
            appointment.setStarttime("7:00");
            appointment.setEndtime("12:00");
        }
        if (timeSelect.equals("12:00 - 17:00")) {
            appointment.setStarttime("12:00");
            appointment.setEndtime("17:00");
        }
        if (timeSelect.equals("17:00 - 21:00")) {
            appointment.setStarttime("17:00");
            appointment.setEndtime("21:00");
        }

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<Appointment> request = new HttpEntity<>(appointment, headers);

        ResponseEntity<Appointment> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Appointment.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)
            // Chuyển hướng về trang danh sách Casher
            model.addAttribute("appointment", new Appointment());
            return "redirect:/appointment/create";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/appointment/create";
        }
    }

    @RequestMapping("/previousUrl")
    public String previousUrl(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            return "redirect:/admin";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            return "redirect:/admin";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            return "redirect:/admin";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/forbien")
    public String forbien(Model model) {
        return "/auth/forbien";
    }

}
