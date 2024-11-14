 package com.kayky.waleska.kwtruco;

import com.bueno.spi.service.BotServiceProvider;
import com.bueno.spi.model.*;
import java.util.List;
import java.util.Optional;

public class KwTruco implements BotServiceProvider {
    private static final List<CardRank> offCards = List.of(CardRank.ACE, CardRank.TWO, CardRank.THREE);
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (oponnentHasZap(intel)){
            return false;
        }
        if(hasZap(intel)){
            return true;
        }
        if (intel.getOpponentScore() == 11){
            return true;
        }

        return hasManilhaAndHighRank(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if (intel.getOpponentScore() >= 9) {
            return false;
        }

        if (oponnentHasZap(intel)) {
            return false;
        }

        if (intel.getOpponentCard().isPresent()) {
            if (getMaxCardValue(intel) <= intel.getOpponentCard().get().relativeValue(intel.getVira())) {
                return false;
            }
        }

        return intel.getOpponentScore() == 0 || intel.getScore() >= intel.getOpponentScore() + 3 || hasHigherThanAverageValue
                (intel) || hasManilhaAndHighRank(intel) || hasZap(intel);
    }

    private int getMaxCardValue(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return cards.stream().mapToInt(card -> card.relativeValue(vira)).max().orElse(0);
    }

    private boolean hasHigherThanAverageValue(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        int handValue = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();

        return handValue >= 18;
    }

    private boolean hasManilhaAndHighRank(GameIntel intel) {
        List<TrucoCard> cartas = intel.getCards();
        TrucoCard vira = intel.getVira();

        boolean hasManilha = false;
        boolean hasCartaHigh = false;


        for (TrucoCard carta : cartas) {
            if (carta.isManilha(vira)) {
                hasManilha= true;
            }
            else if (carta.getRank().value() > 4) {
                hasCartaHigh = true;
            }
        }
        return hasCartaHigh && hasManilha;
    }



    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        for (TrucoCard card : intel.getCards()) {
            if (card.isOuros(intel.getVira())) {
                return CardToPlay.of(card);
            }
        }

        if (intel.getRoundResults().isEmpty()) {
            TrucoCard smallestAttackCard = findLowestAttackCard(intel);
            if (smallestAttackCard != null) {
                return CardToPlay.of(smallestAttackCard);
            }
        }

        TrucoCard smallestCardThatCanWin = selectSmallestCardThatCanWin(intel);

        if (smallestCardThatCanWin == null) {
            smallestCardThatCanWin = findSmallestCardInHand(intel);
        }

        return CardToPlay.of(smallestCardThatCanWin);
    }

    private static TrucoCard findLowestAttackCard(GameIntel intel) {
        List<TrucoCard> attackCards = intel.getCards().stream()
                .filter(card -> offCards.contains(card.getRank()))
                .toList();

        if (attackCards.size() >= 2) {
            return attackCards.stream()
                    .min(TrucoCard::relativeValue)
                    .orElse(null); // Usando orElse para maior clareza
        }

        return null;
    }
    private TrucoCard selectSmallestCardThatCanWin(GameIntel intel) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isEmpty()) {
            return null;
        }
        TrucoCard vira = intel.getVira();
        TrucoCard smallestCardThatCanWin = null;
        for (TrucoCard card : intel.getCards()) {
            if (card.relativeValue(vira) > opponentCard.get().relativeValue(vira)) {
                if (smallestCardThatCanWin == null || card.relativeValue(vira) < smallestCardThatCanWin.relativeValue(vira)) {
                    smallestCardThatCanWin = card;
                }
            }
        }

        return smallestCardThatCanWin;
    }

    private TrucoCard findSmallestCardInHand(GameIntel intel) {
        TrucoCard smallestCard = null;
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            if (smallestCard == null) {
                smallestCard = card;
            }
            if ( card.relativeValue(vira) < smallestCard.relativeValue(vira)){
                smallestCard = card;
            }
        }
        return smallestCard;
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (hasManilha(intel) && has3(intel))
            return 1;
        if(hasZap(intel))
            return 1;
        if (hasManilhaAndHighRank(intel))
            return 1;
        if (hasZapAndTree(intel))
            return 1;
        if (oponnentHasZap(intel))
            return 0;
        if (oponnentHasManilha(intel) && !(hasManilhaAndHighRank(intel)))
            return 0;
        if (oponnentHasManilha( intel) && !(hasManilha(intel)))
            return 0;
        if (oponnentHasManilha(intel) && (hasManilhaAndHighRank(intel)))
            return 1;
        if (oponnentHasManilha(intel) && hasZapAndTree(intel))
            return 1;
        if (intel.getScore() == 9 && intel.getOpponentScore() == 9)
            return 0;
        if (intel.getScore() >= intel.getOpponentScore() && hasManilhaAndHighRank(intel))
            return 1;
        if (intel.getScore() <  intel.getOpponentScore() && hasManilhaAndHighRank(intel)){
            return 1;
        }

        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    public boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isManilha(intel.getVira()));
    }

    public boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }
    public boolean oponnentHasZap(GameIntel intel) {
        return intel.getOpponentCard().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }
    public boolean oponnentHasManilha(GameIntel intel) {
        return  intel.getOpponentCard().stream().anyMatch(card -> card.isManilha(intel.getVira()));
    }
    public boolean has3(GameIntel intel){
        return intel.getCards().stream().anyMatch(card -> card.getRank() == CardRank.THREE);
    }
    public boolean hasZapAndTree(GameIntel intel){
        return has3(intel) && hasZap(intel);
    }

}