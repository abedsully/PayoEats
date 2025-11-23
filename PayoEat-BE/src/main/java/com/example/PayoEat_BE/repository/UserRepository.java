package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;

    public Long addUser(User request) {
        try {
            String sql = """
                    INSERT INTO users (
                        username,
                        email,
                        role_id,
                        password,
                        created_at,
                        updated_at,
                        confirmation_token,
                        is_active
                    ) VALUES (
                        :username,
                        :email,
                        :role_id,
                        :password,
                        :created_at,
                        :updated_at,
                        :confirmation_token,
                        :is_active
                    )
                    RETURNING id;
                    """;

            return jdbcClient.sql(sql)
                    .param("username", request.getUsername())
                    .param("email", request.getEmail())
                    .param("role_id", request.getRoleId())
                    .param("password", request.getPassword())
                    .param("created_at", request.getCreatedAt())
                    .param("updated_at", request.getUpdatedAt())
                    .param("confirmation_token", request.getConfirmationToken())
                    .param("is_active", request.getIsActive())
                    .query(Long.class)
                    .single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<User> findById(Long userId) {
        try {
            String sql = "select * from users where id = :user_id";

            return jdbcClient.sql(sql)
                    .param("user_id", userId)
                    .query(User.class)
                    .optional();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            String sql = "select * from users where email = :email";

            return jdbcClient.sql(sql)
                    .param("email", email)
                    .query(User.class)
                    .optional();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
