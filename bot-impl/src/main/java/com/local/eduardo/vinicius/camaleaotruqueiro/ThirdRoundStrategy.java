package com.local.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;


public class ThirdRoundStrategy implements RoundStrategy{
    public ThirdRoundStrategy(GameIntel intel) {
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        boolean certainVictory = !TrucoUtils.haveStrongestCard(intel, intel.getCards()).isEmpty();
        if(TrucoUtils.winFistRound(intel) || certainVictory) return true;
        else if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return true;
        else if((TrucoUtils.winFistRound(intel) || TrucoUtils.drewFistRound(intel)) &&
                situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return true;
        else return false;
    }
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard card = TrucoUtils.getGreatestCard(cards,vira);
        return CardToPlay.of(card);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        HandsCardSituation situation = HandsCardSituation.evaluateHandSituation(intel);
        if(situation == HandsCardSituation.ALMOST_ABSOLUTE_VICTORY) return 1;
        else if(situation == HandsCardSituation.ALMOST_CERTAIN_VICTORY) return 0;
        else return -1;
    }
}
