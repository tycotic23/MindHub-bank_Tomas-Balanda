package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    private String firstName,lastName,email;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy = "client",fetch=FetchType.EAGER)
    private Set<Account> accounts= new HashSet<>();

    @OneToMany(mappedBy="client",fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans=new HashSet<>();

    public Client(){

    }

    public Client(String firstName,String lastName,String email ){
        this.email=email;
        this.lastName=lastName;
        this.firstName=firstName;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    public List<Loan> getLoans(){
        return clientLoans.stream().map(ClientLoan::getLoan).collect(Collectors.toList());
    }

    public void addLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
}
