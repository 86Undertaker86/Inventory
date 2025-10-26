package com.CourseWork.Inventory.Service;

import com.CourseWork.Inventory.Model.Location;
import com.CourseWork.Inventory.Repository.LocationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Integer id) {
        return locationRepository.findById(id).orElse(null);
    }

    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    public void deleteLocation(Integer id) {
        locationRepository.deleteById(id);
    }
}