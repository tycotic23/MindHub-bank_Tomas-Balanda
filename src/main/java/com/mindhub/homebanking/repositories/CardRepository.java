package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
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

    boolean existsByClient_emailAndTypeAndColor(String clientEmail, CardType type, CardColor color);
}
