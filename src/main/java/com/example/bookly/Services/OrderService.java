package com.example.bookly.Services;

import com.example.bookly.dto.request.OrderRequestDto;
import com.example.bookly.dto.response.OrderResponseDto;
import com.example.bookly.entity.Book;
import com.example.bookly.entity.Order;
import com.example.bookly.entity.OrderItem;
import com.example.bookly.entity.User;
import com.example.bookly.exception.InsufficientStockException;
import com.example.bookly.exception.ResourceNotFoundException;
import com.example.bookly.mapper.OrderMapper;
import com.example.bookly.repository.BookRepository;
import com.example.bookly.repository.OrderRepository;
import com.example.bookly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    public List<OrderResponseDto> findAll(){
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<OrderResponseDto> findByUserId(Long userId){
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User", userId);
        }
        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());

    }
    public OrderResponseDto findById(Long id){
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        return orderMapper.toDto(order);
    }
    public OrderResponseDto create(OrderRequestDto dto){
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.getUserId()));
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (var itemDto : dto.getItems()){
            Book book = bookRepository.findById(itemDto.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book", itemDto.getBookId()));
            if (book.getStockQuantity() < itemDto.getQuantity()){
                throw new InsufficientStockException(
                        book.getTitle(), itemDto.getQuantity(), book.getStockQuantity());
            }
            book.setStockQuantity(book.getStockQuantity() - itemDto.getQuantity());
            bookRepository.save(book);
            OrderItem item = OrderItem.builder()
                    .book(book)
                    .quantity(itemDto.getQuantity())
                    .priceAtPurchase(book.getPrice())
                    .build();
            items.add(item);
            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }
        Order order = Order.builder()
                .user(user)
                .status(Order.OrderStatus.PENDING)
                .totalAmount(total)
                .orderItems(items)
                .build();
        items.forEach(item -> item.setOrder(order));
        return orderMapper.toDto(orderRepository.save(order));
    }
    public OrderResponseDto updateStatus(Long id, Order.OrderStatus newStatus){
        Order order =  orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        if (newStatus == Order.OrderStatus.CANCELLED &&  order.getStatus() != Order.OrderStatus.CANCELLED){
            for (OrderItem item : order.getOrderItems()){
                Book book = item.getBook();
                book.setStockQuantity(book.getStockQuantity() + item.getQuantity());
                bookRepository.save(book);
            }
        }
        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }
    public void delete(Long id){
        if (!orderRepository.existsById(id)){
            throw new ResourceNotFoundException("Order", id);
        }
        orderRepository.deleteById(id);
    }
}
