package com.food.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.food.service.FoodClaimService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationExpiryScheduler {
	
	private final FoodClaimService foodClaimService;

	private static final long SECOND = 1000L;
	private static final long MINUTE = 60 * SECOND;
	
	@Scheduled(fixedRate = 2 * MINUTE)
	public void expiringReservation() {
		try {
			foodClaimService.cancelExpiredReservation();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
