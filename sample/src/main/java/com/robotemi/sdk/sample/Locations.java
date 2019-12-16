package com.robotemi.sdk.sample;
import java.util.Arrays;
import java.util.List;

/*
 * Created by Shubham Jindal and Ivan Reinaldo Ridwan
 */

public class Locations {

    static int currentLocation = 0;

    public class LocationStructure {
        String Name, Description, Image;
        public LocationStructure(String name, String description, String image) {
            Name = name;
            Description = description;
            Image = image;
        }
    }

    public List<LocationStructure> LocationData = Arrays.asList(
            new LocationStructure("Entrance", "We are at the Entrance", "entrance"),
            new LocationStructure("Kitchen", "We have reached the Kitchen", "kitchen"),
            new LocationStructure("Technical Advisor", "We have reached the Technical Advisor", "technical_advisor"),
            new LocationStructure("Workspace", "We have reached the Workspace", "workspace"),
            new LocationStructure("Deakin Launchpad", "We have reached the Deakin Launchpad", "deakin_launchpad"),
            new LocationStructure("Deakin Staff Office", "We have reached the Deakin Staff Office", "deakin_staff_office"),
            new LocationStructure("Open Area", "We have reached the Open Area", "open_area"),
            new LocationStructure("Toilets", "We have reached the Toilets", "toilets"),
            new LocationStructure("Print Area", "We have reached the Print Area", "print_area")


    );
}
