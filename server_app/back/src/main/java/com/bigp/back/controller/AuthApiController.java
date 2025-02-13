package com.bigp.back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bigp.back.dto.AdminDTO;
import com.bigp.back.dto.UserDTO;
import com.bigp.back.dto.auth.CheckEmailApiDto;
import com.bigp.back.dto.auth.CheckPassApiDto;
import com.bigp.back.dto.auth.CheckVerificationCodeApiDto;
import com.bigp.back.dto.auth.FindEmailApiDto;
import com.bigp.back.dto.auth.FindPassApiDto;
import com.bigp.back.dto.auth.LoginApiDto;
import com.bigp.back.dto.auth.RegisterApiDto;
import com.bigp.back.dto.auth.SendVerificationCodeApiDto;
import com.bigp.back.service.AdminInfoService;
import com.bigp.back.service.BabyInfoService;
import com.bigp.back.service.ConfigInfoService;
import com.bigp.back.service.MailService;
import com.bigp.back.service.UserInfoService;
import com.bigp.back.service.VerificationCodeService;
import com.bigp.back.utils.CheckUtils;
import com.bigp.back.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequestMapping("api/auth")
@RequiredArgsConstructor
@RestController
public class AuthApiController {
    private final UserInfoService userService;
    private final BabyInfoService babyService;
    private final ConfigInfoService configInfoService;
    private final JwtTokenProvider tokenProvider;
    private final AdminInfoService adminService;
    private final MailService mailService;
    private final VerificationCodeService verificationSerivce;
    private final CheckUtils checkUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginApiDto.RequestBody request) {
        String email = request.getEmail();
        String password = request.getPassword();

        try {
            if (checkUtils.checkQuery(email) || checkUtils.checkQuery(password))
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new LoginApiDto.ErrorResponse(false, "이메일 또는 비밀번호 오류"));
            if (userService.isMatchedUser(email, password)) {
                UserDTO.UserInfo update = new UserDTO.UserInfo();
                String newAccessToken = tokenProvider.createToken(email, true);
                String newRefreshToken = tokenProvider.createToken(email, false);
                update.setAccessToken(newAccessToken);
                update.setRefreshToken(newRefreshToken);
                if (userService.updateUser("email", email, update))
                    return ResponseEntity.ok(new LoginApiDto.SuccessResponse(true, false, newAccessToken, newRefreshToken));
            } else if (adminService.isMatchedAdmin(email, password)) {
                String newAccessToken = tokenProvider.createToken(email, true);
                adminService.updateAdminInfo("email", email, new AdminDTO.AdminInfo(null,null, newAccessToken));
                return ResponseEntity.ok(new LoginApiDto.SuccessAdminResponse(true, true, newAccessToken));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new LoginApiDto.ErrorResponse(false, "이메일 또는 비밀번호 오류"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterApiDto.RequestBody request) {
        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()
            || checkUtils.checkQuery(email) || checkUtils.checkQuery(password) || checkUtils.checkQuery(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RegisterApiDto.ErrorResponse(false, "인자가 정확하지 않습니다"));
        }

        try {
            if (!adminService.isMatchedAdmin(email, password) && userService.insertUser(request)) {
                UserDTO.UserInfo user = userService.getUserInfo("email", email);
                String accessToken = user.getAccessToken();
                String refreshToken = user.getRefreshToken();
                babyService.insertBabyInfo(accessToken);
                configInfoService.insertConfigInfo(accessToken);
                return ResponseEntity.ok(new RegisterApiDto.SuccessResponse(true, accessToken, refreshToken));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RegisterApiDto.ErrorResponse(false, "이미 가입한 회원입니다"));
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RegisterApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(@RequestBody CheckEmailApiDto.RequestBody request) {
        String email = request.getEmail();
        CheckUtils util = new CheckUtils();

        try {
            if (util.checkEmail(email))
                return ResponseEntity.ok(new CheckEmailApiDto.SuccessResponse(true));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CheckEmailApiDto.ErrorResponse(false, "이메일 양식에 맞지 않음"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CheckEmailApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/checkPass")
    public ResponseEntity<?> checkPass(@RequestBody CheckPassApiDto.RequestBody request) {
        String password = request.getPassword();
        CheckUtils util = new CheckUtils();
        int comb = util.checkPassword(password);
        
        try {
            if (comb == 0)
                return ResponseEntity.ok(new CheckPassApiDto.SuccessResponse(true));
            if (comb == 1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CheckPassApiDto.ErrorBadRequestResponse(false, true, false, "양식에 맞지 않는 비밀번호입니다"));
            if (comb == 2)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CheckPassApiDto.ErrorBadRequestResponse(false, false, true, "양식에 맞지 않는 비밀번호입니다"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CheckPassApiDto.ErrorBadRequestResponse(false, false, false, "양식에 맞지 않는 비밀번호입니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CheckPassApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody SendVerificationCodeApiDto.RequestBody request) {
        CheckUtils util = new CheckUtils();
        String code = util.checkVerificationCode();
        String email = request.getEmail();
        String subject = "[나비잠] 안녕하십니까 고객님. 이메일 인증번호 안내입니다.";
        String text = "<h3>나비잠을 사용해주시는 고객님. 대단히 감사합니다.</h3>\n<p>인증번호는 다음과 같습니다.</p>\n<h4>" + code + "</h4>\n<p>나비잠에서 인증번호를 입력해주십시오.</p>";

        try {
            if (mailService.sendMail(email, subject, text)) {
                verificationSerivce.insertVerificationCode(email, code);
                return ResponseEntity.ok(new SendVerificationCodeApiDto.SuccessResponse(true, code));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SendVerificationCodeApiDto.ErrorResponse(false, "다시 한 번 요청해주세요"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SendVerificationCodeApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/checkVerificationCode")
    public ResponseEntity<?> checkVerificationCode(@RequestBody CheckVerificationCodeApiDto.RequestBody request) {
        String email = request.getEmail();
        String code = request.getCode();

        try {
            int flag = verificationSerivce.deleteVerificationCode(email, code);
            if (flag == 0)
                return ResponseEntity.ok(new CheckVerificationCodeApiDto.SuccessResponse(true));
            if (flag == 1)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CheckVerificationCodeApiDto.ErrorResponse(false, "인증번호가 맞지 않습니다"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CheckVerificationCodeApiDto.ErrorResponse(false, "요청하지 않은 사용자입니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CheckVerificationCodeApiDto.ErrorResponse(false, "내부 서버 오류"));  
        }
    }
    
    @GetMapping("/findEmail")
    public ResponseEntity<?> findEmail(@RequestParam String email) {

        if (!checkUtils.checkEmail(email) || checkUtils.checkQuery(email))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FindEmailApiDto.ErrorResponse(false, "잘못된 이메일 형식입니다"));
        
        try {
            if (userService.getUserInfo("email", email) != null)
                return ResponseEntity.ok(new FindEmailApiDto.SuccessResponse(true));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FindEmailApiDto.ErrorResponse(false, "존재하지 않는 이메일입니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FindEmailApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @GetMapping("/findPass")
    public ResponseEntity<?> findPass(@RequestParam String email) {
        try {
            if (checkUtils.checkQuery(email))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new FindPassApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
            UserDTO.UserInfo user = userService.getUserInfo("email", email);
            if (user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new FindPassApiDto.ErrorResponse(false, "존재하지 않는 이메일입니다"));
            email = user.getEmail();
            String password = checkUtils.createPass();
            user.setPassword(password);
            userService.updateUser("email", email, user);
            String subject = "[나비잠] 안녕하십니까 고객님. 임시 비밀번호 안내입니다.";
            String text = "<h3>나비잠을 사용해주시는 고객님. 대단히 감사합니다.</h3>\n<p>임시 비밀번호는 다음과 같습니다.</p>\n<h4>" + password + "</h4>\n<p>임시 비밀번호로 로그인 하신 후, 반드시 비밀번호 변경을 통해 비밀번호를 바꿔주시면 대단히 감사하겠습니다.</p>";
            mailService.sendMail(email, subject, text);
            return ResponseEntity.ok(new FindPassApiDto.SuccessResponse(true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FindPassApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
}
