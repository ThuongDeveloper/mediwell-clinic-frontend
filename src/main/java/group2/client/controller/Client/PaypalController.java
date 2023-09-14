/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package group2.client.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import group2.client.service.PaypalService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author hokim
 */
@Controller
public class PaypalController {

    @Autowired
    private PaypalService service;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

//    @RequestMapping("/book-appointment-time/{id}")
//    public String home(Model model, HttpSession session) {
//        session.setAttribute("msg", "Bạn đã đặt lịch thành công");
//        return "redirect:/book-appointment-time/{id}";
//
//    }

//    @PostMapping("/paypal")
//    public String page(Model model, @RequestParam("totalCost") Double totalCost,
//            @RequestParam("selector") String paymentSelect) throws PayPalRESTException {
//        Payment payment = service.createPayment(totalCost, "USD", paymentSelect, "SALE", "http://localhost:9999/" + CANCEL_URL, "http://localhost:9999/" + SUCCESS_URL);
//        for (Links link : payment.getLinks()) {
//            if (link.getRel().equals("approval_url")) {
//                return "redirect:" + link.getHref();
//            }
//        }
//        return "redirect:/checkout";
//    }
//
//    @GetMapping(value = SUCCESS_URL)
//    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
//        try {
//            Payment payment = service.executePayment(paymentId, payerId);
//            System.out.println(payment.toJSON());
//            if (payment.getState().equals("approved")) {
//                return "success";
//            }
//        } catch (PayPalRESTException e) {
//            System.out.println(e.getMessage());
//        }
//        return "redirect:/";
//    }
}
