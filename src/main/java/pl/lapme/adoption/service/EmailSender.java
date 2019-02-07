package pl.lapme.adoption.service;

public interface EmailSender {
    void sendEmail(String to, String subject, String content);

}
