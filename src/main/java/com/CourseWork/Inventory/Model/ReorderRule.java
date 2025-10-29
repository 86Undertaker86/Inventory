package com.CourseWork.Inventory.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reorder_rule")
public class ReorderRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rule_id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; // посилання на товар

    @Column(nullable = false)
    private Integer reorder_quantity; // скільки дозамовити

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime last_checked = LocalDateTime.now();

    // --- Гетери і Сетери ---
    public Integer getRule_id() {
        return rule_id;
    }

    public void setRule_id(Integer rule_id) {
        this.rule_id = rule_id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getReorder_quantity() {
        return reorder_quantity;
    }

    public void setReorder_quantity(Integer reorder_quantity) {
        this.reorder_quantity = reorder_quantity;
    }

    public LocalDateTime getLast_checked() {
        return last_checked;
    }

    public void setLast_checked(LocalDateTime last_checked) {
        this.last_checked = last_checked;
    }
}