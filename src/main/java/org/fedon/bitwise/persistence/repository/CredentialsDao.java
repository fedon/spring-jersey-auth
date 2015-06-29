package org.fedon.bitwise.persistence.repository;

import org.fedon.bitwise.persistence.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Dmytro Fedonin
 *
 */
@Repository
public interface CredentialsDao extends JpaRepository<Credentials, Long> {
    @Query("select c from Credentials c where c.username = :username")
    Credentials oneByUsername(@Param("username") String username);
}
