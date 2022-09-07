package tourGuide.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
	
	@Autowired
	RewardsService rewardsService;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }
    
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions. --> rewardsService.getDistance
        // The reward points for visiting each Attraction. --> rewardsService.getRewardPoints
        //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions") 
    public <T> String getNearbyAttractions(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        List<Map<String, Double>> data = new ArrayList<>();
        List<Map<String, Attraction>> data1 = new ArrayList<>();
        List<Map<String, Integer>> data2 = new ArrayList<>();
        Map<String, Attraction> item1 = new HashMap<>();
        Map<String, Double> item2 = new HashMap<>();
        Map<String, Double> item3 = new HashMap<>();
        Map<String, Double> item4 = new HashMap<>();
        Map<String, Integer> item5 = new HashMap<>();
    	for(Attraction attraction : tourGuideService.getNearByAttractions(visitedLocation) ) {
    		List<Double> attractionsLocation= new ArrayList<>();
    		attractionsLocation.add(tourGuideService.getAttractionLocation(attraction));
    		
    		double userLocation= tourGuideService.getUserLocation(visitedLocation);

    		rewardsService.getDistance(attraction, visitedLocation.location);
    		rewardsService.getRewardPoints(attraction, getUser(userName));
    		
    		item1.put("tourist attraction", attraction);
    		for(Double location : attractionsLocation) {
    			item2.put("Tourist attractions lat/long", location);
    		}
    		item3.put("User location lat/long", userLocation);
    		item4.put("distance between user and attraction", rewardsService.getDistance(attraction, visitedLocation.location));
    		item5.put("reward points", rewardsService.getRewardPoints(attraction, getUser(userName)));
    		data.add(item2);
    		data.add(item3);
    		data.add(item4);
    		data1.add(item1);
    		data2.add(item5);
    		
    		String jsonStringFromObject= JsonStream.serialize(data);
    		jsonStringFromObject= JsonStream.serialize(data1);
    		jsonStringFromObject= JsonStream.serialize(data2);
    		System.out.println(jsonStringFromObject);
    		return jsonStringFromObject;
    		
   
    	}
    	
    	return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }
    
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	// TODO: Get a list of every user's most recent location as JSON
    	//- Note: does not use gpsUtil to query for their current location, 
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371} 
    	//        ...
    	//     }
    	
    	return JsonStream.serialize("");
    }
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
    	return JsonStream.serialize(providers);
    }
    
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}