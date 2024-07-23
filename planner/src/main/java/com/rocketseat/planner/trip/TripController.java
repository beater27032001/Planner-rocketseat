package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.ActivityData;
import com.rocketseat.planner.activity.ActivityRequestPayload;
import com.rocketseat.planner.activity.ActivityResponse;
import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.link.LinkData;
import com.rocketseat.planner.link.LinkRequestPayload;
import com.rocketseat.planner.link.LinkResponse;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        try {

            TripCreateResponse response = tripService.createTrip(payload);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        try {
            Optional<Trip> trip = tripService.getTripDetails(id);
            return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        try {
            Optional<Trip> updatedTrip = tripService.updateTrip(id, payload);
            return updatedTrip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        try {
            Optional<Trip> trip = this.tripService.getTripDetails(id);

            if (trip.isPresent()) {
                Trip rawTrip = trip.get();
                rawTrip.setIsConfirmed(true);

                this.tripService.save(rawTrip);
                this.participantService.triggerConfirmationEmailToParticipants(id);

                return ResponseEntity.ok(rawTrip);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        try {
            Optional<Trip> trip = this.tripService.getTripDetails(id);

            if (trip.isPresent()) {
                Trip rawTrip = trip.get();

                ParticipantCreateResponse participantResponse = this.participantService.registerParticipantTOEvent(payload.email(), rawTrip);

                if (rawTrip.getIsConfirmed())
                    this.participantService.triggerConfirmationEmailToParticipant(payload.email());


                return ResponseEntity.ok(participantResponse);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        try {
            List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvent(id);

            return ResponseEntity.ok(participantList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> resgisterActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {

        try {
            Optional<Trip> trip = this.tripService.getTripDetails(id);

            if (trip.isPresent()) {
                Trip rawTrip = trip.get();

                ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

                return ResponseEntity.ok(activityResponse);
            }

            return ResponseEntity.notFound().build();

        } catch (
                IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (
                Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        try {
            List<ActivityData> activityData = this.activityService.getAllActivitiesFromId(id);

            return ResponseEntity.ok(activityData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> resgisterLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {

        try {
            Optional<Trip> trip = this.tripService.getTripDetails(id);

            if (trip.isPresent()) {
                Trip rawTrip = trip.get();

                LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

                return ResponseEntity.ok(linkResponse);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {

        try {
            List<LinkData> linkData = this.linkService.getAllLinksFromTrip(id);

            return ResponseEntity.ok(linkData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
