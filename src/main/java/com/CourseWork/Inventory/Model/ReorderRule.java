package com.CourseWork.Inventory.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "reorder_rule")
public class ReorderRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rule_id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Integer reorder_level;

    @Column(nullable = false)
    private Integer reorder_quantity;

    // Гетери і сетери
    public Integer getRule_id() { return rule_id; }
    public void setRule_id(Integer rule_id) { this.rule_id = rule_id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Integer getReorder_level() { return reorder_level; }
    public void setReorder_level(Integer reorder_level) { this.reorder_level = reorder_level; }

    public Integer getReorder_quantity() { return reorder_quantity; }
    public void setReorder_quantity(Integer reorder_quantity) { this.reorder_quantity = reorder_quantity; }
}