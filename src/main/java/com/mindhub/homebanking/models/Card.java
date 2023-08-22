package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(strategy = "native",name = "native")
    private long id;

    private String cardholder;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cardholder_id")
    private Client client;
    private CardType type;
    private CardColor color;
    private String number;
    private LocalDate thruDate, fromDate;
    private short cvv;

    public Card() {
    }

    public Card(CardType type, CardColor color, String number, LocalDate fromDate,LocalDate thruDate, short cvv) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
        this.cvv = cvv;
    }

    public Card(CardType type, CardColor color){
        this.type = type;
        this.color = color;
        generateNumber();
        generateCvv();
        fromDate=LocalDate.now();
        thruDate=LocalDate.now().plusYears(5);
    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setClient(Client client) {
        this.client = client;
        this.cardholder=client.getFirstName()+client.getLastName();
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

    public short getCvv() {
        return cvv;
    }

    public void setCvv(short cvv) {
        this.cvv = cvv;
    }

    private void generateCvv(){

        cvv= (short) (Math.random() * 999);
    }

    private void generateNumber(){
        DecimalFormat format=new DecimalFormat("0000");
        String number="";
        for(int i=0;i<4;i++){
            number += format.format((int)(Math.random() * 9999));
            if(i!=3){
                number+="-";
            }
        }
        this.number= number;
    }
}
