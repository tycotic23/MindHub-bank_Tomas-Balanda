package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardState;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> findAllCard() {
        return cardRepository.findByState(CardState.ENABLED);
    }

    @Override
    public List<CardDTO> getAllCardDTO() {
        return findAllCard().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @Override
    public Card findCardById(long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public CardDTO getCardById(long id) {
        return new CardDTO(findCardById(id));
    }

    @Override
    public Card findCardByNumber(String number) {
        return cardRepository.findByNumber(number).orElse(null);
    }

    @Override
    public CardDTO getCardByNumber(String number) {
        return new CardDTO(findCardByNumber(number));
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card generateCard(CardType cardType, CardColor cardColor) {
        //crear numero de tarjeta unico
        String newCardNumber="";
        do{
            newCardNumber= CardUtils.generateCardNumber();
        }while(cardRepository.findByNumber(newCardNumber).orElse(null)!=null);
        //si se cumplen las condiciones crear entidad Card
        return new Card(newCardNumber,cardType,cardColor);
    }

    public boolean clientHasThatCard(CardType cardType, CardColor cardColor,String emailClient){
        return cardRepository.existsByClient_emailAndTypeAndColorAndState(emailClient,cardType,cardColor,CardState.ENABLED);
    }

    @Override
    public void deleteLogicalCard(String number) {
        Card card= findCardByNumber(number);
        card.setState(CardState.DISABLED);
        saveCard(card);
    }

    @Override
    public boolean cardBelongsToClient(String number, String email) {
        return cardRepository.existsByClient_emailAndNumber(email,number);
    }

    @Override
    public boolean cardExistsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }


}
