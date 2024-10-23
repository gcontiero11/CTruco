/*
 *  Copyright (C) 2024 Victor Lopes de Oliveira - IFSP/SCL and Ariadne de Rezende Tavares- IFSP/SCL
 *  Contact: victor <dot> lopes1 <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: rezende <dot> ariadne <at> aluno <dot> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.adivic.octopus;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;
import java.util.stream.Collectors;

import static com.bueno.spi.model.GameIntel.RoundResult.*;

public class Octopus implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return numberOfStrongCards(intel) > 1 ||
                hasManilha(intel) ||
                (!hasManilha(intel) && intel.getOpponentScore() < 7 && numberOfStrongCards(intel) > 0);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        return !roundResults.isEmpty() && (
                numberOfStrongCards(intel) > 1 ||
                        hasManilha(intel) ||
                        (!hasManilha(intel) && roundResults.get(0) == GameIntel.RoundResult.WON && numberOfStrongCards(intel) > 1) ||
                        (numberOfManilhas(intel) > 1 && roundResults.get(0) == GameIntel.RoundResult.LOST) ||
                        numberOfStrongCards(intel) > 2
        );
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        try {
            switch (intel.getRoundResults().size() + 1) {
                case 1 -> {
                    if(checkIfWeAreFirstToPlay(intel))
                        return chooseFirstRoundPlay(intel);
                    else
                        return CardToPlay.of(chooseBetterCardToWinTheRound(intel));
                }
                case 2 -> {
                    if(checkIfWeAreFirstToPlay(intel))
                        return chooseSecondRoundPlay(intel);
                    else
                        return CardToPlay.of(chooseBetterCardToWinTheRound(intel));
                }
                case 3 -> {
                    return chooseLastRoundPlay(intel);
                }
                default -> {
                    List<TrucoCard> cards = sortCards(intel);
                    return CardToPlay.of(cards.get(cards.size() - 1));
                }
            }
        } catch (Exception e) {
            List<TrucoCard> cards = sortCards(intel);
            return CardToPlay.of(cards.get(cards.size() - 1));
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return (numberOfStrongCards(intel) >= 2 ||
                (numberOfStrongCards(intel) >= 1 && hasSixPointAdvantage(intel))) ? 1 :
                (numberOfStrongCards(intel) >= 1 || hasThreePointAdvantage(intel)) ? 0 :
                        -1;
    }

    public boolean checkIfWeAreFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }
    public List<TrucoCard> caseOneWhenNoneOfTheCardsAreStrong(GameIntel intel){
        List<TrucoCard> playSequence = sortCards(intel);
        Collections.reverse(playSequence);

        return playSequence;
    }

    public CardToPlay cardToPlayLastRound(GameIntel intel){
        return CardToPlay.of(intel.getCards().get(0));
    }

    public List<TrucoCard> caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> cards = sortCards(intel);
        List<TrucoCard> playSequence = new ArrayList<>();
        playSequence.add(cards.get(2));

        if (roundResults.isEmpty())
            playSequence.addAll(List.of(cards.get(1), cards.get(0)));
         else
            playSequence.addAll(roundResults.get(0) == GameIntel.RoundResult.WON
                    ? List.of(cards.get(0), cards.get(1))
                    : List.of(cards.get(1), cards.get(0)));

        return playSequence;
    }

    public List<TrucoCard> caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(GameIntel intel){
        List<TrucoCard> cards = sortCards(intel);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> playSequence = new ArrayList<>();

        playSequence.add(cards.get(1));
        playSequence.add(roundResults.isEmpty() ? cards.get(2) : (roundResults.get(0) == WON ? cards.get(0) : cards.get(2)));
        playSequence.add(roundResults.isEmpty() ? cards.get(0) : (roundResults.get(0) == WON ? cards.get(2) : cards.get(0)));

        return playSequence;
    }

    public List<TrucoCard> caseThreeWhenTwoOfTheCardsAreManilha(GameIntel intel){
        List<TrucoCard> cards = sortCards(intel);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> playSequence = new ArrayList<>();

        playSequence.add(cards.get(2));
        playSequence.add(roundResults.isEmpty() ? cards.get(2) : (roundResults.get(0) == WON ? cards.get(0) : cards.get(1)));
        playSequence.add(roundResults.isEmpty() ? cards.get(0) : (roundResults.get(0) == WON ? cards.get(1) : cards.get(0)));

        return playSequence;
    }

    public List<TrucoCard> caseFourWhenAllOfTheCardsAreStrong(GameIntel intel) {
        List<TrucoCard> cards = sortCards(intel);
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> playSequence = new ArrayList<>();

        playSequence.add(cards.get(2));
        playSequence.add(roundResults.isEmpty() ? cards.get(2) : (roundResults.get(0) == WON ? cards.get(0) : cards.get(1)));
        playSequence.add(roundResults.isEmpty() ? cards.get(0) : (roundResults.get(0) == WON ? cards.get(1) : cards.get(0)));

        return playSequence;
    }

    public CardToPlay cardToPlayFirstRoundWhenZeroStrongCards(GameIntel intel){
        return CardToPlay.of(caseOneWhenNoneOfTheCardsAreStrong(intel).get(0));
    }

    public CardToPlay cardToPlayFirstRoundWhenOneStrongCard(GameIntel intel) {
        return hasManilha(intel)
                ? CardToPlay.of(caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(intel).get(0))
                : CardToPlay.of(caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(intel).get(0));
    }

    public CardToPlay cardToPlayFirstRoundWhenTwoStrongCards(GameIntel intel) {
        return CardToPlay.of(
                numberOfManilhas(intel) == 1 ? caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(intel).get(0) :
                        numberOfManilhas(intel) == 2 ? caseThreeWhenTwoOfTheCardsAreManilha(intel).get(0) :
                                caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(intel).get(0)
        );
    }

    public CardToPlay cardToPlayFirstRoundWhenThreeStrongCards(GameIntel intel){
        return CardToPlay.of(caseFourWhenAllOfTheCardsAreStrong(intel).get(0));
    }

    public CardToPlay cardToPlaySecondRoundWhenZeroStrongCards(GameIntel intel) {
        return CardToPlay.of(caseOneWhenNoneOfTheCardsAreStrong(intel).get(1));
    }

    public CardToPlay cardToPlaySecondRoundWhenOneOrTwoStrongCards(GameIntel intel) {
        return hasManilha(intel)
                ? CardToPlay.of(caseTwoWhenOneOrTwoCardsAreStrongAndIsManilha(intel).get(1))
                : CardToPlay.of(caseTwoWhenOneOrTwoCardsAreStrongAndIsNotManilha(intel).get(1));
    }

    public CardToPlay cardToPlaySecondRoundWhenThreeStrongCards(GameIntel intel) {
        return CardToPlay.of(caseFourWhenAllOfTheCardsAreStrong(intel).get(1));
    }

    public CardToPlay cardToPlayIfOpponentPlayFirst(GameIntel intel){
        TrucoCard card = chooseBetterCardToWinTheRound(intel);
        return CardToPlay.of(card);
    }

    public boolean hasManilha(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return cards.stream()
                .limit(3)
                .anyMatch(card -> card.isManilha(vira));
    }

    public int numberOfManilhas(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if(hasManilha(intel))
            return (int) cards.stream()
                    .filter(card -> card.isManilha(vira))
                    .count();

        return 0;
    }

    public List<TrucoCard> listOfManilhas(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if(hasManilha(intel))
            return cards.stream()
                    .filter(card -> card.getRank() == vira.getRank()
                    .next()).collect(Collectors.toList());

        return Collections.emptyList();
    }

    public boolean hasThree(GameIntel intel){

        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .anyMatch(card -> card.getRank() == CardRank.THREE);
    }

    public int numberOfThreeCards(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        if(hasThree(intel))
            return (int) cards.stream()
                    .filter(card -> card.getRank() == CardRank.THREE)
                    .count();
        return 0;
    }
    public boolean hasTwo(GameIntel intel){

        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .anyMatch(card -> card.getRank() == CardRank.TWO);
    }

    public int numberOfTwoCards(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        if (hasTwo(intel))
            return (int) cards.stream()
                    .filter(card -> card.getRank() == CardRank.TWO)
                    .count();
        return 0;
    }

    public boolean hasThreePointAdvantage(GameIntel intel){
        int scoreDifference = intel.getScore() - intel.getOpponentScore();
        if(scoreDifference >= 3 && scoreDifference <= 6)
            return true;
        return false;
    }

    public boolean hasSixPointAdvantage(GameIntel intel){
        int scoreDifference = intel.getScore() - intel.getOpponentScore();
        if(scoreDifference > 6)
            return true;
        return false;
    }

    public boolean hasAce(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .anyMatch(card -> card.getRank() == CardRank.ACE);
    }

    public int numberOfAceCards(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();

        if (hasAce(intel))
            return (int) cards.stream()
                    .filter(card -> card.getRank() == CardRank.ACE)
                    .count();
        return 0;
    }

    public TrucoCard chooseBetterCardToWinTheRound(GameIntel intel) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        List<TrucoCard> ourCards = sortCards(intel);
        List<TrucoCard> myCardsToWin = new ArrayList<>();
        TrucoCard vira = intel.getVira();

        opponentCard.ifPresent(opponentCards -> {
            myCardsToWin.addAll(
                    ourCards.stream()
                            .filter(card -> card.compareValueTo(opponentCards, vira) > 0)
                            .toList()
            );
        });
        return !myCardsToWin.isEmpty() ? myCardsToWin.get(0) : ourCards.get(0);
    }

    public int numberOfStrongCards(GameIntel intel) {
        int manilhasCount = numberOfManilhas(intel);
        int threeCount = numberOfThreeCards(intel);
        int twoCount = numberOfTwoCards(intel);

        return manilhasCount + threeCount + twoCount;
    }

    public List<TrucoCard> sortCards(GameIntel intel) {
        List<TrucoCard> ourCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return ourCards.stream()
                .sorted(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .collect(Collectors.toList());
    }

    public GameIntel.RoundResult whoWonTheRound(GameIntel intel) {
        List<TrucoCard> ourCards = sortCards(intel);
        TrucoCard vira = intel.getVira();
        TrucoCard opponentCard = intel.getOpponentCard().orElseThrow(() -> new IllegalArgumentException("Opponent card is not present"));

        if (hasManilha(intel) && !opponentCard.equals(vira.getRank().next()))
            return WON;

        if (!hasManilha(intel) && opponentCard.equals(vira.getRank().next()))
            return LOST;

        if (hasManilha(intel) && opponentCard.equals(vira.getRank().next()))
            return DREW;

        TrucoCard bestCard = chooseBetterCardToWinTheRound(intel);
        int comparison = bestCard.compareValueTo(opponentCard, vira);

        if (comparison > 0)
            return WON;
        else if (comparison < 0)
            return LOST;
        else
            return DREW;
    }
    public CardToPlay chooseFirstRoundPlay(GameIntel intel) {
        int strongCards = numberOfStrongCards(intel);

        return switch (strongCards) {
            case 0 -> cardToPlayFirstRoundWhenZeroStrongCards(intel);
            case 1 -> cardToPlayFirstRoundWhenOneStrongCard(intel);
            case 2 -> cardToPlayFirstRoundWhenTwoStrongCards(intel);
            default -> cardToPlayFirstRoundWhenThreeStrongCards(intel);
        };
    }

    public CardToPlay chooseSecondRoundPlay(GameIntel intel){
        int strongCards = numberOfStrongCards(intel);

        return switch (strongCards) {
            case 0 -> cardToPlaySecondRoundWhenZeroStrongCards(intel);
            case 1 -> cardToPlaySecondRoundWhenOneOrTwoStrongCards(intel);
            default -> cardToPlaySecondRoundWhenThreeStrongCards(intel);
        };
    }
    public CardToPlay chooseLastRoundPlay(GameIntel intel){
        return cardToPlayLastRound(intel);
    }
}
