package com.miguelestevan.jakaredumatubot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class JakareDuMatuBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(getManilhas(intel.getCards(), intel.getVira()).containsAll(List.of(CardSuit.CLUBS, CardSuit.HEARTS))) return true;

        else if (getManilhas(intel.getCards(), intel.getVira()).size() >= 2) return true;

        else if ((getManilhas(intel.getCards(), intel.getVira()).size() == 1) && hasCardHigherThan(intel, TrucoCard.of(CardRank.KING, CardSuit.SPADES))) return true;

        else if(countCardsHigherThan(intel, CardRank.KING) >= 2) return true;

        return false;

    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if(intel.getHandPoints() == 12){
            return false;
        }

        switch (intel.getRoundResults().size()){
            case 0 -> {
                // First Hand
                if(getManilhas(intel.getCards(), intel.getVira()).containsAll(List.of(CardSuit.CLUBS, CardSuit.HEARTS))){
                    // Hand contains zap and copas
                    return true;
                } else if (getManilhas(intel.getCards(), intel.getVira()).size() == 2 && intel.getScore()-intel.getOpponentScore()>=3) {
                    return true;
                }
            }
            case 1 -> {
                // Second Hand (Estevan)
                //Se tiver feito a primeira e tiver uma manilha
                if(intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON)
                        && (!getManilhas(intel.getCards(), intel.getVira()).isEmpty() || !hasGoodCards(intel.getCards(), intel.getVira()).isEmpty())){
                    return true;
                }

                //Se tiver empatado tiver duas cartas fortes ou uma manilha
                if(intel.getRoundResults().get(0).equals(GameIntel.RoundResult.DREW)
                        && (!getManilhas(intel.getCards(), intel.getVira()).isEmpty() || hasGoodCards(intel.getCards(), intel.getVira()).size() > 1)){
                    return true;
                }
            }
            case 2 -> {
                // Third Hand (Miguel)
                // Se tiver uma manilha pede truco
                if(intel.getCards().get(0).isManilha(intel.getVira())){
                    return true;
                }
                // Se tiver uma carta boa e uma diferença de score de 3 pontos
                if(!hasGoodCards(intel.getCards(), intel.getVira()).isEmpty() && Math.abs(intel.getScore() - intel.getOpponentScore()) > 3){
                    return true;
                }
            }

        }


        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        switch (intel.getRoundResults().size()) {
            case 0 -> {
                // First Hand

                //faz a lista ordenada das cartas
                List<TrucoCard> list = sortedListCards(intel, intel.getVira());

                //se o adversario for o primeiro a jogar
                if (intel.getOpponentCard().isPresent()){
                    //se tiver carta mais forte do que a que ele
                    if (hasCardHigherThan(intel, intel.getOpponentCard().get())){
                        if (list.get(2).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0){
                            return CardToPlay.of(list.get(2));
                        } else if (list.get(1).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0) {
                            return CardToPlay.of(list.get(1));
                        }else{
                            return CardToPlay.of(list.get(0));
                        }
                    }
                    //se conseguir empatar
                    if (list.get(0).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) == 0){
                        return CardToPlay.of(list.get(0));
                    }
                    else if (list.get(1).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) == 0) {
                        return CardToPlay.of(list.get(1));
                    }
                    //se não conseguir empatar, jogar a mais fraca
                    else  return CardToPlay.of(list.get(2));

                }

                // jogar sempre a mais forte na primeira se não tiver zap
                if (list.get(0).isZap(intel.getVira())) return CardToPlay.of(list.get(1));
                return CardToPlay.of(list.get(0));
            }
            case 1->{
                // Second Hand
                // Responder a carta do oponente
                if(intel.getOpponentCard().isPresent()){
                    // Se tiver carta que mata
                    if (hasCardHigherThan(intel, intel.getOpponentCard().get())){
                        List<TrucoCard> list = sortedListCards(intel, intel.getVira());
                        if(list.get(1).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0){
                            return CardToPlay.of(list.get(1));
                        }
                        return CardToPlay.of(list.get(0));
                    }
                    // Se não tiver carta -> retornar a carta mais fraca
                    return CardToPlay.of(sortedListCards(intel, intel.getVira()).get(1));

                }

                List<TrucoCard> sorted = sortedListCards(intel, intel.getVira());
                if(intel.getHandPoints() > 1){
                    // Está trucado
                    if(!getManilhas(intel.getCards(), intel.getVira()).isEmpty()){
                        return CardToPlay.of(sorted.get(0)); // Vai retornar a manilha
                    }
                    return CardToPlay.of(sorted.get(1));
                }

                // Não está trucado
                if(!getManilhas(intel.getCards(), intel.getVira()).isEmpty()){
                    return CardToPlay.of(sorted.get(1)); // Retorna a mais fraca
                }

                return CardToPlay.of(sorted.get(0));
            }
        }
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        switch (intel.getRoundResults().size()) {
            case 0 -> {

                // Mesmo que pediu
                if(getManilhas(intel.getCards(), intel.getVira()).size() >= 2){
                    return 1;
                }
                // First Hand
                if(intel.getOpenCards().size() == 1){

                    // Truco para sair -> Só tem a vira na mesa
                    if(getManilhas(intel.getCards(), intel.getVira()).size() == 1 && !hasGoodCards(intel.getCards(), intel.getVira()).isEmpty()){
                        return 1;
                    }

                    // Duas cartas boas
                    if (hasGoodCards(intel.getCards(), intel.getVira()).size() >= 2){
                        return 0;
                    }
                }
                // Pediu truco na carta do bot
                if(Math.abs(intel.getScore() - intel.getOpponentScore()) >= 5 && hasGoodCards(intel.getCards(), intel.getVira()).size() == 1){
                    return 1;
                }

                return -1;
            }
            case 1 ->{
                //se o bot tiver uma manilha na mão ele aceita o truco
                if(getManilhas(intel.getCards(), intel.getVira()).size() == 1)return 0;
                //se o bot tiver duas manilhas na mão ele aumenta
                if(getManilhas(intel.getCards(), intel.getVira()).size() == 2) return 1;
                //se o bot tiver duas cartas boas ele aceita o truco
                if(!(hasGoodCards(intel.getCards(), intel.getVira()).size() > 1)) return 0;
            }
            case 2 ->{
                //se o bot tiver uma manilha na mão AINDA ele aumenta
                if(!getManilhas(intel.getCards(), intel.getVira()).isEmpty()) return 1;
                //se o bot tiver uma carta boa na mão AINDA ele aceita o truco
                if (!hasGoodCards(intel.getCards(), intel.getVira()).isEmpty()) return 0;
            }
        }

        return 0;
    }

    @Override
    public String getName() {
        return "JakaréDuMatuBóty";
    }
    // The correct way to say is JakaréDuMatuBóty


    // This function returns a list of TrucoCards that are manilas
    private List<CardSuit> getManilhas(List<TrucoCard> botCards, TrucoCard vira) {
        return botCards.stream().filter(trucoCard -> trucoCard.isManilha(vira)).map(TrucoCard::getSuit).toList();
    }

    public boolean hasCardHigherThan(GameIntel intel, TrucoCard trucoCard) {
        return intel.getCards().stream().anyMatch(card -> card.compareValueTo(trucoCard, intel.getVira()) > 0);
    }

    public int countCardsHigherThan(GameIntel intel, CardRank cardRank) {
        return (int) intel.getCards().stream()
                .filter(card -> card.getRank().value() > cardRank.value())
                .count();
    }

    public List<TrucoCard> sortedListCards(GameIntel intel, TrucoCard vira){
//        intel.getCards().sort((o1, o2) -> o1.compareValueTo(o2, vira));
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort(new Comparator<TrucoCard>() {
            @Override
            public int compare(TrucoCard o1, TrucoCard o2) {
                return o1.compareValueTo(o2, vira);
            }
        });
        Collections.reverse(cards);
        return cards;

    }

    // GoodCards -> a,2,3 not been a manilha
    public List<TrucoCard> hasGoodCards(List<TrucoCard> trucoCards, TrucoCard vira){
        return trucoCards.stream()
                .filter(trucoCard -> !trucoCard.isManilha(vira))
                .filter(trucoCard -> trucoCard.getRank() == CardRank.ACE || trucoCard.getRank() == CardRank.TWO || trucoCard.getRank() == CardRank.THREE)
                .toList();
    }

}
