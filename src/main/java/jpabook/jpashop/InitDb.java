package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService{
        private final EntityManager em;

        /**
         * userA
         * 	JPA1 BOOK
         * 	JPA2 BOOK
         * userB
         * 	SPRING1 BOOK
         *  SPRING2 BOOK
         */
        public void dbInit1(){

            Member member = createMember("userA", "aaaCity", "aaaStreet", "111");
            em.persist(member);
            Book book1 = createBook("aaa", "usera111", "JPA1", 10000, 10);
            em.persist(book1);
            Book book2 = createBook("bbb", "userb111", "JPA2", 20000, 20);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);

            em.persist(order);

        }

        public void dbInit2(){
            Member member = createMember("userB", "bbbCity", "bbbStreet", "222");
            em.persist(member);
            Book book1 = createBook("ccc", "userc111", "SPRING1", 30000, 30);
            em.persist(book1);
            Book book2 = createBook("ddd", "userd111", "SPRING2", 40000, 40);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);

            em.persist(order);

        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book createBook(String author, String isbn, String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
    }
}
