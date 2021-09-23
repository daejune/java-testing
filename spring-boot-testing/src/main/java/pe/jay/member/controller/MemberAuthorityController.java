package pe.jay.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 멤버 권한별 접근 페이지
 */
@Controller
public class MemberAuthorityController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")  
    public String admin() {
        return "member/admin";
    }

    @GetMapping("/userpage")
    public String userpage() {
        return "member/userpage";
    }
}
