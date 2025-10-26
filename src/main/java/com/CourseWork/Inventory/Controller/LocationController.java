package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.Location;
import com.CourseWork.Inventory.Service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Сторінка з усіма локаціями
    @GetMapping
    public String viewLocations(Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("newLocation", new Location());
        return "AdminLocation";
    }

    // Додавання нової локації
    @PostMapping("/add")
    public String addLocation(@ModelAttribute("newLocation") Location location) {
        locationService.saveLocation(location);
        return "redirect:/admin/locations?success";
    }

    // Відкрити форму редагування
    @GetMapping("/edit/{id}")
    public String editLocation(@PathVariable Integer id, Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("editLocation", locationService.getLocationById(id));
        return "AdminLocation";
    }

    // Оновлення
    @PostMapping("/update/{id}")
    public String updateLocation(@PathVariable Integer id,
                                 @ModelAttribute("editLocation") Location updated) {
        updated.setLocation_id(id);
        locationService.saveLocation(updated);
        return "redirect:/admin/locations?updated";
    }

    // Видалення
    @GetMapping("/delete/{id}")
    public String deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return "redirect:/admin/locations?deleted";
    }
}