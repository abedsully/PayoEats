package com.example.PayoEat_BE.repository;

import com.example.PayoEat_BE.dto.ProgressOrderDto;

import com.example.PayoEat_BE.dto.orders.*;
import com.example.PayoEat_BE.dto.restaurants.TodayRestaurantStatusDto;
import com.example.PayoEat_BE.enums.OrderStatus;
import com.example.PayoEat_BE.enums.PaymentStatus;
import com.example.PayoEat_BE.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final JdbcClient jdbcClient;

    public UUID addOrder(Order newRestaurantOrder) {
        try {
            String sql = """
                    INSERT INTO orders (
                        created_date,
                        order_time,
                        order_message,
                        is_active,
                        order_status,
                        restaurant_id,
                        payment_begin_at,
                        sub_total,
                        total_price,
                        tax_price,
                        cancellation_reason,
                        dine_in_time,
                        payment_image_rejection_reason,
                        payment_image_rejection_count,
                        payment_status
                    ) VALUES (
                        :date,
                        :order_time,
                        :order_message,
                        TRUE,
                        :order_status,
                        :restaurant_id,
                        NULL,
                        :sub_total,
                        :total_price,
                        :tax_price,
                        NULL,
                        NULL,
                        NULL,
                        0,
                        NULL
                    )
                    RETURNING id;
                    """;

            return jdbcClient.sql(sql)
                    .param("date", LocalDate.now())
                    .param("order_time", LocalDateTime.now())
                    .param("order_message", newRestaurantOrder.getOrderMessage())
                    .param("order_status", OrderStatus.RECEIVED.toString())
                    .param("restaurant_id", newRestaurantOrder.getRestaurantId())
                    .param("sub_total", newRestaurantOrder.getSubTotal())
                    .param("total_price", newRestaurantOrder.getTotalPrice())
                    .param("tax_price", newRestaurantOrder.getTaxPrice())
                    .query(UUID.class)
                    .single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<IncomingOrderRow> getIncomingOrderRow(UUID restaurantId) {
        try {
            String sql = """
                select 
                    o.id as order_id,
                    o.order_time,
                    oi.menu_code,
                    oi.quantity,
                    m.menu_name,
                    m.menu_price,
                    m.menu_image_url
                from orders o
                join order_items oi on oi.order_id = o.id
                join menu m on m.menu_code = oi.menu_code and m.is_active = true
                where o.restaurant_id = :restaurant_id
                  and o.is_active = true
                  and o.order_status = :status
                  and o.created_date::date = :date
                order by o.order_time asc
        """;

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .param("date", LocalDate.now())
                    .param("status", OrderStatus.RECEIVED.toString())
                    .query(IncomingOrderRow.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public List<ConfirmedOrderRow> getConfirmedOrderRow(UUID restaurantId) {
        try {
            String sql = """
                select 
                    o.id as order_id,
                    o.order_time,
                    oi.menu_code,
                    oi.quantity,
                    m.menu_name,
                    m.menu_price,
                    m.menu_image_url,
                    o.payment_image_url
                from orders o
                join order_items oi on oi.order_id = o.id
                join menu m on m.menu_code = oi.menu_code and m.is_active = true
                where o.restaurant_id = :restaurant_id
                  and o.is_active = true
                  and o.order_status = :status
                  and o.created_date::date = :date
                order by o.order_time asc
        """;

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .param("date", LocalDate.now())
                    .param("status", OrderStatus.PAYMENT.toString())
                    .query(ConfirmedOrderRow.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public List<ActiveOrderRow> getActiveOrderRows(UUID restaurantId) {
        try {
            String sql = """
                select 
                    o.id as order_id,
                    o.order_time,
                    oi.menu_code,
                    oi.quantity,
                    m.menu_name,
                    m.menu_price,
                    m.menu_image_url,
                    o.dine_in_time
                from orders o
                join order_items oi on oi.order_id = o.id
                join menu m on m.menu_code = oi.menu_code and m.is_active = true
                where o.restaurant_id = :restaurant_id
                  and o.is_active = true
                  and o.order_status in (:statuses)
                  and o.created_date::date = :date
                order by o.order_time asc
        """;

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .param("date", LocalDate.now())
                    .param("statuses", List.of(
                            OrderStatus.ACTIVE.toString(),
                            OrderStatus.CONFIRMED.toString()
                    ))
                    .query(ActiveOrderRow.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public OrderDetailResponseDto getOrderDetails(UUID orderId) {
        try {
            String sql = """
            SELECT 
                o.id AS order_id,
                o.created_date,
                o.order_time,
                o.order_message,
                o.sub_total,
                o.total_price,
                o.tax_price,

                oi.id AS order_item_id,
                oi.menu_code,
                oi.quantity,

                m.menu_name,
                m.menu_price,
                m.menu_image_url AS menu_image_url

            FROM orders o
            JOIN order_items oi ON oi.order_id = o.id
            JOIN menu m ON m.menu_code = oi.menu_code
            WHERE o.id = :orderId
            ORDER BY oi.id
        """;

            List<Map<String, Object>> rows = jdbcClient.sql(sql)
                    .param("orderId", orderId)
                    .query()
                    .listOfRows();

            if (rows.isEmpty()) {
                return null;
            }

            return mapOrderDetail(rows);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private OrderDetailResponseDto mapOrderDetail(List<Map<String, Object>> rows) {
        OrderDetailResponseDto dto = new OrderDetailResponseDto();
        List<OrderItemDetailDto> items = new ArrayList<>();

        for (Map<String, Object> row : rows) {

            if (dto.getOrderId() == null) {
                dto.setOrderId((UUID) row.get("order_id"));
                Object createdDateObj = row.get("created_date");
                if (createdDateObj instanceof java.sql.Date sqlDate) {
                    dto.setCreatedDate(sqlDate.toLocalDate());
                }

                Object orderTimeObj = row.get("order_time");
                if (orderTimeObj instanceof java.sql.Timestamp ts) {
                    dto.setOrderTime(ts.toLocalDateTime());
                }
                dto.setOrderMessage((String) row.get("order_message"));

                Object subTotalObj = row.get("sub_total");
                if (subTotalObj instanceof BigDecimal bd) {
                    dto.setSubTotal(bd.doubleValue());
                } else if (subTotalObj instanceof Double d) {
                    dto.setSubTotal(d);
                }

                Object totalPriceObj = row.get("total_price");
                if (totalPriceObj instanceof BigDecimal bd) {
                    dto.setTotalPrice(bd.doubleValue());
                } else if (totalPriceObj instanceof Double d) {
                    dto.setTotalPrice(d);
                }

                Object taxPriceObj = row.get("tax_price");
                if (taxPriceObj instanceof BigDecimal bd) {
                    dto.setTaxPrice(bd.doubleValue());
                } else if (taxPriceObj instanceof Double d) {
                    dto.setTaxPrice(d);
                }


            }

            OrderItemDetailDto item = new OrderItemDetailDto();
            item.setOrderItemId((UUID) row.get("order_item_id"));
            item.setMenuCode((UUID) row.get("menu_code"));
            item.setQuantity(((Number) row.get("quantity")).longValue());
            item.setMenuName((String) row.get("menu_name"));
            Object menuPriceObj = row.get("menu_price");
            if (menuPriceObj instanceof BigDecimal bd) {
                item.setMenuPrice(bd.doubleValue());
            } else if (menuPriceObj instanceof Double d) {
                item.setMenuPrice(d);
            } else {
                item.setMenuPrice(null);
            }
            item.setMenuImageUrl((String) row.get("menu_image_url"));

            items.add(item);
        }

        dto.setItems(items);
        return dto;
    }

    public TodayRestaurantStatusDto getTodayRestaurantStatus(UUID restaurantId, LocalDate date, List<OrderStatus> orderStatuses, Boolean isActive) {
        try {
            String sql = """
                    select
                        coalesce(sum(o.total_price), 0) as total_price,
                        count(*) as total_count
                    from
                        orders o
                    where
                        o.restaurant_id = :restaurant_id 
                        and DATE(o.created_date) = :date 
                        and o.order_status in (:order_status) 
                        and o.is_active = :is_active 
                    """;

            return jdbcClient.sql(sql)
                    .param("restaurant_id", restaurantId)
                    .param("date", LocalDate.now())
                    .param("order_status",
                            orderStatuses.stream()
                                    .map(OrderStatus::name)
                                    .toList()
                    )
                    .param("is_active", isActive)
                    .query(TodayRestaurantStatusDto.class)
                    .single();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ProgressOrderDto getProgressOrder(UUID orderId) {
        try {
            String sql = "SELECT o.id AS orderId, r.name AS restaurantName, o.total_price AS totalPrice, o.order_status AS orderStatus, " +
                    "o.payment_status as paymentStatus, o.payment_image_rejection_reason AS additionalInfo " +
                    "FROM orders o join restaurant r on o.restaurant_id  = r.id " +
                    "WHERE o.id = :orderId";

            return jdbcClient.sql(sql)
                    .param("orderId", orderId)
                    .query(ProgressOrderDto.class).single();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<CheckOrderDto> checkOrderExistance(UUID orderId) {
        try {
            String sql = "select id, restaurant_id, order_status, payment_image_url, payment_image_rejection_count " +
                    "from orders where id = :order_id";

            return jdbcClient.sql(sql)
                    .param("order_id", orderId)
                    .query(CheckOrderDto.class)
                    .optional();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer finishOrder(UUID orderId) {
        try {
            String sql = """
                    update
                    	orders
                    set
                    	is_active = false,
                    	order_status = :order_status
                    where
                    	id = :order_id
                    """;

            return jdbcClient.sql(sql)
                    .param("order_status", OrderStatus.FINISHED.toString())
                    .param("order_id", orderId)
                    .update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer processOrderToActive(UUID orderId) {
        try {
            String sql = """
                    update
                    	orders
                    set
                        dine_in_time = :dine_in_time,
                    	order_status = :order_status
                    where
                    	id = :order_id
                    """;

            return jdbcClient.sql(sql)
                    .param("dine_in_time", LocalTime.now(ZoneId.of("Asia/Jakarta")))
                    .param("order_status", OrderStatus.ACTIVE.toString())
                    .param("order_id", orderId)
                    .update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer cancelOrder(UUID orderId, String cancellationReason) {
        try {
            String reason = cancellationReason != null ? cancellationReason : "";

            String sql = """
                    update
                    	orders
                    set
                        cancellation_reason = :cancellation_reason,
                    	order_status = :order_status,
                    	is_active = FALSE
                    where
                    	id = :order_id
                    """;

            return jdbcClient.sql(sql)
                    .param("order_status", OrderStatus.CANCELLED.toString())
                    .param("cancellation_reason", reason)
                    .param("order_id", orderId)
                    .update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer rejectOrderPayment(UUID orderId, String rejectionReason, Long count) {
        try {
            String sql = """
                    update
                    	orders
                    set
                        payment_image_rejection_reason = :reason,
                        payment_image_url = null,
                        payment_status = :payment_status,
                        payment_begin_at = :begin_at,
                        payment_image_rejection_count = :count,
                    	order_status = :order_status,
                    where
                    	id = :order_id
                    """;

            return jdbcClient.sql(sql)
                    .param("payment_status", PaymentStatus.PENDING.toString())
                    .param("reason", rejectionReason)
                    .param("begin_at", LocalDateTime.now())
                    .param("count", count)
                    .param("order_status", OrderStatus.PAYMENT.toString())
                    .param("order_id", orderId)
                    .update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer confirmOrderPayment(UUID orderId) {
        try {
            String sql = """
                    update
                    	orders
                    set
                        payment_status = :payment_status,
                    	order_status = :order_status,
                    	is_active = TRUE
                    where
                    	id = :order_id
                    """;

            return jdbcClient.sql(sql)
                    .param("payment_status", PaymentStatus.APPROVED.toString())
                    .param("order_status", OrderStatus.CONFIRMED.toString())
                    .param("order_id", orderId)
                    .update();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer processOrderToPayment(UUID orderId) {
        try {
            String sql = """
                    update
                    	orders
                    set
                        payment_status = :payment_status,
                    	order_status = :order_status,
                    	payment_begin_at = :payment_begin_at
                    where
                    	id = :order_id
                    """;

            return jdbcClient.sql(sql)
                    .param("payment_status", PaymentStatus.PENDING.toString())
                    .param("order_status", OrderStatus.PAYMENT.toString())
                    .param("payment_begin_at", LocalDateTime.now())
                    .param("order_id", orderId)
                    .update();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer addPaymentProof(UUID orderId, String url) {
        try {
            String sql = """
                    update
                    	orders
                    set
                        payment_status = :payment_status,
                        payment_image_url = :url
                    where
                    	id = :order_id
                    """;

            return jdbcClient.sql(sql)
                    .param("url", url)
                    .param("payment_status", PaymentStatus.UPLOADED.toString())
                    .param("order_id", orderId)
                    .update();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public List<UUID> findExpiredOrders(LocalDateTime cutOffTime) {
        String sql = """
        SELECT id FROM orders o
        WHERE DATE(o.created_date) = :date
          AND o.payment_begin_at < :cutOffTime
          AND o.payment_image_url IS NULL
          AND o.order_status = :status
          AND o.is_active = TRUE
          AND o.payment_status = :payment_status
    """;
        try (var stream = jdbcClient.sql(sql)
                .param("date", LocalDate.now())
                .param("cutOffTime", cutOffTime)
                .param("status", OrderStatus.PAYMENT.name())
                .param("payment_status", PaymentStatus.PENDING.name())
                .query(UUID.class)
                .stream()) {

            return stream.toList();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch expired orders", e);
        }
    }

    public void updateOrderStatus(UUID orderId) {
        try {
            String sql = """
            UPDATE orders 
            SET order_status = :order_status,
                cancellation_reason = :reason,
                payment_status = :payment_status,
                is_active = FALSE
            WHERE id = :order_id AND is_active = TRUE
        """;

            jdbcClient.sql(sql)
                    .param("order_status", OrderStatus.CANCELLED.name())
                    .param("payment_status", PaymentStatus.EXPIRED.name())
                    .param("reason", "Payment time expired")
                    .param("order_id", orderId)
                    .update();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
        }
    }

    public boolean checkPayment(UUID orderId) {
        try {
             String sql = """
                     SELECT EXISTS (select 1 FROM orders WHERE id = :order_id and payment_image_url is not null and order_status = 'CONFIRMED')
                     """;

             return jdbcClient.sql(sql)
                     .param("order_id", orderId)
                     .query(Boolean.class)
                     .single();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
