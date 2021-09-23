package pe.jay.member.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import pe.jay.member.model.MemberDto;
import pe.jay.member.model.MemberEntity;
import pe.jay.member.service.MemberService;

import javax.validation.Valid;

/**
 * 멤버 컨트롤러
 */
@Controller
@RequestMapping("/member")
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

    /**
     * 멤버 페이지
     * @param pageable : 페이징 정보 객체
     * @param model
     * @return
     */
	@GetMapping("/page")
	public String memberPage(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
			Model model) {
		Page<MemberEntity> all = memberService.findAll(pageable);
		model.addAttribute("page", all);
		return "member/member-list";
	}

    /**
     * 멤버 더미 생성 컨트롤러
     * @return
     */
	@GetMapping("/dummy")
	@ResponseBody
	public String dummy() {
		memberService.createDummy();
		return "OK";
	}

    /**
     * 멤버 생성 폼
     * @param MemberDto
     * @return
     */
	@GetMapping("/form")
	public String form(@ModelAttribute("form") MemberDto createRequest) {
		return "member/member-form";
	}

    /**
     * 멤버 쓰기 페이지
     * @param MemberDto
     * @param bindingResult
     * @param model
     * @return
     */
	@PostMapping("/form")
	public String postForm(@Valid MemberDto createRequest, BindingResult bindingResult, Model model) {
		validateMemberRequest(createRequest, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("form", createRequest);
			return "member/member-form";
		}
		memberService.create(createRequest);
		return "redirect:/member/page";
	}

	private void validateMemberRequest(MemberDto request, BindingResult bindingResult) {
		if (memberService.isDuplicate(request.getUsername())) {
			bindingResult.addError(new FieldError("form", "username", "중복된 이메일입니다"));
		}
		if (request.getPassword() != null && !request.getPassword().equals(request.getPasswordConfirm())) {
			bindingResult.addError(new FieldError("form", "password", "비밀번호가 일치하지 않습니다."));
		}
	}

}