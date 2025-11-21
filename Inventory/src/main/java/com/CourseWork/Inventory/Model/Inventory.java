package com.CourseWork.Inventory.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventory_id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private int quantity;
    private LocalDateTime last_update = LocalDateTime.now();

    public Integer getInventory_id() { return inventory_id; }
    public void setInventory_id(Integer inventory_id) { this.inventory_id = inventory_id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getLast_update() { return last_update; }
    public void setLast_update(LocalDateTime last_update) { this.last_update = last_update; }
}