package com.CourseWork.Inventory.Repository;

import com.CourseWork.Inventory.Model.ReorderRule;
import com.CourseWork.Inventory.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReorderRuleRepository extends JpaRepository<ReorderRule, Integer> {

    // Знайти правило за товаром
    ReorderRule findByItem(Item item);

    // Отримати стан запасів (з item, supplier, inventory)
    @Query("""
        SELECT i.item_id, i.name, s.name_company, 
               COALESCE(SUM(inv.quantity), 0) AS stock,
               i.min_level
        FROM Item i
        LEFT JOIN i.supplier s
        LEFT JOIN Inventory inv ON inv.item = i
        GROUP BY i.item_id, i.name, s.name_company, i.min_level
        ORDER BY i.item_id
    """)
    List<Object[]> getReorderStatus();
}