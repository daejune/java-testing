package pe.jay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 평범한 첫 페이지 인덱스 컨트롤러
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
}
