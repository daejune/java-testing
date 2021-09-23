package pe.jay.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.jay.board.model.BoardEntity;

/**
 * 게시판 리파지토리 인터페이스. 자동으로 스프링에서 CRUD 클래스를 생성해줌.
 */
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
