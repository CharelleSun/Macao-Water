package maintenance.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationService {

    // 存储验证码和过期时间
    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>();

    // 验证码有效期 (5分钟)
    private static final long CODE_EXPIRATION_MINUTES = 5;

    @Value("${app.admin.email}")
    private String adminEmail;

    // 发送验证码到指定邮箱 - 不需要参数
    public void sendVerificationCode() {
        // 生成6位随机验证码
        String code = generateRandomCode();

        // 存储验证码
        verificationCodes.put(code, code);
        log.info("Generated verification code for {}: {}", adminEmail, code);

        // 发送邮件
        sendEmail("维护公告系统验证码", "您的验证码是: " + code);
    }

    // 验证验证码 - 只需要 token 参数
    public boolean verifyCode(String token) {
        String storedCode = verificationCodes.get(token);
        if (storedCode != null && storedCode.equals(token)) {
            // 验证成功后移除验证码
            verificationCodes.remove(token);
            return true;
        }
        return false;
    }

    // 生成6位随机数字验证码
    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // 发送邮件方法
    private void sendEmail(String subject, String content) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("reqToken", "81f7b76a73584871bd6f4b097fe9daa7")
                .addFormDataPart("lbSendFrom", "smtp@macaowater.com")
                .addFormDataPart("lbSendTo", adminEmail)
                .addFormDataPart("lbSubject", subject)
                .addFormDataPart("lbContent", content)
                .addFormDataPart("saveAndSend", "true")
                .addFormDataPart("encryptedData", "YVctitdq+q+s/Yzx9o2K7pFUq8KiFycWQEkV0zuJunHWjLQm9K0+pr8LUZA+g21x9FPrnW0/CycEvxXAtmBKoGxZA5+4cf8DzQgTKNbRkSUdUni9f4cDDFdD+7kaox3vIrPdGLg6j33vnZBrFts/u3vxYa7Ux0LtL3oJ+4ScrlU=")
                .build();

        Request request = new Request.Builder()
                .url("https://testeop.macaowater.com/eop/mssc/email/internship/msgSave")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "testeop.macaowater.com")
                .addHeader("Connection", "keep-alive")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                log.info("Verification email sent to {}", adminEmail);
            } else {
                log.error("Failed to send email. Response: {}", response.body().string());
                throw new RuntimeException("Failed to send verification email");
            }
        } catch (IOException e) {
            log.error("Error sending verification email", e);
            throw new RuntimeException("Error sending verification email", e);
        }
    }
}