package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne,OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleRepository orderSimpleRepository;
    /**
     * V1. 엔티티 직접 노출
     * - Heibername5Module 등록 , LAZY = null  처리
     * - 양방향 관계 문제 발생 -> @JosnIgore
     */

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환 (fetch join 사용 x)
     * - 단점 : 지연로딩으로 쿼리 N번 호출
     *
     * @return
     */
    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderSimpleQueryDto> collect = orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    /**
     * V3. 엔티티를 조회해서 DTO로 변환 (fetch join O)
     * - fetch join으로 쿼리 1번 호출
     * 참조 : fetch join은 중요함
     * @return
     */
    @GetMapping("/api/v3/simple-orders")
        public Result ordersV3() {
            List<Order> orders = orderRepository.findAllWithMemberDelivery();
            List<OrderSimpleQueryDto> collect = orders.stream()
                    .map(o -> new OrderSimpleQueryDto(o))
                    .collect(Collectors.toList());

            return new Result(collect);
        }

    /**
     * V4. 엔티티를 조회해서 DTO로 변환 (fetch join O)
     * - fetch join으로 쿼리 1번 호출
     * - select 절에서 원하는 데이터만 선택 조회
     * @return
     */
    @GetMapping("/api/v4/simple-orders")
        public Result ordersV4() {
        List<OrderSimpleQueryDto> orderDtos = orderSimpleRepository.findOrderDtos();
            return new Result(orderDtos);
        }
}
