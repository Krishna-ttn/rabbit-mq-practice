package com.example.rabbitmq.Service;



import com.example.rabbitmq.config.RabbitMQConfig;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    public void sendMessage(String message, String email) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                emailService(message, email)
        );
        redisTemplate.opsForValue().set(email, message);
        System.out.println("Email sent to : " + email);
    }

    @Async
    public String emailService(String text, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Testing...");
            String emailContent = "<p>Hi User, </p>" + text + "</p>";
            helper.setText(emailContent, true);
            mailSender.send(message);
            return text;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
