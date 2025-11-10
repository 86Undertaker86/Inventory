package com.CourseWork.Inventory.Controller;

import com.CourseWork.Inventory.Model.Location;
import com.CourseWork.Inventory.Service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Displays the page with all warehouse locations.
     * Adds a list of locations and an empty Location object for the add form.
     */
    @GetMapping
    public String viewLocations(Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("newLocation", new Location());
        return "LocationPage";
    }

    /**
     * Handles the creation of a new location.
     */
    @PostMapping("/add")
    public String addLocation(@ModelAttribute("newLocation") Location location) {
        locationService.saveLocation(location);
        return "redirect:/manager/locations?success";
    }

    /**
     * Returns a specific location by ID as JSON (for AJAX or dynamic forms).
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public Location getLocationById(@PathVariable("id") Integer id) {
        return locationService.getLocationById(id);
    }

    /**
     * Loads the edit form for a specific location.
     * Also passes the full list of locations for display on the same page.
     */
    @GetMapping("/edit/{id}")
    public String editLocation(@PathVariable Integer id, Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("editLocation", locationService.getLocationById(id));
        return "LocationPage";
    }

    /**
     * Updates an existing location with new data.
     */
    @PostMapping("/update/{id}")
    public String updateLocation(@PathVariable Integer id,
                                 @ModelAttribute("editLocation") Location updated) {
        updated.setLocation_id(id);
        locationService.saveLocation(updated);
        return "redirect:/manager/locations?updated";
    }

    /**
     * Deletes a location by its ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return "redirect:/manager/locations?deleted";
    }
}