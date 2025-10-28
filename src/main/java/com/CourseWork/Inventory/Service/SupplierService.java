package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Supplier;
import com.CourseWork.Inventory.Repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepo) {
        this.supplierRepository = supplierRepo;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Integer id) {
        return supplierRepository.findById(id).orElse(null);
    }

    public void saveSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    public void deleteSupplier(Integer id) { supplierRepository.deleteById(id);
    }
}