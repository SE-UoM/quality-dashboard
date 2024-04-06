package gr.uom.strategicplanning.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSendingService {
    JavaMailSender mailSender;

    public MailSendingService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email, String verificationCode, Long userId) {
        String subject = "UoM Dashboard | Verify Your Email";
        String confirmationUrl = "http://localhost:5173/verify?token=" + verificationCode + "&uid=" + userId;

        String message = "Hello! Please verify your email by clicking the link below: \n" + confirmationUrl;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public void sendAnalysisCompletionEmail(String email, String projectName) {
        String subject = "UoM Dashboard | Analysis Completed";
        String message =
                "Hello! The analysis for project " + projectName + " on UoM Dashboard has been completed."
                + " You can now view the results on the dashboard."
                + " Thank you for using UoM Dashboard!";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}

