package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class VerificationTokenRepository {
    private final JdbcClient jdbcClient;

    public Optional<VerificationToken> findByTokenAndType(String token, Character type) {
        String sql = """
                select * from verification_token where token = :token and type = :type;
                """;

        return jdbcClient.sql(sql)
                .param("token", token)
                .param("type", type)
                .query(VerificationToken.class)
                .optional();
    }

    public Integer delete(Long id) {
        String sql = """
                delete from verification_token where id = :id;
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }

    public Integer add(VerificationToken request) {
        String sql = """
                INSERT INTO verification_token (token, user_id, expiry_date, type) values (:token, :user_id, :expiry_date, :type);
                """;

        return jdbcClient.sql(sql)
                .param("token", request.getToken())
                .param("user_id", request.getUserId())
                .param("expiry_date", request.getExpiryDate())
                .param("type", request.getType())
                .update();
    }
}
