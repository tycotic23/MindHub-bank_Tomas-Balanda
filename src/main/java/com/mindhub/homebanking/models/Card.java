package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(strategy = "native",name = "native")
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cardholder_id")
    private Client cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private LocalDate thruDate, fromDate;
    private int cvv;

    public Card() {
    }

    public Card(CardType type, CardColor color, String number, LocalDate thruDate, LocalDate fromDate, int cvv) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.cvv = cvv;
    }

    public long getId() {
        return id;
    }

    public Client getCardholder() {
        return cardholder;
    }

    public void setCardholder(Client cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
}
