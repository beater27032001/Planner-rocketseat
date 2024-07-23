package com.rocketseat.planner.activity;

import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository repository;

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip){

        LocalDateTime occursAt = LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME);
        if (!isWithTripDates(occursAt, trip)){
            throw new IllegalArgumentException("A atividade deve ocorrer durante as datas da viagem.");
        }

        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

        this.repository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId) {
        return this.repository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }

    public boolean isWithTripDates(LocalDateTime activityDate, Trip trip){
        LocalDateTime tripStartDate = trip.getStartsAt();
        LocalDateTime tripEndDate = trip.getEndsAt();

        return activityDate.isAfter(tripStartDate) && activityDate.isBefore(tripEndDate);
    }
}
