package com.CourseWork.Inventory.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movement")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movement_id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private MovementType movement_type;

    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Integer getMovement_id() { return movement_id; }
    public void setMovement_id(Integer movement_id) { this.movement_id = movement_id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public MovementType getMovement_type() { return movement_type; }
    public void setMovement_type(MovementType movement_type) { this.movement_type = movement_type; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}