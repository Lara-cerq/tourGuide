package tourGuide.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.user.User;

@Service
public class GpsUtilService {

	private GpsUtil gpsUtil;

	public GpsUtilService() {
		gpsUtil = new GpsUtil();
	}
//
//	private ExecutorService executor = Executors.newFixedThreadPool(10000);
//
	public List<Attraction> getAttractions() {
		return gpsUtil.getAttractions();
	}
	public VisitedLocation getUserLocation(UUID userId) {
		return gpsUtil.getUserLocation(userId);
	}
//
//	public void submitLocation(User user, TourGuideService tourGuideService ) {
//		CompletableFuture.supplyAsync(() -> {
//			return tourGuideService.getUserLocation(user);
//		}, executor).thenAccept(visitedLocation -> {
//			tourGuideService.finalizeLocation(user, visitedLocation);
//		});
//	}
	
	public void newUserLocation(User user, TourGuideService tourGuideService) {
		ExecutorService executor = Executors.newFixedThreadPool(100000); 
//		for (User user : userList) {
			Runnable runnableTask = () -> {
				tourGuideService.getUserLocation(user);
			};
			executor.execute(runnableTask);
//		}
			System.out.println("///");
		executor.shutdown();
	}

}
