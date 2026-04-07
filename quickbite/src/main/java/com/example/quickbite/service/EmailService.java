package com.example.quickbite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String name) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("Welcome to QuickBite! 🍔");
            msg.setText("Hi " + name + ",\n\nWelcome to QuickBite! Your account has been created successfully.\n\nEnjoy ordering your favourite food!\n\nTeam QuickBite");
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Email send failed: " + e.getMessage());
        }
    }

    public void sendOrderConfirmationEmail(String to, String name, Long orderId, Double total) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("QuickBite Order Confirmed! #" + orderId);
            msg.setText("Hi " + name + ",\n\nYour order #" + orderId + " has been confirmed!\n\nTotal Amount: ₹" + total + "\n\nEstimated delivery: 30-45 minutes\n\nTrack your order at: http://localhost:8080/order/tracking.html?orderId=" + orderId + "\n\nTeam QuickBite 🍔");
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Email send failed: " + e.getMessage());
        }
    }

    public void sendOrderDeliveredEmail(String to, String name, Long orderId) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("QuickBite Order Delivered! #" + orderId);
            msg.setText("Hi " + name + ",\n\nYour order #" + orderId + " has been delivered!\n\nThank you for ordering with QuickBite. Enjoy your meal! 😋\n\nTeam QuickBite");
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Email send failed: " + e.getMessage());
        }
    }
}
