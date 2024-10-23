package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class SlayerBotUtils {

    public List<TrucoCard> getManilhas(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().filter(card -> card.isManilha(vira)).toList();
    }

    public TrucoCard getStrongestCard( List<TrucoCard> cards, TrucoCard vira){
        TrucoCard strongerCard = cards.get(0);

        for (TrucoCard card : cards) {
            if(card.relativeValue(vira) > strongerCard.relativeValue(vira))
                strongerCard = card;
        }

        return strongerCard;
    }

    public TrucoCard getSecondStrongestCard(List<TrucoCard> cards, TrucoCard strongestCard, TrucoCard vira){
        List<TrucoCard> cardsWithoutStrongest = new ArrayList<>(cards);
        cardsWithoutStrongest.remove(strongestCard);
        TrucoCard weakestCard = getWeakestCard(cardsWithoutStrongest, vira);
        cardsWithoutStrongest.remove(weakestCard);
        return cardsWithoutStrongest.get(0);
    }

    public TrucoCard getWeakestCard( List<TrucoCard> cards, TrucoCard vira){
        TrucoCard weakerCard = cards.get(0);

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(weakerCard, vira);
            if (comparison < 0) {
                weakerCard = card;
            }
        }

        return weakerCard;


    }

    public List<TrucoCard> getThreesAtHand(List<TrucoCard> cards) {
        return cards.stream().filter(card -> card.getRank() == CardRank.THREE).toList();
    }


}
