package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Item;
import com.CourseWork.Inventory.Repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepo;

    public ItemService(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }

    public Item getItemById(Integer id) {
        return itemRepo.findById(id).orElse(null);
    }

    public void saveItem(Item item) {
        itemRepo.save(item);
    }

    public void deleteItem(Integer id) {
        itemRepo.deleteById(id);
    }
}