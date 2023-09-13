package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardState;
import com.mindhub.homebanking.models.CardType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findByClient_email(String email);
    Optional<Card> findByNumber(String number);
    boolean existsByNumber(String number);
    boolean existsByClient_emailAndTypeAndColor(String clientEmail, CardType type, CardColor color);
    boolean existsByClient_emailAndNumber(String email,String number);
    List<Card> findByState(CardState state);
    List<Card> findByClient_emailAndState(String email, CardState state);
    Optional<Card> findByNumberAndState(String number, CardState state);
    boolean existsByClient_emailAndTypeAndColorAndState(String clientEmail, CardType type, CardColor color, CardState state);
}
