package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.user.UserEntity;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity,Long> {

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);

    // Seleciona por usuario, todas as urls, dessas, apenas as não expiradas
    @Query("SELECT u FROM UrlEntity u WHERE u.user = :user AND (u.expiredAt IS NULL OR u.expiredAt > :now)")
    Page<UrlEntity> findByUser(UserEntity user, LocalDateTime now, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByExpiredAtBefore(LocalDateTime now);
}
