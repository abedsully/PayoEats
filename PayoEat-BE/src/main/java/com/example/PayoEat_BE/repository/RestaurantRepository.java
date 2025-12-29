package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.RestaurantManagementData;
import com.example.PayoEat_BE.dto.RestaurantOpenStatusDto;
import com.example.PayoEat_BE.dto.restaurants.CheckUserRestaurantDto;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.request.restaurant.RegisterRestaurantRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RestaurantRepository {
    private final JdbcClient jdbcClient;

    public UUID addRestaurant(RegisterRestaurantRequest request, Long userId, String url1, String url2) {
        try {
            UUID restaurantId = UUID.randomUUID();

            String sql = """
        INSERT INTO restaurant (
            id,
            name,
            rating,
            total_rating,
            description,
            created_at,
            updated_at,
            is_active,
            tax,
            user_id,
            opening_hour,
            closing_hour,
            location,
            telephone_number,
            restaurant_image_url,
            qris_image_url,
            color,
            restaurant_category
        ) VALUES (
            :id, :name, :rating, :total_rating_count, :description,
            :created_at, :updated_at, :is_active, :tax, :user_id,
            :opening_hour, :closing_hour, :location, :telephone_number,
            :restaurant_image_url, :qris_image_url, :color, :restaurant_category
        )
        """;

            jdbcClient.sql(sql)
                    .param("id", restaurantId)
                    .param("name", request.getRestaurantName())
                    .param("rating", 0.0)
                    .param("total_rating_count", 0L)
                    .param("description", request.getDescription())
                    .param("created_at", LocalDateTime.now(ZoneId.of("Asia/Jakarta")))
                    .param("updated_at", null)
                    .param("is_active", false)
                    .param("tax", request.getTax())
                    .param("user_id", userId)
                    .param("opening_hour", request.getOpeningHour())
                    .param("closing_hour", request.getClosingHour())
                    .param("location", request.getLocation())
                    .param("telephone_number", request.getTelephoneNumber())
                    .param("restaurant_image_url", url1)
                    .param("qris_image_url", url2)
                    .param("color", request.getColor())
                    .param("restaurant_category", request.getRestaurantCategory())
                    .update();

            return restaurantId;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    public Optional<CheckUserRestaurantDto> checkUserRestaurant(Long userId) {
        try {
            String sql = """
                    	SELECT r.id, user_id, u.role_id from restaurant r
                    	join users u on r.user_id  = u.id
                    	where u.id = :id
                    """;

            return jdbcClient.sql(sql)
                    .param("id", userId)
                    .query(CheckUserRestaurantDto.class)
                    .optional();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public UUID getRestaurantId(Long userId) {
        try {
            String sql = "SELECT id from restaurant where user_id = :user_id";

            return jdbcClient.sql(sql)
                    .param("user_id", userId)
                    .query(UUID.class)
                    .single();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Boolean> findRestaurantByIdAndIsActiveTrue(UUID restaurantId) {
        try {
            String sql = """
                    select exists(select 1 from restaurant where id = :restaurantId and is_active = true)
                    """;

            return jdbcClient.sql(sql)
                    .param("restaurantId", restaurantId)
                    .query(Boolean.class)
                    .optional();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Long getRestaurantCategory(UUID restaurantId) {
        try {
            String sql = """
                    select restaurant_category from restaurant where id = :id;
                    """;

            return jdbcClient.sql(sql)
                    .param("id", restaurantId)
                    .query(Long.class)
                    .single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Restaurant> getAllRestaurant() {
        String sql = """
        SELECT *
        FROM restaurant
        WHERE is_active = true
    """;

        try (var stream = jdbcClient.sql(sql)
                .query(Restaurant.class)
                .stream()) {

            return stream.toList();
        }
    }


    public List<Restaurant> getSimilarRestaurant(Long categoryCode, UUID restaurantId) {
        try {
            String sql = """
                    select
                    	*
                    from
                    	restaurant
                    where
                    	restaurant_category = :category_code
                    	and is_active = true
                    	and id <> :id
                    """;

            return jdbcClient.sql(sql)
                    .param("category_code", categoryCode)
                    .param("id", restaurantId)
                    .query(Restaurant.class)
                    .list();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Restaurant> getDetail(UUID restaurantId, Boolean isActive) {
        try {
            String sql = """
                    select * from restaurant where id = :restaurantId and is_active = :isActive;
                    """;

            return jdbcClient.sql(sql)
                    .param("restaurantId", restaurantId)
                    .param("isActive", isActive)
                    .query(Restaurant.class)
                    .optional();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean existsByNameAndIsActiveTrue(String name) {
        try {
            String sql = """
                    select exists(select 1 from restaurant where name = :name and is_active = true);
                    """;

            return jdbcClient.sql(sql)
                    .param("name", name)
                    .query(Boolean.class)
                    .single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer addReview(UUID id, Double rating, Long total) {
        try {
            String sql = """
                    UPDATE restaurant set rating = :rating, total_rating = :total
                    where id = :id;
                    """;

            return jdbcClient.sql(sql)
                    .param("id", id)
                    .param("rating", rating)
                    .param("total", total)
                    .update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Restaurant> findByNameContainingIgnoreCase(String name) {
        try {
            String sql = "SELECT * from restaurant where LOWER(name) LIKE LOWER(:name)";

            return jdbcClient.sql(sql)
                    .param("name", "%" + name + "%")
                    .query(Restaurant.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer approveRestaurant(UUID restaurantId) {
        try {
            String sql = """
                    update restaurant set is_active = true where id = :restaurantId;
                    """;

            return jdbcClient.sql(sql)
                    .param("restaurantId", restaurantId)
                    .update();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UUID> getRestaurantUUIDLists() {
        try {
            String sql = """
                    select id from restaurant where is_active = true;
                    """;

            return jdbcClient.sql(sql)
                    .query(UUID.class).list();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public RestaurantOpenStatusDto getRestaurantOpenStatus(UUID restaurantId) {
        try {
            String sql = """
                    select id, opening_hour, closing_hour, is_open from restaurant where id = :restaurant_id;
                    """;

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .query(RestaurantOpenStatusDto.class)
                    .single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Integer setRestaurantIsOpenStatus(UUID restaurantId, Boolean status) {
        try {
            String sql = """
                UPDATE restaurant
                SET is_open = :status
                WHERE id = :restaurant_id
                """;

            return jdbcClient.sql(sql)
                    .param("status", status)
                    .param("restaurant_id", restaurantId)
                    .update();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update restaurant open status: " + e.getMessage());
        }
    }

    public Integer updateOpenStatusForRestaurant(UUID restaurantId) {
        try {
            RestaurantOpenStatusDto restaurant = getRestaurantOpenStatus(restaurantId);

            LocalTime now = LocalTime.now(ZoneId.of("Asia/Jakarta"));
            LocalTime openingTime = restaurant.getOpeningHour();
            LocalTime closingTime = restaurant.getClosingHour();

            boolean shouldBeOpen;

            if (openingTime.isBefore(closingTime)) {
                shouldBeOpen = !now.isBefore(openingTime) && now.isBefore(closingTime);
            } else {
                shouldBeOpen = !now.isBefore(openingTime) || now.isBefore(closingTime);
            }

            if (!Objects.equals(restaurant.getIsOpen(), shouldBeOpen)) {

                setRestaurantIsOpenStatus(restaurantId, shouldBeOpen);

                System.out.println("[Scheduler] Restaurant " + restaurantId +
                        " is now " + (shouldBeOpen ? "OPEN" : "CLOSED"));

            } else {

                System.out.println("Time now: " + LocalTime.now(ZoneId.of("Asia/Jakarta")));

                System.out.println("[Scheduler] Restaurant " + restaurantId +
                        " already " + (restaurant.getIsOpen() ? "OPEN" : "CLOSED"));
            }

            return 1;

        } catch (Exception e) {
            throw new RuntimeException("Failed to update open status: " + e.getMessage());
        }
    }

    public Integer setRestaurantToActive(Long userId) {
        try {
            String sql = """
                    update restaurant set is_active = true where user_id = :user_id
                    """;

            return jdbcClient.sql(sql)
                    .param("user_id", userId)
                    .update();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    };

    public RestaurantManagementData getRestaurantManagementData(UUID restaurantId) {
        try {
            String sql = """
                    select name from restaurant r where r.id = :restaurant_id;
                    """;

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .query(RestaurantManagementData.class)
                    .single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer toggleRestaurantActiveStatus(UUID restaurantId, Boolean isOpen) {
        try {
            String sql = """
                    UPDATE restaurant
                    SET is_open = :is_open,
                        updated_at = :updated_at
                    WHERE id = :restaurant_id
                    """;

            return jdbcClient.sql(sql)
                    .param("is_open", isOpen)
                    .param("updated_at", LocalDateTime.now(ZoneId.of("Asia/Jakarta")))
                    .param("restaurant_id", restaurantId)
                    .update();

        } catch (Exception e) {
            throw new RuntimeException("Failed to toggle restaurant status: " + e.getMessage());
        }
    }

    public Integer updateRestaurant(UpdateRestaurantRequest request, String resImageUrl, String qrisImageUrl) {
        try {
            String sql = """
                    UPDATE restaurant
                    SET name = :name, telephone_number = :number, description = :description, 
                    location = :location, opening_hour = :opening_hour, closing_hour = :closing_hour, tax = :tax,
                    restaurant_image_url = :res_image, qris_image_url = :qris_image, restaurant_category = :category
                    where id = :restaurant_id;
                    """;

            return jdbcClient.sql(sql)
                    .param("name", request.getName())
                    .param("number", request.getTelephoneNumber())
                    .param("description", request.getDescription())
                    .param("location", request.getLocation())
                    .param("opening_hour", request.getOpeningHour())
                    .param("closing_hour", request.getClosingHour())
                    .param("tax", request.getTax())
                    .param("res_image", resImageUrl)
                    .param("qris_image", qrisImageUrl)
                    .param("category", request.getRestaurantCategory())
                    .param("restaurant_id", request.getRestaurantId())
                    .update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Long getRestaurantTax(UUID restaurantId) {
        try {
            String sql = "select tax from restaurant where id = :id";

            return jdbcClient.sql(sql)
                    .param("id", restaurantId)
                    .query(Long.class)
                    .single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
