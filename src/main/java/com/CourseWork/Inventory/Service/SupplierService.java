package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Supplier;
import com.CourseWork.Inventory.Repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepo;

    public SupplierService(SupplierRepository supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepo.findAll();
    }

    public Supplier getSupplierById(Integer id) {
        return supplierRepo.findById(id).orElse(null);
    }

    public void saveSupplier(Supplier supplier) {
        supplierRepo.save(supplier);
    }

    public void deleteSupplier(Integer id) {
        supplierRepo.deleteById(id);
    }
}