/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller;

import group2.client.dto.NewsDAO;
import group2.client.entities.Admin;
import group2.client.entities.Casher;
import group2.client.entities.Doctor;
import group2.client.entities.News;
import group2.client.entities.Patient;
import group2.client.repository.NewsRepository;
import group2.client.service.AuthService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 */
@Controller
public class NewsController {
    
    @Value("${upload.path}")
    private String fileUpload;

    String apiUrl = "http://localhost:8888/api/news";
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private AuthService authService;
    
    @Autowired
    private NewsRepository newsRepository;

    @RequestMapping("/admin/news")
    public String page(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            ResponseEntity<List<News>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<News>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<News> listNews = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listNews", listNews);
                model.addAttribute("currentAdmin", currentAdmin);
            }
            return "/admin/news/index";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            ResponseEntity<List<News>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<News>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<News> listNews = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listNews", listNews);
                model.addAttribute("currentDoctor", currentDoctor);
            }
            return "/admin/news/index";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            ResponseEntity<List<News>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<News>>() {
            });
            // Kiểm tra mã trạng thái của phản hồi
            if (response.getStatusCode().is2xxSuccessful()) {
                List<News> listNews = response.getBody();
                // Xử lý dữ liệu theo nhu cầu của bạn
                model.addAttribute("listNews", listNews);
                model.addAttribute("currentCasher", currentCasher);
            }
            return "/admin/news/index";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping("/admin/news/create")
    public String create(Model model, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("news", new News());
            model.addAttribute("currentAdmin", currentAdmin);
            return "/admin/news/create";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("news", new News());
            model.addAttribute("currentDoctor", currentDoctor);
            return "/admin/news/create";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            // Tạo một đối tượng Casher trống để gửi thông tin tới form tạo mới
            model.addAttribute("news", new News());
            model.addAttribute("currentCasher", currentCasher);
            return "/admin/news/create";
        } else {
            return "redirect:/login";
        }

    }

   @RequestMapping(value = "/admin/news/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute News news, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
         ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("News", news);
        body.add("file", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity response = restTemplate.exchange(apiUrl + "/create", HttpMethod.POST, requestEntity, String.class);
        
        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            session.setAttribute("msg", "Create news successful!");
            return "redirect:/admin/news";
        } else {
            // Xử lý lỗi nếu cần thiết
            return "/admin/news/create";
        }
    }

    @RequestMapping(value = "/admin/news/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Integer id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            News news = restTemplate.getForObject(apiUrl + "/" + id, News.class);
            model.addAttribute("news", news);
            model.addAttribute("currentAdmin", currentAdmin);
            return "/admin/news/edit";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            News news = restTemplate.getForObject(apiUrl + "/" + id, News.class);
            model.addAttribute("news", news);
            model.addAttribute("currentDoctor", currentDoctor);
            return "/admin/news/edit";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            News news = restTemplate.getForObject(apiUrl + "/" + id, News.class);
            model.addAttribute("news", news);
            model.addAttribute("currentCasher", currentCasher);
            return "/admin/news/edit";
        } else {
            return "redirect:/login";
        }

    }

    @RequestMapping(value = "/admin/news/edit", method = RequestMethod.POST)
    public String update(Model model, @ModelAttribute News updatedNews, HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Kiểm tra dữ liệu đã cập nhật có thay đổi so với dữ liệu hiện có trong cơ sở dữ liệu hay không
        News existingNews = restTemplate.getForObject(apiUrl + "/" + updatedNews.getId(), News.class);

        // Bổ sung id vào URL khi thực hiện PUT
        String url = apiUrl + "/" + updatedNews.getId();

        // Tạo một HttpEntity với thông tin Casher cập nhật để gửi yêu cầu PUT
        HttpEntity<News> request = new HttpEntity<>(updatedNews, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, request, News.class);
            session.setAttribute("msg", "Update news successful!");
            return "redirect:/admin/news";

        } catch (RestClientException e) {
            model.addAttribute("news", existingNews);
            return "/admin/news/edit";
        }
    }

    @RequestMapping(value = "/admin/news/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id, HttpServletRequest request, HttpSession session) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)
            session.setAttribute("msg", "Delete news successful!");
            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/news";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)
            session.setAttribute("msg", "Delete news successful!");
            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/news";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)
            session.setAttribute("msg", "Delete news successful!");
            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/news";
        } else {
            return "redirect:/login";
        }

    }
    
      @RequestMapping(value = "/change-status-news/{id}", method = RequestMethod.POST)
    public String changeNewsStatus(Model model, @PathVariable("id") Integer id) {
        News news = newsRepository.findById(id).get();
        if(news.getStatus().equals(Boolean.FALSE)){
            news.setStatus(Boolean.TRUE);
        }else{
            news.setStatus(Boolean.FALSE);
        }
        newsRepository.save(news);
        return "redirect:/admin/news";
    }

}
