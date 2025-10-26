package com.CourseWork.Inventory.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer item_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private Integer min_level;

    @Column(nullable = true)
    private Integer max_level;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // Гетери і сетери
    public Integer getItem_id() { return item_id; }
    public void setItem_id(Integer item_id) { this.item_id = item_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getMin_level() { return min_level; }
    public void setMin_level(Integer min_level) { this.min_level = min_level; }

    public Integer getMax_level() { return max_level; }
    public void setMax_level(Integer max_level) { this.max_level = max_level; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
}
