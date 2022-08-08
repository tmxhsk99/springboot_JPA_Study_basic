package jpabook.jpashop.service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateBookDTO {
    private Long Id;
    private String name;
    private int price;
}
