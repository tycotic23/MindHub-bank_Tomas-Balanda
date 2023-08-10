package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;

    private String name;
    private double MaxAmount;
    @ElementCollection
    private List<Integer> payments=new ArrayList<>();

    @OneToMany(mappedBy = "loan",fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans=new HashSet<>();

    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        MaxAmount = maxAmount;
        this.payments = payments;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return MaxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        MaxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public List<Client> getClients(){
        return clientLoans.stream().map(ClientLoan::getClient).collect(Collectors.toList());
    }

    public void addClient(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
}
