package com.pchab.JoParis2024.security.payload.request;

import java.time.LocalDateTime;

import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.SportEnum;

import lombok.Data;

@Data
public class NewEventRequest {
    private String eventTitle;
    private String eventDescription;
    private LocalDateTime eventDate;
    private SportEnum eventSport;
    private String eventStadium;
    private CityEnum eventCity;
    private Integer eventCapacity;
    private Float eventPrice;


    // Getters and Setters
    public String getEventTitle() {
        return eventTitle;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }
    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventSport(SportEnum eventSport) {
        this.eventSport = eventSport;
    }
    public SportEnum getEventSport() {
        return eventSport;
    }

    public void setEventCity(CityEnum eventCity) {
        this.eventCity = eventCity;
    }
    public CityEnum getEventCity() {
        return eventCity;
    }

    public void setEventPrice(Float eventPrice) {
        this.eventPrice = eventPrice;
    }
    public Float getEventPrice() {
        return eventPrice;
    }
    public void setEventCapacity(Integer eventCapacity) {
        this.eventCapacity = eventCapacity;
    }
    public Integer getEventCapacity() {
        return eventCapacity;
    }

    public void setEventStadium(String eventStadium) {
        this.eventStadium = eventStadium;
    }
    public String getEventStadium() {
        return eventStadium;
    }
}
