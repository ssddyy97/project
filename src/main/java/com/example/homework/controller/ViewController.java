package com.example.homework.controller;

import com.example.homework.dto.UserDto;
import com.example.homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

    private final UserService userService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        } else {
            model.addAttribute("username", "Guest");
        }
        return "index";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto.SignUpRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserDto.SignUpRequest userDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        logger.info("회원가입 요청 수신: {}", userDto.getUsername());
        if (bindingResult.hasErrors()) {
            logger.warn("회원가입 유효성 검사 오류: {}", bindingResult.getAllErrors());
            return "register";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "passwordMismatch", "비밀번호가 일치하지 않습니다.");
            logger.warn("회원가입 비밀번호 불일치");
            return "register";
        }

        try {
            userService.createUser(userDto);
            logger.info("회원가입 성공: {}", userDto.getUsername());
            redirectAttributes.addFlashAttribute("message", "회원가입이 성공적으로 완료되었습니다!");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            logger.warn("회원가입 실패 (중복): {}", e.getMessage());
            bindingResult.reject("registrationError", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (Exception e) {
            logger.error("회원가입 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/intro")
    public String intro() {
        return "intro";
    }

    

    @GetMapping("/clips")
    public String clips() {
        return "clips";
    }

    

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/sns")
    public String sns() {
        return "sns";
    }

    @GetMapping("/notices")
    public String notices() {
        return "notices";
    }
}
