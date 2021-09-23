package pe.jay.board.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.jay.board.model.BoardDto;
import pe.jay.board.model.BoardEntity;
import pe.jay.board.repository.BoardRepository;
import pe.jay.member.model.MemberEntity;

import java.util.Optional;

/**
 * 게시판용 서비스
 */
@Service
@Transactional
public class BoardService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final BoardRepository repository;

	public BoardService(BoardRepository boardRepository) {
		this.repository = boardRepository;
	}

	/**
	 * 페이징 객체를 돌려 주는 메서드
	 * 
	 * @param pageable
	 * @return
	 */
	public Page<BoardEntity> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	/**
	 * 게시판 생성 요청
	 * 
	 * @param createRequest
	 * @return
	 */
	public BoardEntity create(BoardDto createRequest) {
		BoardEntity board = createRequest.toEntity();
		return repository.save(board);
	}

	/**
	 * 게시판 엔티티 하나 조회
	 * 
	 * @param id
	 * @return
	 */
	public BoardDto read(Long id) {
		return repository.findById(id).map(BoardDto::new)
				.orElseThrow(() -> new IllegalArgumentException());
	}

	/**
	 * 게시판 수정/삭제 등의 작업 시 권한체크등 처리
	 * 
	 * @param board
	 */
	private void checkMember(BoardEntity board) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			throw new IllegalStateException("User not found");
		}
		MemberEntity member = ((MemberEntity) authentication.getPrincipal());
		if (!member.getUsername().equals(board.getMember().getUsername())) {
			throw new IllegalStateException("Illegal User access");
		}
	}

	/**
	 * 아이디로 게시판 수정용 폼에 넣어질 데이터 생성
	 * 
	 * @param id
	 * @return
	 */
	public BoardDto findById4Edit(Long id) {
		return repository.findById(id).map(board -> {
			checkMember(board);
			return new BoardDto(board.getId(), board.getTitle(), board.getContent());
		}).orElseThrow(() -> new IllegalArgumentException());
	}

	/**
	 * 게시판 수정 처리
	 * 
	 * @param request
	 * @return
	 */
	public BoardEntity update(final BoardDto request) {
		Optional<BoardEntity> optBoard = repository.findById(request.getId());
		if (optBoard.isPresent()) {
			BoardEntity board = optBoard.get();
			checkMember(board);
			board.update(request);
			return repository.save(board);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 게시판 삭제 처리
	 * 
	 * @param id
	 */
	public void deleteById(Long id) {
		Optional<BoardEntity> optBoard = repository.findById(id);
		if (optBoard.isPresent()) {
			checkMember(optBoard.get());
			repository.deleteById(id);
		} else {
			new IllegalArgumentException();
		}
	}
}
