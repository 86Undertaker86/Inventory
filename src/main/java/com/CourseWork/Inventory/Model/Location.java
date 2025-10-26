package com.CourseWork.Inventory.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer location_id;

    @Column(nullable = false)
    private String rack;       // Стелаж

    @Column(nullable = false)
    private String level;      // Поверх або рівень

    @Column(nullable = false)
    private String position;   // Місце

    @Column(nullable = true)
    private String description;

    // Гетери і сетери
    public Integer getLocation_id() { return location_id; }
    public void setLocation_id(Integer location_id) { this.location_id = location_id; }

    public String getRack() { return rack; }
    public void setRack(String rack) { this.rack = rack; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}