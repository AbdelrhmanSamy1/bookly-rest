package com.example.bookly.service;

import com.example.bookly.Services.BookService;
import com.example.bookly.Services.OrderService;
import com.example.bookly.dto.request.OrderItemRequestDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    private User user;
    private Book book;
    private OrderRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("ahmed@email.com")
                .role(User.Role.USER)
                .build();

        book = Book.builder()
                .id(1L)
                .title("Palace Walk")
                .price(new BigDecimal("24.99"))
                .stockQuantity(10)
                .build();
        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setBookId(1L);
        itemDto.setQuantity(2);

        requestDto = new OrderRequestDto();
        requestDto.setUserId(1L);
        requestDto.setItems(List.of(itemDto));

    }

    @Nested
    @DisplayName("create()")
    class Create {
        @Test
        @DisplayName("should create order and deduct stock correctly")
        void shouldCreateOrderAndDeductStock() {
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));
            given(bookRepository.save(any())).willReturn(book);
            given(orderRepository.save(any())).willAnswer(inv -> inv.getArguments()[0]);
            given(orderMapper.toDto(any())).willReturn(new OrderResponseDto());

            orderService.create(requestDto);

            then(bookRepository).should().save(argThat(b ->
                    b.getStockQuantity() == 8
            ));
        }

        @Test
        @DisplayName("should calculate total amount correctly")
        void shouldCalculateTotalCorrectly() {
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));
            given(bookRepository.save(any())).willReturn(book);
            given(orderRepository.save(any())).willAnswer(inv -> inv.getArguments()[0]);
            given(orderMapper.toDto(any())).willReturn(new OrderResponseDto());

            orderService.create(requestDto);

            then(orderRepository).should().save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue();

            assertThat(savedOrder.getTotalAmount()).isEqualByComparingTo(new BigDecimal("49.98"));
        }

        @Test
        @DisplayName("should set priceAtPurchase as snapshot of current price")
        void shouldSnapshotPriceAtPurchase() {
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));
            given(bookRepository.save(any())).willReturn(book);
            given(orderRepository.save(any())).willAnswer(inv -> inv.getArguments()[0]);
            given(orderMapper.toDto(any())).willReturn(new OrderResponseDto());

            orderService.create(requestDto);

            then(orderRepository).should().save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue();

            assertThat(savedOrder.getOrderItems().get(0).getPriceAtPurchase())
                    .isEqualByComparingTo(new BigDecimal("24.99"));
        }

        @Test
        @DisplayName("should throw InsufficientStockException when stock is too low")
        void shouldThrowWhenInsufficientStock() {
            book.setStockQuantity(1);

            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));

            assertThatThrownBy(() -> orderService.create(requestDto))
                    .isInstanceOf(InsufficientStockException.class)
                    .hasMessageContaining("Palace Walk")
                    .hasMessageContaining("requested 2")
                    .hasMessageContaining("available 1");
            then(orderRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when user not found")
        void shouldThrowWhenUserNotFound() {
            given(userRepository.findById(99L)).willReturn(Optional.empty());
            requestDto.setUserId(99L);
            assertThatThrownBy(() -> orderService.create(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User");
        }
    }

    @Nested
    @DisplayName("updateStatus()")
    class UpdateStatus {
        @Test
        @DisplayName("should restore stock when order is cancelled")
        void shouldRestoreStockOnCancellation() {
            OrderItem item = OrderItem.builder()
                    .book(book)
                    .quantity(2)
                    .build();

            Order order = Order.builder()
                    .id(1L)
                    .status(Order.OrderStatus.CONFIRMED)
                    .orderItems(List.of(item))
                    .build();

            given(orderRepository.findById(1L)).willReturn(Optional.of(order));
            given(orderRepository.save(any())).willReturn(order);
            given(orderMapper.toDto(any())).willReturn(new OrderResponseDto());

            orderService.updateStatus(1L, Order.OrderStatus.CANCELLED);

            // الـ stock كان 10 — المفروض يرجع 12
            then(bookRepository).should().save(argThat(b ->
                    b.getStockQuantity() == 12
            ));
        }

        @Test
        @DisplayName("should not restore stock when cancelling already cancelled order")
        void shouldNotRestoreStockIfAlreadyCancelled() {
            Order order = Order.builder()
                    .id(1L)
                    .status(Order.OrderStatus.CANCELLED) // ← already cancelled
                    .orderItems(List.of())
                    .build();

            given(orderRepository.findById(1L)).willReturn(Optional.of(order));
            given(orderRepository.save(any())).willReturn(order);
            given(orderMapper.toDto(any())).willReturn(new OrderResponseDto());

            orderService.updateStatus(1L, Order.OrderStatus.CANCELLED);

            // مش المفروض يلمس الـ bookRepository خالص
            then(bookRepository).should(never()).save(any());
        }
    }
}


