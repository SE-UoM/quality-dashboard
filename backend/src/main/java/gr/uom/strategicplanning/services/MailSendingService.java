package gr.uom.strategicplanning.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class MailSendingService {
    JavaMailSender mailSender;

    public MailSendingService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String loadHtmlTemplate(String templateName) {
        try {
            //System.getProperty("user.dir") + "\\src\\main\\resources\\emailTemplates" + templateName
            Resource resource = new ClassPathResource("emailTemplates/" + templateName);
            byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error loading template
            return "";
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Set HTML content to true
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }

    public void sendVerificationEmail(String email, String verificationCode, Long userId, String frontendUrl) {
        String subject = "UoM Dashboard | Verify Your Email";
        String confirmationUrl = frontendUrl + "/verify?token=" + verificationCode + "&uid=" + userId;

        // Load HTML template from file
        String htmlContent = loadHtmlTemplate("template-email-basic.html");

        // Replace placeholders in the template with actual values
        htmlContent = htmlContent.replace("{{GREETING_TEXT}}", "Hello!");
        htmlContent = htmlContent.replace("{{GREETING_SUBTEXT}}", "Thank you for signing up to UoM Dashboard!");
        htmlContent = htmlContent.replace("{{MAIL_BODY_TEXT}}", "Before you login we need to verify your email. To do this follow the instructions on this email.");
        htmlContent = htmlContent.replace("{{BUTTON_SECTION_HEADER_TEXT}}", "Click the button to verify your Account");
        htmlContent = htmlContent.replace("{{BUTTON_SECTION_URL}}", confirmationUrl);
        htmlContent = htmlContent.replace("{{BUTTON_TEXT}}", "Verify Account");
        htmlContent = htmlContent.replace("{{BUTTON_SUB_TEXT}}", "If the button does not work, please copy and paste the following link in your browser: ");
        htmlContent = htmlContent.replace("{{BUTTON_SUB_TEXT_CAPTION}}", confirmationUrl);

        // Send email with HTML content
        sendHtmlEmail(email, subject, htmlContent);
    }

    public void sendAnalysisCompletionEmail(String email, String projectName, String frontendUrl) {
        String subject = "UoM Dashboard | Analysis Completed";

        // Load HTML template from file
        String htmlContent = loadHtmlTemplate("template-email-basic.html");

        String dashboardUrl = frontendUrl + "/dashboard";

        // Replace placeholders in the template with actual values
        htmlContent = htmlContent.replace("{{GREETING_TEXT}}", "Hello!");
        htmlContent = htmlContent.replace("{{GREETING_SUBTEXT}}", "Thank you for signing up to UoM Dashboard!");
        htmlContent = htmlContent.replace("{{MAIL_BODY_TEXT}}", "We wanted to inform you that the analysis for the project " + projectName + " has been completed.");
        htmlContent = htmlContent.replace("{{BUTTON_SECTION_HEADER_TEXT}}", "View results on the Dashboard!");
        htmlContent = htmlContent.replace("{{BUTTON_SECTION_URL}}", dashboardUrl);
        htmlContent = htmlContent.replace("{{BUTTON_TEXT}}", "View Dashboard");
        htmlContent = htmlContent.replace("{{BUTTON_SUB_TEXT}}", "If the button does not work, please copy and paste the following link in your browser: ");
        htmlContent = htmlContent.replace("{{BUTTON_SUB_TEXT_CAPTION}}", dashboardUrl);

        // Send email with HTML content
        sendHtmlEmail(email, subject, htmlContent);
    }
}

