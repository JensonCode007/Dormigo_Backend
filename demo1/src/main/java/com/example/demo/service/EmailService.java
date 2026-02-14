package com.example.demo.service;

import com.example.demo.Entity.Order;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import freemarker.template.Configuration;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfiguration;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    @Value("${app.url: http://localhost:3000/}")
    private String appUrl;

    @Async
    public void sendWelcomeEmail(User user) {


        try{
            log.info("Sending welcome email 📨 to user with user id : {}", user.getId());
            Map<String, Object> model = new HashMap<>();
            model.put("firstName", user.getFirstName());
            model.put("lastName", user.getLastName());
            model.put("email", user.getEmail());
            model.put("appUrl", appUrl);

            sendTemplatedEmail(
                    user.getEmail(),
                    "Welcome to Dormgio 🎉🎊",
                    "welcome.ftl",
                    model
            );
            log.info("Welcome email sent to user with user id : {} ✅", user.getId());
        } catch (Exception e) {
            log.info("Couldn't send welcome email to the user ❌");
            throw new RuntimeException(e);
        }

    }

    @Async
    public void sendOrderConfirmationEmail(Order order) {
        try{
            log.info("Sending Order Confirmation Email to the user with order id : {} 📨", order.getId());

            Map<String, Object> model = new HashMap<>();
            model.put("buyerName",  order.getBuyer().getFirstName() + " " + order.getBuyer().getLastName());
            model.put("orderNumber",  order.getOrderNumber());
            model.put("orderDate", order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
            model.put("totalAmount", order.getTotalAmount());
            model.put("orderUrl", appUrl + "/orders/" + order.getId());

            model.put("items", order.getItems().stream().map(
                    item ->{
                        Map<String, Object> itemModel = new HashMap<>();
                        itemModel.put("productTitle", item.getProduct().getTitle());
                        itemModel.put("productQuantity", item.getProduct().getQuantity());
                        itemModel.put("productPrice", item.getProduct().getPrice());
                        itemModel.put("totalPrice", item.getProduct().getPrice());
                        itemModel.put("sellerName", item.getSeller().getFirstName() + " " + item.getSeller().getLastName());
                        return itemModel;
                    }

            ));
        }
        catch (Exception e){
            log.info("Couldn't sent the order confirmation email to the user with order id : {} ❌", order.getId());
        }
    }

    @Async
    public void sendOTPEmail(Order order, String otpCode){
        try{
            log.info("Sending OTP mail to the user with order id : {} 📨", order.getId());
            Map<String, Object> model = new HashMap<>();
            model.put("buyerName",  order.getBuyer().getFirstName() + " " + order.getBuyer().getLastName());
            model.put("orderNumber",  order.getOrderNumber());
            model.put("otpCode", otpCode);
            model.put("meetingLocation",  order.getMeetingLocation());
            model.put("meetingTime", order.getMeetingTime());
            model.put("meetingNotes", order.getMeetingNotes());

            sendTemplatedEmail(
                    order.getBuyer().getEmail(),
                    "🔏 Your OTP for the Order " + order.getOrderNumber(),
                    "otp-email.ftl",
                    model
            );

            log.info("OTP has been sent to the user with order id : {} ✅", order.getId());
        }
        catch (Exception e){
            log.info("Couldn't send the OTP email to the user with order id : {} ❌", order.getId());
        }
    }
    public void sendTestEmail(String toEmail, String name) {
        try {
            log.info("📧 Sending test email to:  {}", toEmail);

            Map<String, Object> model = new HashMap<>();
            model.put("name", name);
            model. put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm: ss a")));

            sendTemplatedEmail(
                    toEmail,
                    "✅ Dormigo Email Test",
                    "test-email.ftl",
                    model
            );

            log.info("✅ Test email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("❌ Failed to send test email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send test email", e);
        }
    }

    @Async
    public void sendLoginNotification(User user, String ipAddress, String device, String createdAt) {
        try{
            log.info("Sending login notification to user with id:  {} 📨",user.getId());

            Map<String, Object> model = new HashMap<>();
            model.put("firstName", user.getFirstName());
            model.put("loginTime", createdAt);
            model.put("deviceInfo", device);
            model.put("ipAddress", ipAddress);

            sendTemplatedEmail(
                    user.getEmail(),
                    "Login Notification 🤔",
                    "login-notification.ftl",
                    model
            );
            log.info("Login notification sent successfully to: {} ✅", user.getEmail());
        }
        catch (Exception e){
            log.info("Couldn't send login notification mail to the user ❌");
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, String resetToken){

    }

    @Async
    public void sendPaymentConfirmation(Order order){

        try{
            log.info("Sending Payment Confirmation Mail for the order id : {}", order.getId());

            Map<String, Object> model = new HashMap<>();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendSellerNotification(Order order){

    }

    @Async
    public void sendMeetingDetails(Order order){

    }

    @Async
    public void sendOrderCompletedEmail(Order order){

    }

    @Async
    public void sendOrderCancelledEmail(Order order){

    }

    @Async
    public void sendProductListedEmail(Product product){

    }



    private void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> model) {

        try{
            Template template = freemarkerConfiguration.getTemplate(templateName);
            String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            MimeMessage mimeMessage = mailSender.createMimeMessage(); // Creates an email draft
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                                            //email draft, enable attachment, enable emojis,etc

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

            log.info("Email Successfully Sent to the user 🎉");

        }
        catch (Exception e){
            log.info("Couldn't send email to the user ❌");
            throw new RuntimeException("Email Sending Failed",e);
        }
    }
}
