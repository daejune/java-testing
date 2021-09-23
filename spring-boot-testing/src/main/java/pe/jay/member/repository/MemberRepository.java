package pe.jay.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.jay.member.model.MemberEntity;

import java.util.Optional;

/**
 * 멤버 리파지토리
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	boolean existsByUsername(String username);

	Optional<MemberEntity> findByUsername(String username);
}
