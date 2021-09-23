package pe.jay.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import pe.jay.board.model.BoardDto;
import pe.jay.board.model.BoardEntity;
import pe.jay.board.service.BoardService;

import javax.validation.Valid;

/**
 * Board API
 * 게시판에서 REST 하게 요청/응답을 주고 받을 수 있도록 함
 */
@RestController
@RequestMapping("/board")
public class BoardApi {

    @Autowired
    private BoardService service;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<BoardDto> read(@PathVariable Long id) {
        return ResponseEntity.ok(service.read(id));
    }

    @PostMapping("/form/rest")
    public ResponseEntity<BoardDto> post(@RequestBody @Valid BoardDto createRequest,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        BoardEntity board = service.create(createRequest);
        return ResponseEntity.ok(new BoardDto(board));
    }
}
