package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.*;

import java.time.LocalDate;

public class CardDTO {

    private long id;

    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private LocalDate thruDate, fromDate;
    private short cvv;

    private CardState state;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.cvv = card.getCvv();
        this.state=card.getState();
    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public short getCvv() {
        return cvv;
    }

    public CardState getState() {
        return state;
    }
}
