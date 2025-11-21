package com.CourseWork.Inventory.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supplier_id;

    @Column(nullable = false)
    private String name_company;

    @Column(nullable = false)
    private String full_name;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    public Integer getSupplier_id() { return supplier_id; }
    public void setSupplier_id(Integer supplier_id) { this.supplier_id = supplier_id; }

    public String getName_company() { return name_company; }
    public void setName_company(String name_company) { this.name_company = name_company; }

    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}