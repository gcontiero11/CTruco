package com.local.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;

public final class ThirdRoundStrategy implements RoundStrategy {
    private final GameIntel gameIntel;
    private final TrucoCard vira;
    private final ArrayList<TrucoCard> myCards;
    private final int handPower;

    public ThirdRoundStrategy(GameIntel gameIntel) {
        this.gameIntel = gameIntel;
        this.vira = gameIntel.getVira();
        this.myCards = new ArrayList<>(gameIntel.getCards());
        this.handPower = HandUtils.getHandPower(gameIntel, vira);

        myCards.sort((c1, c2) -> c2.compareValueTo(c1, this.vira));
    }

    @Override
    public boolean decideIfRaises() {
        return handPower >= 11;
    }

    @Override
    public CardToPlay chooseCard() {
        final List<TrucoCard> playedCards = gameIntel.getOpenCards();

        if(myCards.size() == 1) return CardToPlay.of(myCards.get(0));
        return HandUtils.getLessCardToWin(myCards, playedCards.get(playedCards.size()-1), vira);
    }

    @Override
    public int getRaiseResponse() {
        if (handPower >= 11) return 1;
        if (handPower > 7) return 0;

        return -1;
    }
}
