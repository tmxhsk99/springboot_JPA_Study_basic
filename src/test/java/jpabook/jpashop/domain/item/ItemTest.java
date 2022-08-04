package jpabook.jpashop.domain.item;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void addStock_ShouldPass() {
        //given
        Item item = new Book();
        item.setName("JPA");
        item.setStockQuantity(10);
        //when
        item.addStock(100);
        //then
        Assert.assertEquals(item.getStockQuantity(), 110);

    }

    @Test
    public void removeStock_ShouldPass() {
        //given
        Item item = new Book();
        item.setName("JPA");
        item.setStockQuantity(10);
        //when
        item.removeStock(2);
        //then
        Assert.assertEquals(item.getStockQuantity(), 8);

    }
}