package com.example.demo.services;

import com.example.demo.entities.Donation;
import com.example.demo.entities.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailService(ObjectProvider<JavaMailSender> mailSenderProvider,
                        @Value("${app.mail.from:no-reply@demo.local}") String fromAddress) {
        this.mailSender = mailSenderProvider.getIfAvailable();
        this.fromAddress = fromAddress;
    }

    public void sendWelcomeEmail(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        String subject = "Bienvenue sur Gestion des Actions de Charité";
        String text = String.format("Bonjour %s,\n\nMerci de vous être inscrit sur notre plateforme.\n\nBienvenue parmi nous !\n\nCordialement,\nL'équipe Gestion des Actions de Charité",
                user.getName() != null ? user.getName() : "Utilisateur");

        sendSimpleEmail(user.getEmail(), subject, text);
    }

    public void sendDonationReceipt(Donation donation) {
        if (donation == null || donation.getUser() == null || donation.getUser().getEmail() == null || donation.getUser().getEmail().isBlank()) {
            return;
        }

        String subject = "Merci pour votre don";
        String userName = donation.getUser().getName() != null ? donation.getUser().getName() : "Utilisateur";
        String actionTitle = donation.getAction() != null && donation.getAction().getTitle() != null ? donation.getAction().getTitle() : "cette action";
        String text = String.format("Bonjour %s,\n\nMerci pour votre don de %.2f € à %s. Votre soutien nous aide à atteindre nos objectifs.\n\nCordialement,\nL'équipe Gestion des Actions de Charité",
                userName, donation.getAmount() != null ? donation.getAmount() : 0.0, actionTitle);

        sendSimpleEmail(donation.getUser().getEmail(), subject, text);
    }

    private void sendSimpleEmail(String to, String subject, String text) {
        if (mailSender == null) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
