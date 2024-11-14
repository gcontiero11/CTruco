package com.local.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;


public class SecondRoundStrategy implements RoundStrategy{
    public SecondRoundStrategy(GameIntel intel) {
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(TrucoUtils.isWinning(intel.getScore(),intel.getOpponentScore()) && (TrucoUtils.winFistRound(intel) || TrucoUtils.drewFistRound(intel))) return true;
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return true;
        else if((TrucoUtils.winFistRound(intel) || TrucoUtils.drewFistRound(intel)) &&
                situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return true;
        else return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard selectedCard;

        if (TrucoUtils.theBotPlaysFirst(intel)) {
            selectedCard = TrucoUtils.getGreatestCard(cards, vira);
        }
        else {
            if(!(TrucoUtils.haveStrongestCard(intel, cards).isEmpty())){
                selectedCard = TrucoUtils.getLowestCard(TrucoUtils.haveStrongestCard(intel, cards), vira);
            }
            else selectedCard = TrucoUtils.getLowestCard(cards, vira);
        }

        return CardToPlay.of(selectedCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return 1;
        else if(situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return 0;
        else if(TrucoUtils.winFistRound(intel) || TrucoUtils.drewFistRound(intel)){
            if(TrucoUtils.isWinning(intel.getScore(),intel.getOpponentScore())
               && situation == HandsCardSituation.BLUFF_TO_GET_POINTS) return 0;
            else if(TrucoUtils.isWinning(intel.getScore(),intel.getOpponentScore())
                    && situation == HandsCardSituation.BLUFF_TO_INTIMIDATE) return 0;
            else return -1;
        }
        else return -1;
    }
}
