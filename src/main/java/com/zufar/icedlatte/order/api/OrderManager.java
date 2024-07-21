package com.zufar.icedlatte.order.api;

import com.zufar.icedlatte.openapi.dto.OrderResponseDto;
import com.zufar.icedlatte.openapi.dto.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderManager {

    private final OrderProvider orderProvider;

    public List<OrderResponseDto> getOrders(final List<OrderStatus> statusList) {
        return orderProvider.getOrdersByStatus(statusList);
    }
}
