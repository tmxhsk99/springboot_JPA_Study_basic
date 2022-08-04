package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private EntityManager em;
    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void order_ShouldPass() throws Exception {
        //given
        //회원
        Member member = createMember("testUser");

        //상품
        Book book = createBook("JPA", 100, 10000);

        //when
        int orderCount = 5;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야한다", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격을 가격 * 수량이다.", 10000 * 5, getOrder.getTotalPrice());
        assertEquals("주문수량 만큼 재고가 줄어야된다.", 100 - 5, book.getStockQuantity());
    }


    @Test
    public void cancelOrder_ShouldPass() throws Exception {
        //given
        Member member = createMember("testUser");
        Book book = createBook("JPA", 100, 10000);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(),book.getId(),orderCount);
        //when
        orderService.cancelOrder(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL 이여야 한다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야한다", 100, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량_초과() throws Exception {
        //given
        //회원
        Member member = createMember("testUser");
        //상품
        Book book = createBook("JPA", 100, 10000);
        int orderCount = 1001;

        //when
        orderService.order(member.getId(),book.getId(),orderCount);

        //then
        fail("재고 수량 부족 예외가 발생해야한다.");
    }
    private Book createBook(String bookName, int stockQuantity, int price) {
        Book book = new Book();
        book.setName(bookName);
        book.setIsbn("223322");
        book.setStockQuantity(stockQuantity);
        book.setAuthor("김영한");
        book.setPrice(price);
        em.persist(book);
        return book;
    }

    private Member createMember(String testUser) {
        Member member = new Member();
        member.setName(testUser);
        em.persist(member);
        return member;
    }
}