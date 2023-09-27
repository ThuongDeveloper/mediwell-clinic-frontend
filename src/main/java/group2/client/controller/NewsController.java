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
import group2.client.service.AuthService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
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
    public String create(Model model, @ModelAttribute NewsDAO newsDAO, @RequestParam ("author") String author) {
        MultipartFile multipartFile = newsDAO.getBanner();
        String fileName = multipartFile.getOriginalFilename();
        String pathFile = this.fileUpload + fileName;
        try {
            FileCopyUtils.copy(newsDAO.getBanner().getBytes(), new File(pathFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        News news = new News();

        news.setTitle(newsDAO.getTitle());
       // news.setBanner(fileName);
        news.setContent(newsDAO.getContent());
        news.setStatus(newsDAO.getStatus());
       // news.setAuthor(author);

        // Tạo một HttpEntity với thông tin Casher để gửi yêu cầu POST
        HttpEntity<News> request = new HttpEntity<>(news, headers);

        ResponseEntity<News> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, News.class);

        // Kiểm tra mã trạng thái của phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            // Thực hiện thêm xử lý sau khi tạo Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
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
    public String update(Model model, @ModelAttribute News updatedNews) {
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
            return "redirect:/admin/news";

        } catch (RestClientException e) {
            model.addAttribute("news", existingNews);
            return "/admin/news/edit";
        }
    }

    @RequestMapping(value = "/admin/news/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Integer id, HttpServletRequest request) {

        Admin currentAdmin = authService.isAuthenticatedAdmin(request);
        Doctor currentDoctor = authService.isAuthenticatedDoctor(request);
        Patient currentPatient = authService.isAuthenticatedPatient(request);
        Casher currentCasher = authService.isAuthenticatedCasher(request);

        if (currentPatient != null && currentPatient.getRole().equals("PATIENT")) {
            return "redirect:/forbien";
        } else if (currentAdmin != null && currentAdmin.getRole().equals("ADMIN")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/news";
        } else if (currentDoctor != null && currentDoctor.getRole().equals("DOCTOR")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/news";
        } else if (currentCasher != null && currentCasher.getRole().equals("CASHER")) {
            restTemplate.delete(apiUrl + "/" + id);
            // Thực hiện thêm xử lý sau khi xóa Casher thành công (nếu cần)

            // Chuyển hướng về trang danh sách Casher
            return "redirect:/admin/news";
        } else {
            return "redirect:/login";
        }

    }

}
