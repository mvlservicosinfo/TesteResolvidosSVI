package org.example.Exercicio004;

import java.net.PasswordAuthentication;
import java.util.Properties;

public class TExercicio004 implements EmailService {

        private Session session;

        public JavaMailEmailService() {
            // Configuração do JavaMail
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            this.session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("user@gmail.com", "password");
                }
            });
        }

        @Override
        public void sendEmail(String to, String subject, String body) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("noreply@empresa.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                System.out.println("E-mail enviado via JavaMail para: " + to);
            } catch (MessagingException e) {
                throw new RuntimeException("Erro ao enviar e-mail via JavaMail", e);
            }
        }

        @Override
        public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("noreply@empresa.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);

                // Criação do corpo com anexo
                Multipart multipart = new MimeMultipart();

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(body);
                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentPath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(new File(attachmentPath).getName());
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
                Transport.send(message);
                System.out.println("E-mail com anexo enviado via JavaMail para: " + to);
            } catch (MessagingException e) {
                throw new RuntimeException("Erro ao enviar e-mail com anexo via JavaMail", e);
            }
        }
    }

    // 3. Implementação alternativa usando SendGrid (nova biblioteca)
    public class SendGridEmailService implements EmailService {
        private SendGrid sendGrid;

        public SendGridEmailService(String apiKey) {
            this.sendGrid = new SendGrid(apiKey);
        }

        @Override
        public void sendEmail(String to, String subject, String body) {
            Email from = new Email("noreply@empresa.com");
            Email toEmail = new Email(to);
            Content content = new Content("text/plain", body);

            Mail mail = new Mail(from, subject, toEmail, content);

            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                Response response = sendGrid.api(request);
                System.out.println("E-mail enviado via SendGrid para: " + to);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao enviar e-mail via SendGrid", e);
            }
        }

        @Override
        public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
            Email from = new Email("noreply@empresa.com");
            Email toEmail = new Email(to);
            Content content = new Content("text/plain", body);

            Mail mail = new Mail(from, subject, toEmail, content);

            try {
                // Adicionar anexo
                Attachments attachment = new Attachments();
                byte[] fileContent = Files.readAllBytes(Paths.get(attachmentPath));
                attachment.setContent(Base64.getEncoder().encodeToString(fileContent));
                attachment.setFilename(new File(attachmentPath).getName());
                attachment.setType("application/octet-stream");
                mail.addAttachments(attachment);

                Request request = new Request();
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                Response response = sendGrid.api(request);
                System.out.println("E-mail com anexo enviado via SendGrid para: " + to);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao enviar e-mail com anexo via SendGrid", e);
            }
        }
    }

    // 4. Classe de configuração para injeção de dependência
    @Configuration
    public class EmailConfiguration {

        @Value("${email.provider:javamail}")
        private String emailProvider;

        @Value("${sendgrid.api.key:#{null}}")
        private String sendGridApiKey;

        @Bean
        public EmailService emailService() {
            switch (emailProvider.toLowerCase()) {
                case "sendgrid":
                    if (sendGridApiKey == null) {
                        throw new IllegalStateException("SendGrid API key não configurada");
                    }
                    return new SendGridEmailService(sendGridApiKey);
                case "javamail":
                default:
                    return new JavaMailEmailService();
            }
        }
    }

    // 5. Serviço de negócio que usa o EmailService
    @Service
    public class NotificationService {

        private final EmailService emailService;

        // Injeção de dependência via construtor
        public NotificationService(EmailService emailService) {
            this.emailService = emailService;
        }

        public void enviarBoasVindas(String emailUsuario, String nomeUsuario) {
            String subject = "Bem-vindo ao nosso sistema!";
            String body = String.format("Olá %s,\n\nSeja bem-vindo ao nosso sistema!\n\nAtenciosamente,\nEquipe de Desenvolvimento", nomeUsuario);

            emailService.sendEmail(emailUsuario, subject, body);
        }

        public void enviarRelatorioMensal(String emailGerente, String caminhoRelatorio) {
            String subject = "Relatório Mensal - " + LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"));
            String body = "Segue em anexo o relatório mensal das atividades.";

            emailService.sendEmailWithAttachment(emailGerente, subject, body, caminhoRelatorio);
        }
    }

    // 6. Exemplo de uso
    @RestController
    @RequestMapping("/api/notifications")
    public class NotificationController {

        private final NotificationService notificationService;

        public NotificationController(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        @PostMapping("/welcome")
        public ResponseEntity<String> sendWelcomeEmail(@RequestBody WelcomeRequest request) {
            try {
                notificationService.enviarBoasVindas(request.getEmail(), request.getNome());
                return ResponseEntity.ok("E-mail de boas-vindas enviado com sucesso!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao enviar e-mail: " + e.getMessage());
            }
        }
    }




