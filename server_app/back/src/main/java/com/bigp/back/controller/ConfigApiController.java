package com.bigp.back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bigp.back.dto.AdminDTO;
import com.bigp.back.dto.UserDTO;
import com.bigp.back.dto.config.DeleteUserApiDto;
import com.bigp.back.dto.config.GetAliasNameApiDto;
import com.bigp.back.dto.config.GetPersonalInfo;
import com.bigp.back.dto.config.GetProfileImageApiDto;
import com.bigp.back.dto.config.GetSettingInfoApiDto;
import com.bigp.back.dto.config.LogoutApiDto;
import com.bigp.back.dto.config.SetAliasNameApiDto;
import com.bigp.back.dto.config.SetPassApiDto;
import com.bigp.back.dto.config.SetProfileImageApiDto;
import com.bigp.back.dto.config.SetSettingInfoApiDto;
import com.bigp.back.entity.AdminInfo;
import com.bigp.back.entity.BabyInfo;
import com.bigp.back.entity.ConfigInfo;
import com.bigp.back.entity.UserInfo;
import com.bigp.back.service.AESService;
import com.bigp.back.service.AdminInfoService;
import com.bigp.back.service.BabyInfoService;
import com.bigp.back.service.ConfigInfoService;
import com.bigp.back.service.UserInfoService;
import com.bigp.back.utils.CheckUtils;
import com.bigp.back.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/config")
@RequiredArgsConstructor
@RestController
public class ConfigApiController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoService userService;
    private final AdminInfoService adminInfoService;
    private final BabyInfoService babyService;
    private final ConfigInfoService configService;
    private final CheckUtils checkUtils;
    private final AESService aesService;

    @GetMapping("/getSettingInfo")
    public ResponseEntity<?> getSettingInfo(@CookieValue(name="accessToken", required=true) String accessToken) {

        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)) {
                UserInfo user = userService.getEntity("accessToken", accessToken);

                if (user != null) {
                    BabyInfo baby = user.getBabyInfo();
                    ConfigInfo config = user.getConfigInfo();

                    if (baby != null && config != null) {
                        return ResponseEntity.ok(new GetSettingInfoApiDto.SuccessResponse(true, config.isAlarm(), aesService.decryptInfo(baby.getBabyname()), aesService.decryptInfo(baby.getBabybirth()), config.getDataeliminateduration()));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GetSettingInfoApiDto.ErrorResponse(false, "유효하지 않은 접근수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetSettingInfoApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/setSettingInfo")
    public ResponseEntity<?> setSettingInfo(@RequestBody SetSettingInfoApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();
        boolean alarm = request.isAlarm();
        String babyName = request.getBabyName();
        String babyBirth = request.getBabyBirth();
        int dataEliminateDuration = request.getDataEliminateDuration();

        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)
                && !checkUtils.checkQuery(babyName) && !checkUtils.checkQuery(babyBirth)) {
                UserDTO.UserInfo user = userService.getUserInfo("accessToken", accessToken);
                if (user != null) {
                    UserDTO.BabyInfo baby = new UserDTO.BabyInfo();
                    UserDTO.ConfigInfo config = new UserDTO.ConfigInfo();
                    baby.setBabyname(babyName);
                    baby.setBabybirth(babyBirth);
                    config.setAlarm(alarm);
                    config.setDateeliminateduration(dataEliminateDuration);
                    boolean b = babyService.updateBabyInfo(accessToken, baby);
                    boolean c = configService.updateConfigInfo(accessToken, config);
                    if (b && c)
                        return ResponseEntity.ok(new SetSettingInfoApiDto.SuccessResponse(true));
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SetSettingInfoApiDto.ErrorResponse(false, "유효하지 않은 접근수단"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SetSettingInfoApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();

        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)) {
                UserDTO.UserInfo user = userService.getUserInfo("accessToken", accessToken);
                AdminInfo admin = adminInfoService.getAdminInfo(accessToken);
                if (user != null) {
                    user.setAccessToken(accessToken + ".logout");
                    user.setPassword(null);
                    boolean isUpdate = userService.updateUser("accessToken", accessToken, user);

                    if (isUpdate)
                        return ResponseEntity.ok(new LogoutApiDto.SuccessResponse(true));
                } else if (admin != null) {
                    AdminDTO.AdminInfo adminInfo = new AdminDTO.AdminInfo();

                    adminInfo.setAccessToken(accessToken + ".logout");
                    boolean isUpdate = adminInfoService.updateAdminInfo("accessToken", accessToken, adminInfo);

                    if (isUpdate)
                        return ResponseEntity.ok(new LogoutApiDto.SuccessResponse(true));
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LogoutApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LogoutApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();

        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)) {
                UserDTO.UserInfo user = userService.getUserInfo("accessToken", accessToken);

                if (user != null) {
                    if (userService.deleteUser(accessToken))
                        return ResponseEntity.ok(new DeleteUserApiDto.SuccessResponse(true));
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DeleteUserApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DeleteUserApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @GetMapping("/getProfileImage")
    public ResponseEntity<?> getProfileImage(@CookieValue(name="accessToken", required=true) String accessToken) {

        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)) {
                String profileImage = userService.getProfileImage("accessToken", accessToken);

                if (profileImage != null) {
                    return ResponseEntity.ok(new GetProfileImageApiDto.SuccessResponse(true, profileImage));
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GetProfileImageApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetProfileImageApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/setProfileImage")
    public ResponseEntity<?> setProfileImage(@RequestBody SetProfileImageApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();
        String profile = request.getProfileImage();
        int isSuccess = userService.setProfileImage("accessToken", accessToken, profile);

        if (isSuccess == 0)
            return ResponseEntity.ok(new SetProfileImageApiDto.SuccessResponse(true, userService.getProfileImage("accessToken", accessToken)));
        if (isSuccess == 1)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SetProfileImageApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SetProfileImageApiDto.ErrorResponse(false, "내부 서버 오류"));
    }
    
    @GetMapping("/getPersonalInfo")
    public ResponseEntity<?> getPersonalInfo(@CookieValue(name="accessToken", required=true) String accessToken) {
        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)) {
                UserDTO.UserInfo user = userService.getUserInfo("accessToken", accessToken);

                if (user != null)
                    return ResponseEntity.ok(new GetPersonalInfo.SuccessResponse(true, user.getUsername(), user.getEmail()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GetPersonalInfo.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetPersonalInfo.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @GetMapping("/getAliasName")
    public ResponseEntity<?> getAliasName(@CookieValue(name="accessToken", required=true) String accessToken) {
        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)) {
                UserDTO.UserInfo user = userService.getUserInfo("accessToken", accessToken);

                if (user != null)
                    return ResponseEntity.ok(new GetAliasNameApiDto.SuccessResponse(true, user.getAliasname()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GetAliasNameApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetAliasNameApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
    
    @PostMapping("/setAliasName")
    public ResponseEntity<?> setAliasName(@RequestBody SetAliasNameApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();
        String name = request.getName();
        
        try {
            if (jwtTokenProvider.isExpired(accessToken) || !checkUtils.checkQuery(accessToken)
                && !checkUtils.checkQuery(name)) {
                UserDTO.UserInfo userDto = new UserDTO.UserInfo();

                userDto.setAliasname(name);
                boolean isUpdate = userService.updateUser("accessToken", accessToken, userDto);

                if (isUpdate)
                    return ResponseEntity.ok(new SetAliasNameApiDto.SuccessResponse(true));
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new SetAliasNameApiDto.ErrorResponse(false, "존재하지 않는 이용자"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SetAliasNameApiDto.ErrorResponse(false, "유효하지 않은 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SetAliasNameApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }

    @PostMapping("/setPass")
    public ResponseEntity<?> setPass(@RequestBody SetPassApiDto.RequestBody request) {
        String accessToken = request.getAccessToken();
        String password = request.getPassword();
        
        try {
            if (jwtTokenProvider.isExpired(accessToken) && !checkUtils.checkQuery(accessToken)
                && !checkUtils.checkQuery(password)) {
                UserDTO.UserInfo user = new UserDTO.UserInfo();

                user.setPassword(password);
                boolean isUpdate = userService.updateUser("accessToken", accessToken, user);

                if (isUpdate)
                    return ResponseEntity.ok(new SetPassApiDto.SuccessResponse(true));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SetPassApiDto.ErrorResponse(false, "존재하지 않는 이용자"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SetPassApiDto.ErrorResponse(false, "유효하지 않는 인증수단"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SetPassApiDto.ErrorResponse(false, "내부 서버 오류"));
        }
    }
}
