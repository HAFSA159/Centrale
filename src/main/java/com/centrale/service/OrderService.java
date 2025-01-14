package com.centrale.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.centrale.model.entity.Client;
import com.centrale.model.entity.Order;
import com.centrale.model.entity.OrderItem;
import com.centrale.model.enums.OrderStatus;
import com.centrale.repository.OrderRepository;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    public List<Order> getOrdersByClient(Client client) {
        return orderRepository.findByClient(client);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getAllOrdersPaginated(int page, int pageSize, String searchTerm) {
        return orderRepository.findAllPaginated(page, pageSize, searchTerm);
    }

    public Order createOrder(Client client, List<OrderItem> cartItems, String shippingAddress, String paymentMethod) {
        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
    
        // Set order items and calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : cartItems) {
            item.setOrder(order);
            total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        order.setOrderItems(cartItems);
        order.setTotal(total);
    
        return saveOrder(order);
    }

    public int getTotalOrderCount(String searchTerm) {
        return orderRepository.getTotalOrderCount(searchTerm);
    }
    public int getTotalOrdersCountByClient(Client client) {
        return orderRepository.getTotalOrderCountByClient(client, null);
    }
    public List<Order> getOrdersByClientPaginated(Client client, int page, int pageSize, String searchTerm) {
        return orderRepository.findByClientPaginated(client, page, pageSize, searchTerm);
    }

    public int getTotalOrderCountByClient(Client client, String searchTerm) {
        return orderRepository.getTotalOrderCountByClient(client, searchTerm);
    }

    public List<Order> getRecentOrdersByClient(Client client, int limit) {
        return orderRepository.findRecentByClient(client, limit);
    }

}
