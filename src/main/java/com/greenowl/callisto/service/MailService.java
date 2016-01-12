package com.greenowl.callisto.service;

import com.amazonaws.services.s3.model.S3Object;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;


/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private Environment env;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private FileService fileService;

    @Inject
    private com.greenowl.callisto.config.CallistoBeanConfigurationProperties props;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @PostConstruct
    public void init() {
        this.from = env.getProperty("spring.mail.from");
    }


    @Async
    public void sendAdminEmail(String subject, String content) {
        sendEmail(props.getAdmin().getEmail(), subject, content, false, false);
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}'",
                isMultipart, isHtml, to, subject);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'!", to);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }


    @Async
    public void sendResetPassEmail(String email, String link) {
        try {
            log.debug("Sending password reset e-mail to '{}'", email);
            Context context = new Context(Locale.ENGLISH);
            log.debug("Setting reset link in email as {}", link);
            context.setVariable("resetLink", link);
            String subject = "Password Reset";
            final String content = this.templateEngine.process("mail/password_reset", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
            // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
            message.setText(content, true /* isHtml */);
            message.setTo(email);
            message.setFrom(from);
            message.setSubject(subject);
            javaMailSender.send(mimeMessage);
            log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}'",
                    true, true, email, subject);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("E-mail could not be sent to user '{}', due to exception: {}", email, e.getMessage());
        }
    }


    private File getFileAsset(String path, String name) throws IOException {
        S3Object s3Object = fileService.getFile(path);
        File file = new File(name);
        byte[] byteArray = IOUtils.toByteArray(s3Object.getObjectContent());
        Files.write(byteArray, file);
        return file;
    }

}
