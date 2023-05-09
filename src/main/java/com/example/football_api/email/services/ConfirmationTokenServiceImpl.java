package com.example.football_api.email.services;

import com.example.football_api.email.entity.ConfirmationToken;
import com.example.football_api.email.repositories.ConfirmationTokenRepository;
import com.example.football_api.exceptions.users.EmailConfirmationException;
import com.example.football_api.entities.users.User;
import com.example.football_api.services.users.AuthService;
import com.example.football_api.services.users.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl {
    private final AuthService authService;
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    @Value(value = "${app.mail.expirationTimeInMin}")
    private int expirationTimeInMin;
    @Value(value = "${app.mail.confrimationLink}")
    private String confrimationLink;
    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken createConfirmationToken(User toUser, long expiresTimeInMinutes){
        String token = UUID.randomUUID().toString();

        return ConfirmationToken
                .builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expiresTimeInMinutes))
                .user(toUser)
                .build();
    }

    public void sendConfirmationEmail(User toUser){
        List<ConfirmationToken> userConfirmationTokens =
                confirmationTokenRepository.findAllByUserId(toUser.getId());

        checkIfAnyTokenAlreadyConfirmed(userConfirmationTokens);

        ConfirmationToken confirmationToken = createConfirmationToken(toUser, expirationTimeInMin);
        saveConfirmationToken(confirmationToken);

        String link = confrimationLink + confirmationToken.getToken();
        emailSenderService.send(toUser.getEmail(), buildEmail(toUser.getFirstName(),
                link));
    }

    public void sendConfirmationEmail(String email){
        User currentUser = userService.findUserByEmail(email);
        sendConfirmationEmail(currentUser);
    }

    private static void checkIfAnyTokenAlreadyConfirmed(List<ConfirmationToken> userConfirmationTokens) {
        if(userConfirmationTokens
                .stream()
                .filter(s->!Objects.isNull(s.getConfirmedAt()))
                .toList().size()>0){
            throw new EmailConfirmationException();
        }
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = findByToken(token);

        validateToken(confirmationToken);

        setConfirmedAt(token);
        authService.enableUser(confirmationToken.getUser().getEmail());

        return "User's email confirmed!";
    }

    private static void validateToken(ConfirmationToken confirmationToken) {
        if(confirmationToken.getConfirmedAt() != null){
            throw new EmailConfirmationException("Email has already been confirmed!");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new EmailConfirmationException("Email confirmation has expired!");
        }
    }

    private ConfirmationToken findByToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new EmailConfirmationException("Email confirmation has expired!"));
        return confirmationToken;
    }

    public void setConfirmedAt(String token){
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now()
        );
    }

    // TODO replace that method to read email body from file
    private String buildEmail(String firstNameOfUser, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + firstNameOfUser + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
