package org.fedon.bitwise.persistence.repository;

import org.fedon.bitwise.persistence.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dmytro Fedonin
 *
 */
@Repository
public interface MemberDao extends JpaRepository<Member, Long> {
}
