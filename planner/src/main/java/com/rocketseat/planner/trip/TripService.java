package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    private TripRepository repository;

    @Autowired
    private ParticipantService participantService;

    public TripCreateResponse createTrip(TripRequestPayload payload){
        LocalDateTime startsAt = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endsAt = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);

        if (startsAt.isAfter(endsAt)){
            throw new IllegalArgumentException("The start date must be before the end date.");
        }

        Trip newTrip = new Trip(payload);
        this.repository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

        return new TripCreateResponse(newTrip.getId());
    }

    public Optional<Trip> getTripDetails(UUID id){
        return this.repository.findById(id);
    }

    public Optional<Trip> updateTrip(UUID id, TripRequestPayload payload){
        Optional<Trip> trip = repository.findById(id);

        if(trip.isPresent()){
            LocalDateTime startsAt = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime endsAt = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);

            if (startsAt.isAfter(endsAt)){
                throw new IllegalArgumentException("The start date must be before the end date.");
            }

            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(endsAt);
            rawTrip.setStartsAt(startsAt);
            rawTrip.setDestination(payload.destination());

            this.repository.save(rawTrip);
            return Optional.of(rawTrip);
        }

        return Optional.empty();
    }

    public Trip save(Trip trip) {
        return repository.save(trip);
    }
}
