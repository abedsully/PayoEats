package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MenuRepository {
    private final JdbcClient jdbcClient;

    public List<Menu> getMenuDetail(List<UUID> menuCodes) {
        try {
            String sql = """
                    select * from menu where menu_code in (:menuCodes)
                    and is_active = true;
                    """;

            return jdbcClient.sql(sql)
                    .param("menuCodes", menuCodes)
                    .query(Menu.class)
                    .stream().toList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<MenuDto> findMenuDtosByCodes(List<UUID> menuCodes) {
        String sql = """
        SELECT m.menu_code, m.menu_name, m.menu_detail, m.menu_price, 
               m.menu_image AS menu_image_id, mi.image_url AS menu_image_url,
               r.id AS restaurant_id, r.name AS restaurant_name
        FROM menu m
        JOIN restaurant r ON m.restaurant_id = r.id
        LEFT JOIN menu_image mi ON m.menu_image = mi.id
        WHERE m.menu_code IN (:menuCodes)
    """;

        return jdbcClient.sql(sql)
                .param("menuCodes", menuCodes)
                .query((rs, rowNum) -> {
                    MenuDto menuDto = new MenuDto();
                    menuDto.setMenuCode(rs.getString("menu_code"));
                    menuDto.setMenuName(rs.getString("menu_name"));
                    menuDto.setMenuDetail(rs.getString("menu_detail"));
                    menuDto.setMenuPrice(rs.getDouble("menu_price"));
                    menuDto.setMenuImage(UUID.fromString(rs.getString("menu_image_id")));
                    menuDto.setMenuImageUrl(rs.getString("menu_image_url"));

                    menuDto.setRestaurantId(UUID.fromString(rs.getString("restaurant_id")));
                    menuDto.setRestaurantName(rs.getString("restaurant_name"));

                    return menuDto;
                }).stream().toList();
    }

    public Optional<Menu> findByMenuCodeAndRestaurantId(UUID menuCode, UUID restaurantId) {
        String sql = """
            SELECT *
            FROM menu
            WHERE menu_code = :menu_code
              AND restaurant_id = :restaurant_id
              AND is_active = TRUE
            LIMIT 1
        """;

        return jdbcClient.sql(sql)
                .param("menu_code", menuCode)
                .param("restaurant_id", restaurantId)
                .query(Menu.class)
                .optional();
    }

    public UUID addMenu(Menu request) {
        try {
            String sql = """
                INSERT INTO menu (
                    menu_name,
                    menu_detail,
                    menu_price,
                    created_at,
                    updated_at,
                    is_active,
                    restaurant_id,
                    menu_image_url
                ) VALUES (
                    :menu_name,
                    :menu_detail,
                    :menu_price,
                    :created_at,
                    null,
                    :is_active,
                    :restaurant_id,
                    :menu_image_url
                )
                RETURNING menu_code;
                """;

            return jdbcClient.sql(sql)
                    .param("menu_name", request.getMenuName())
                    .param("menu_detail", request.getMenuDetail())
                    .param("menu_price", request.getMenuPrice())
                    .param("is_active", request.getIsActive())
                    .param("restaurant_id", request.getRestaurantId())
                    .param("menu_image_url", request.getMenuImageUrl())
                    .query(UUID.class)
                    .single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UUID> addMenus(List<Menu> menuList) {
        try {
            String sql = """
                INSERT INTO menu (
                    menu_code,
                    menu_name,
                    menu_detail,
                    menu_price,
                    created_at,
                    updated_at,
                    is_active,
                    restaurant_id,
                    menu_image_url
                ) VALUES (
                    gen_random_uuid(),
                    :menu_name,
                    :menu_detail,
                    :menu_price,
                    now(),
                    now(),
                    :is_active,
                    :restaurant_id,
                    :menu_image_url
                )
                RETURNING menu_code;
                """;

            List<UUID> newIds = new ArrayList<>();

            for (Menu m : menuList) {
                UUID id = jdbcClient.sql(sql)
                        .param("menu_name", m.getMenuName())
                        .param("menu_detail", m.getMenuDetail())
                        .param("menu_price", m.getMenuPrice())
                        .param("is_active", m.getIsActive() != null ? m.getIsActive() : true)
                        .param("restaurant_id", m.getRestaurantId())
                        .param("menu_image_url", m.getMenuImageUrl())
                        .query(UUID.class)
                        .single();

                newIds.add(id);
            }

            return newIds;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Menu> getMenusByRestaurantId(UUID restaurantId) {
        try {
            String sql = "select * from menu where restaurant_id = :restaurant_id";

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .query(Menu.class)
                    .stream().toList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Menu> findByRestaurantIdAndMenuNameContainingIgnoreCaseAndIsActiveTrue(UUID restaurantId, String name) {
        try {
            String sql = "SELECT * from menu where restaurant_id = :restaurant_id and LOWER(name) LIKE LOWER(:name) " +
                    "and is_active = true";

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .param("name", name)
                    .query(Menu.class)
                    .stream().toList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }





}
