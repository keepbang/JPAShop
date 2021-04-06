package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember("user1", "Seoul", "sanil-lo", "20923");

        Book book = createBook("Spring JPA", "kim", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(getOrder.getStatus(), OrderStatus.ORDER,"상품주문시 상태는 ORDER");
        assertEquals(getOrder.getOrderItems().size(),1,"주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(getOrder.getTotalPrice(),10000*orderCount, "주문 가격은 가격 * 수량이다");
        assertEquals(book.getStockQuantity(),8, "주문 수량만큼 재고가 줄어야한다");
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("user1","Busan","dongu","20192");

        Book book = createBook("Spring JPA 2","chae",10000,10);

        //when
        int orderCount = 11;


        //then
        NotEnoughStockException exception = assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount), "재고수량이 부족하면 예외가 발생한다");
//        assertEquals("재고가 충분하지 않습니다.", exception.getMessage());
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember("user1", "Seoul", "sanil-lo", "20923");

        Book book = createBook("Spring JPA", "kim", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(getOrder.getStatus(), OrderStatus.CANCEL,"주문 취소시 상태는 cancel");
        assertEquals(book.getStockQuantity(),10, "재고가 복구 되어야 한");

    }

    private Book createBook(String name, String author, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        book.setAuthor(author);
        entityManager.persist(book);
        return book;
    }

    private Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();

        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        entityManager.persist(member);
        return member;
    }

}