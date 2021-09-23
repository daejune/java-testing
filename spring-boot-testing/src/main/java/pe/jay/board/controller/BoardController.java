package pe.jay.board.controller;

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
import org.springframework.web.bind.annotation.*;

import pe.jay.board.model.BoardDto;
import pe.jay.board.model.BoardEntity;
import pe.jay.board.service.BoardService;

import javax.validation.Valid;

/**
 * 게시판용 컨트롤러 일반적인 MVC방식으로 동작하게 되는 컨트롤러
 */
@Controller
@RequestMapping("/board")
public class BoardController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private BoardService service;

	/**
	 * 페이지 요청
	 * 
	 * @param pageable : 페이징 정보 객체
	 * @param model    : 뷰에 담기는 모델
	 * @return
	 */
	@GetMapping("/page")
	public String memberPage(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
			Model model) {
		Page<BoardEntity> all = service.findAll(pageable);
		model.addAttribute("page", all);
		return "board/board-list";
	}

	/**
	 * 게시판 글쓰기 폼 페이지
	 * 
	 * @param createRequest 폼 페이지에 모델 생성
	 * @return
	 */
	@GetMapping("/form")
	public String form(@ModelAttribute("form") BoardDto createRequest) {
		return "board/board-form";
	}

	/**
	 * 폼 쓰기 요청
	 * 
	 * @param createRequest : 데이터
	 * @param bindingResult : 에러 바인딩 객체
	 * @param model         : 에러
	 * @return
	 */
	@PostMapping("/form")
	public String postForm(@ModelAttribute("form") @Valid BoardDto createRequest, BindingResult bindingResult,
			Model model) {
		logger.info("Create Board: {}", createRequest.toString());
		if (bindingResult.hasErrors()) {
			model.addAttribute("form", createRequest);
			model.addAttribute("results", bindingResult.getAllErrors());
			return "board/board-form";
		}
		logger.info("Create Board: {}", createRequest.toString());
		service.create(createRequest);
		return "redirect:/board/page";
	}

	/**
	 * 게시판 수정 페이지 접근
	 * 
	 * @param id    : 게시판 아이디
	 * @param model : 뷰에 데이터 담아주는 모델
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		BoardDto request = service.findById4Edit(id);
		model.addAttribute("form", request);
		return "board/board-edit-form";
	}

	/**
	 * 게시판 수정 처리
	 * 
	 * @param request
	 * @param bindingResult : 에러 바인딩 객체
	 * @param model         : 에러있을 시 다시 객체에 데이터 담기 위한 모델
	 * @return
	 */
	@PostMapping("/edit")
	public String editPost(@ModelAttribute("form") @Valid BoardDto request, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("form", request);
			return "board/edit-form";
		}
		service.update(request);
		return "redirect:/board/page";
	}

	/**
	 * 삭제용 페이지
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		service.deleteById(id);
		return "redirect:/board/page";
	}

}
