package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem (Item item){
        itemRepository.save(item);
    }
    /**
        영속성 컨텍스트가 자동변경
     */
    @Transactional
    public void updateItem(UpdateBookDTO updateBookDTO){
        Item item = itemRepository.findOne(updateBookDTO.getId());
        item.setName(updateBookDTO.getName());
        item.setPrice(updateBookDTO.getPrice());
    }
    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
