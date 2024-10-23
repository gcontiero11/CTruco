/*
 *  Copyright (C) 2024 Lucas Lazarini and Murilo M. Podenciano
 *  Contact: lazarini <dot> lucas <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: murilo <dot> marques <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.lucasmurilo.m.lazarinipodenciano;

import java.util.List;
import java.util.Optional;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class Akkosocorrompido implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(intel.getOpponentScore()>7){
            return false;
        }
        if (haveHighCardInHand(intel)) {
            return true;   
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (intel.getCards().size() == 0)
            return false;

        //cannot raise
        if (intel.getHandPoints() == 12) {
            return false;
        }
        //can raise
        else {
            if (intel.getCards().size() == 1)
                return Akkosocorrompido.randomBooleanByIntSeed((int) (Math.random()*10));

            if (intel.getCards().size() == 2)
                return intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                &&
                intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
            )
                return true;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
            )
                return true;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
            )
                return false;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
            )
                return false;

            return Akkosocorrompido.randomBooleanByIntSeed((int) (Math.random()*10));
        }
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return switch (getCurrentRoundNumber(intel)) {
            case 1 -> chooseCardFirstRound(intel);
            case 2 -> chooseCardSecondRound(intel);
            case 3 -> chooseCardThirdRound(intel);
            default -> null;
        };
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getCards().size() == 0)
            return -1;

        //cannot raise
        if (intel.getHandPoints() == 12) {
            if (intel.getCards().size() == 1)
                return Akkosocorrompido.randomBooleanByIntSeed((int) (Math.random()*10)) ? 0 : -1;

            if (intel.getCards().size() == 2)
                return (intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                &&
                intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0) ? 0 : -1;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
            )
                return 0;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
            )
                return 0;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
            )
                return -1;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
            )
                return -1;

            return Akkosocorrompido.randomBooleanByIntSeed((int) (Math.random()*10)) ? 0 : -1;
        }
        //can raise
        else {
            if (intel.getCards().size() == 1)
                return Akkosocorrompido.randomBooleanByIntSeed((int) (Math.random()*10)) ? 0 : -1;

            if (intel.getCards().size() == 2)
                return (intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                &&
                intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0) ? 0 : -1;
            
            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) > 0
                )
            )
                return 1;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) > 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) > 0
                )
            )
                return 1;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), intel.getVira()) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), intel.getVira()) < 0
                )
            )
                return -1;

            if (
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
                ||
                (
                    intel.getCards().get(1).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
                ||
                (
                    intel.getCards().get(0).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                    &&
                    intel.getCards().get(2).compareValueTo(intel.getVira(), TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)) < 0
                )
            )
                return -1;

            return Akkosocorrompido.randomBooleanByIntSeed((int) (Math.random()*10)) ? 1 : -1;
        }
    }

    public TrucoCard getLowestCardInHand(GameIntel intel) {
        List<TrucoCard> botCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        TrucoCard lowestCard = botCards.get(0);
        for (TrucoCard card : botCards) {
          if (card.relativeValue(vira)< lowestCard.relativeValue(vira)) {
            lowestCard = card;
          }
        }
        return lowestCard;
      }

      public TrucoCard getHighestCardInHand(GameIntel intel) {
        List<TrucoCard> botCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        TrucoCard highestCard = botCards.get(0);
        for (TrucoCard card : botCards) {
          if (card.relativeValue(vira) > highestCard.relativeValue(vira)) {
            highestCard = card;
          }
        }
        return highestCard;
      }

      private boolean haveHighCardInHand(GameIntel intel) {
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                return true;
            }
            if (card.getRank().toString() == "THREE") {
                return true;
            }
        }
        return false;
    }

    public Optional<TrucoCard> getLowestCardToWin(GameIntel intel){
        List<TrucoCard> botCards = intel.getCards();
        TrucoCard lowestCardToWin = botCards.get(0);
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        
        Optional<TrucoCard> result = Optional.empty();

        if(intel.getOpponentCard().isPresent()){
            TrucoCard presentOpponentCard = opponentCard.orElseThrow();
            for (TrucoCard card : botCards) {
                if (card.relativeValue(vira) > presentOpponentCard.relativeValue(vira)) {
                    if (card.relativeValue(vira) <= lowestCardToWin.relativeValue(vira)) {
                        lowestCardToWin = card;
                        result = Optional.of(lowestCardToWin);
                    }
                }
            }   
        }

        return result;
    }

    public CardToPlay chooseCardFirstRound(GameIntel intel){
        if(intel.getOpponentCard().isPresent()){
            return CardToPlay.of(
                getLowestCardToWin(intel)
                .orElse(getLowestCardInHand(intel))
                );        
        }else{
            if (haveHighCardInHand(intel)) {
                getRaiseResponse(intel);
                return CardToPlay.of(getHighestCardInHand(intel));
            }else{
                return CardToPlay.of(getHighestCardInHand(intel));
            }
        }
    }

    public CardToPlay chooseCardSecondRound(GameIntel intel){
        if(intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){

            getRaiseResponse(intel);

            if(intel.getOpponentCard().isPresent()){
                return CardToPlay.of(getLowestCardToWin(intel).orElse(getLowestCardInHand(intel)));
            }else{
                return CardToPlay.of(getLowestCardInHand(intel));
            }
        }else{
            return CardToPlay.of(getHighestCardInHand(intel));
        }
    }

    public CardToPlay chooseCardThirdRound(GameIntel intel){
        return CardToPlay.of(intel.getCards().get(0));
    }

    public int getCurrentRoundNumber(GameIntel intel){
        return intel.getRoundResults().size()+ 1;
    }

    public static boolean randomBooleanByIntSeed(int seed) {
        return seed >= 5;
    }
}