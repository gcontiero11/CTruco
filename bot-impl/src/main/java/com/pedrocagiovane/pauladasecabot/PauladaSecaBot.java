/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: gioguima24 <at> gmail <dot> com or
 *  pedrodosreis1996 <at> gmail <dot> com
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

package com.pedrocagiovane.pauladasecabot;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;


public class PauladaSecaBot implements BotServiceProvider {

    private boolean temCasalMaior(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isZap(vira) || card.isCopas(vira)){
                contador ++;
            }
        }
        if(contador == 2){
            return true;
        }
        return false;
    }

    private boolean temCasalPreto(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isZap(vira) || card.isEspadilha(vira)){
                contador ++;
            }
        }
        if(contador == 2){
            return true;
        }
        return false;
    }

    private boolean temCasalVermelho(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isCopas(vira) || card.isOuros(vira)){
                contador ++;
            }
        }
        if(contador == 2){
            return true;
        }
        return false;
    }

    private boolean temCasalMenor(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isEspadilha(vira) || card.isOuros(vira)){
                contador ++;
            }
        }
        if(contador == 2){
            return true;
        }
        return false;
    }

    private TrucoCard piorCarta(GameIntel build) {
        Integer menor = 1000;
        TrucoCard cartaMenor = null;

        for (TrucoCard carta : build.getCards()) {
            if (carta.isManilha(build.getVira())) {
                if (carta.relativeValue(build.getVira()) < menor) {
                    menor = carta.relativeValue(build.getVira());
                    cartaMenor = carta;
                }
            } else {
                if (carta.getRank().value() < menor) {
                    menor = carta.getRank().value();
                    cartaMenor = carta;
                }
            }
        }
        return cartaMenor;
    }

    private TrucoCard melhorCarta(GameIntel build) {
        Integer maior = 0;
        TrucoCard cartaMaior = null;
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() > maior) {
                maior = carta.getRank().value();
                cartaMaior = carta;
            }
        }
        return cartaMaior;
    }

    private TrucoCard cartaMedia(GameIntel build) {
        TrucoCard cartaMedia = null;
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() != melhorCarta(build).getRank().value() && carta.getRank().value() != piorCarta(build).getRank().value()) {
                cartaMedia = carta;
            }
        }
        return cartaMedia;
    }

    private int contManilha(List<TrucoCard> cartas, TrucoCard vira){
        int cont = 0;
        for(TrucoCard card : cartas){
            if(card.isManilha(vira)){
                cont++;
            }
        }
        return cont;
    }

    private Boolean temZap(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.isZap(build.getVira()))
                return true;
        }
        return false;
    }

    private Boolean temCopas(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.isCopas(build.getVira()))
                return true;
        }
        return false;
    }

    private Boolean temOuros(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.isOuros(build.getVira())) {
                return true;
            }
        }
        return false;
    }

    private Boolean temEspada(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.isEspadilha(build.getVira())) {
                return true;
            }
        }
        return false;
    }

    private List<TrucoCard> melhoresCartas(GameIntel build){
        List<TrucoCard> maioresCartas = new ArrayList<>();
        for (TrucoCard trucoCard : build.getCards()) {
            if (trucoCard.getRank().value() > build.getOpponentCard().get().getRank().value()) {
                maioresCartas.add(trucoCard);
            }
        }
        return maioresCartas;
    }

    private TrucoCard matarCartaComMenor(GameIntel build) {
        TrucoCard trucoCard = null;
        List<TrucoCard> maioresCartas = melhoresCartas(build);
        if (!maioresCartas.isEmpty()) {
            trucoCard = maioresCartas.get(0);
            for (TrucoCard carta : maioresCartas) {
                if (carta.getRank().value() < trucoCard.getRank().value()) {
                    trucoCard = carta;
                }
            }
        } else {
            trucoCard = piorCarta(build);
        }
        return trucoCard;
    }

    private List<TrucoCard> manilhasOponente(GameIntel build){
        List<TrucoCard> manilhas = new ArrayList<>();
        for (TrucoCard carta : build.getCards()) {
            if (carta.isManilha(build.getVira())) {
                if (carta.relativeValue(build.getVira()) > build.getOpponentCard().get().relativeValue(build.getVira())){
                    manilhas.add(carta);
                }
            }
        }
        return manilhas;
    }

    private TrucoCard matarManilha(GameIntel build) {
        List<TrucoCard> manilhas = manilhasOponente(build);
        TrucoCard cardPlay = piorCarta(build);
        if (!manilhas.isEmpty()) {
            cardPlay = manilhas.get(0);
            for (TrucoCard manilha : manilhas) {
                if (manilha.relativeValue(build.getVira()) < cardPlay.relativeValue(build.getVira())){
                    cardPlay = manilha;
                }
            }
        }
        return cardPlay;
    }

    private Boolean temTres(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() == 10) {
                return true;
            }
        }
        return false;
    }

    private int contTres(GameIntel build) {
        int contadorTres = 0;
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() == 10) {
                contadorTres++;
            }
        }
        return contadorTres;
    }
    private int contDois(GameIntel build) {
        int contadorDois = 0;
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() == 9) {
                contadorDois++;
            }
        }
        return contadorDois;
    }


    private Boolean temDois(GameIntel build){
        for (TrucoCard carta : build.getCards()) {
            if (carta.getRank().value() == 9) {
                return true;
            }
        }
        return false;
    }

    private int valorMao(GameIntel build) {
        int mao = 0;
        for (TrucoCard carta : build.getCards()) {
            if(carta.isManilha(build.getVira())){
                mao += carta.relativeValue(build.getVira());
            } else {
                mao += carta.getRank().value();
            }
        }
        return mao;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {

        //ACEITA SE TIVER DUAS MANILHAS
        if(contManilha(intel.getCards(), intel.getVira()) >= 2){

            return true;
        }

        //ACEITA SE TIVER DOIS TRES OU MAIS
        if(contTres(intel) >= 2){

            return true;
        }

        //ACEITA SE TIVER TRES E MANILHA
        if(contManilha(intel.getCards(), intel.getVira()) >= 1 && temTres(intel)){

            return true;
        }

        //ACEITA SE VALOR DA MAO FOR MAIOR OU IGUAL A 28
        if(valorMao(intel) >= 28){

            return true;
        }

        //ACEITA SE VALOR DA MAO FOR MAIOR OU IGUAL A 23 E TEM MANILHA
        if(valorMao(intel) >= 23 && contManilha(intel.getCards(), intel.getVira()) > 0){

            return true;
        }

        //ACEITA SE OPONENTE TIVER POUCOS PONTOS E A MAO FOR RELATIVAMENTE BOA
        if(valorMao(intel) >= 26 && intel.getOpponentScore() < 5){

            return true;
        }


        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        // se oponente tiver na mao de onze, nao pede truco
        if(intel.getOpponentScore() == 11){

            return false;
        }

        int quantDois = contDois(intel);

        //SEGUNDA:
        if(!intel.getRoundResults().isEmpty()){

            // SEGUNDA: verifica se a mão esta na primeira e se tem casal menor
            if (temCasalMenor(intel)){

                return true;
            }

            // SEGUNDA: se tiver casal maior pede truco
            if (temCasalMaior(intel)) {

                return true;
            }

            // SEGUNDA: se tiver casal menor pede truco
            if (temCasalMenor(intel)) {

                return true;
            }

            // SEGUNDA: se tiver casal preto pede truco
            if (temCasalPreto(intel)) {

                return true;
            }

            // SEGUNDA: se tiver casal preto pede truco
            if (temCasalVermelho(intel)) {

                return true;
            }
        }

        // JOGA PRIMEIRO
        if (!intel.getOpponentCard().isPresent()) {

            //TERCEIRA: pede truco se tiver três ou manilha
            if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2){
                if( temTres(intel) || contManilha(intel.getCards(), intel.getVira()) > 0) {

                    return true;
                }
            }

            //SEGUNDA e GANHOU PRIMEIRA
            if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){

                //SEGUNDA: se tiver ganhado a primeira e tem manilha pra segunda, pede truco
                if( contManilha(intel.getCards(), intel.getVira()) > 0) {

                    return true;
                }

                // SEGUNDA: se tiver feito a primeira e tem 3 pra segunda
                if(temTres(intel)) {

                    return true;
                }

                //SEGUNDA: se a mão não esta na primeira , se tem mais de um 2 e se eu ganhei a primeira mão
                if (quantDois > 1 ) {

                    return true;
                }
            }
        }

        // JOGA DEPOIS
        else if (intel.getOpponentCard().isPresent()) {

            //SEGUNDA E AMARROU A PRIMEIRA
            if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW) {

                //SEGUNDA: pede truco se amarrou a primeira e consegue matar a manilha do oponente na segunda
                TrucoCard opponentCard = intel.getOpponentCard().get();
                if(opponentCard.isManilha(intel.getVira())){
                    for (TrucoCard carta : intel.getCards()) {
                        if (carta.relativeValue(intel.getVira()) > opponentCard.relativeValue(intel.getVira())) {

                            return true;
                        }
                    }
                }

                //SEGUNDA: pede truco se amarrou a primeira e consegue matar a carta(nao manilha) do oponente na segunda
                if(!opponentCard.isManilha(intel.getVira())){
                    for (TrucoCard carta : intel.getCards()) {
                        if (carta.getRank().value() > opponentCard.getRank().value()) {

                            return true;
                        }
                    }
                }

                //SEGUNDA: BLEFE: pede truco se amarrou a primeira e carta do oponente tem valor menor igual a 7
                if(!opponentCard.isManilha(intel.getVira())){
                    if(opponentCard.getRank().value() <= 7){

                        return true;
                    }
                }
            }

            //TERCEIRA: pede truco se consegue amarrar a terceira
            if (intel.getRoundResults().size() == 2 && intel.getRoundResults().get(1) == GameIntel.RoundResult.LOST) {
                TrucoCard opponentCard = intel.getOpponentCard().get();
                if(!opponentCard.isManilha(intel.getVira())) {
                    for (TrucoCard carta : intel.getCards()) {
                        if (carta.getRank().equals(opponentCard.getRank())) {

                            return true;
                        }
                    }

                }
            }

            //TERCEIRA E GANHOU PRIMEIRA
            if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2 && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){

                //TERCEIRA: BLEFE se a carta do oponente for um valete ou menor(sem ser manilha) e se tiver ganho a primeira, TRUCA no safado!!
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if(!opponentCard.isManilha(intel.getVira())){
                        if(opponentCard.getRank().value() <= 7){

                            return true;
                        }
                    }
                }

                //TERCEIRA: trucar se a carta for maior do que a do oponente e se não for manilha e tiver ganho a primeira
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if(!opponentCard.isManilha(intel.getVira())){
                        for (TrucoCard carta : intel.getCards()) {
                            if(opponentCard.getRank().value() < carta.getRank().value()){

                                return true;
                            }
                        }
                    }
                }
            }

            //TERCEIRA: trucar se a carta for maior do que a do oponente e for manilha
            if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() == 2) {
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    if(opponentCard.isManilha(intel.getVira())){
                        for (TrucoCard carta : intel.getCards()) {
                            if(matarManilha(intel).getSuit().ordinal() > opponentCard.getSuit().ordinal()){

                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel build) {
        Integer qtdManilha = contManilha(build.getCards(),build.getVira());

        //PRIMEIRA:
        if(build.getRoundResults().isEmpty()){

            // PRIMEIRA: joga pior carta se tiver casal maior
            if (temCasalMaior(build)){

                return CardToPlay.of(piorCarta(build));
            }

            // PRIMEIRA: joga pior carta se tiver casal menor
            if (temCasalMenor(build)){

                return CardToPlay.of(piorCarta(build));
            }

            // PRIMEIRA: joga pior carta se tiver casal preto
            if (temCasalPreto(build)){

                return CardToPlay.of(piorCarta(build));
            }

            // PRIMEIRA: joga pior carta se tiver casal vermelho
            if (temCasalVermelho(build)){

                return CardToPlay.of(piorCarta(build));
            }

        }

        // JOGA PRIMEIRO
        if (!build.getOpponentCard().isPresent()) {

            // PRIMEIRA:
            if(build.getRoundResults().isEmpty()){

                // PRIMEIRA: joga melhor carta se não tiver manilha
                if(qtdManilha == 0){
                    return CardToPlay.of(melhorCarta(build));
                }

                // PRIMEIRA: joga ouros ou espadas se tiver
                if(temOuros(build) || temEspada(build)){
                    for (TrucoCard card : build.getCards()) {
                        if (card.isOuros(build.getVira()) || card.isEspadilha(build.getVira())) {
                            return CardToPlay.of(card);
                        }
                    }
                }
            }

        }

        // JOGA DEPOIS DO PATO
        if (build.getOpponentCard().isPresent()) {

            //PRIMEIRA:
            if(build.getRoundResults().isEmpty()){
                // PRIMEIRA: tenta amarrar se tiver zap ou copas
                if (qtdManilha == 1 && temZap(build) || qtdManilha == 1 && temCopas(build)) {
                    for (TrucoCard card : build.getCards()) {
                        if (card.getRank() == build.getOpponentCard().get().getRank() && !card.isManilha(build.getVira())) {
                            return CardToPlay.of(card);
                        }
                    }
                }

                // PRIMEIRA: se tiver zap ou copas, tenta fazer a primeira sem utilizar a manilha
                if (qtdManilha == 1 && temZap(build) || qtdManilha == 1 && temCopas(build)) {
                    for (TrucoCard card : build.getCards()) {
                        if (card.getRank().value() > build.getOpponentCard().get().getRank().value() && !card.isManilha(build.getVira())
                                && !build.getOpponentCard().get().isManilha(build.getVira())) {
                            return CardToPlay.of(card);
                        }
                    }
                }
            }


            // mata a manilha oponente com uma manilha maior
            if (build.getOpponentCard().get().isManilha(build.getVira())) {
                return CardToPlay.of(matarManilha(build));
            }

            // mata carta do oponente com a menor que tiver se possivel se nao tiver manilha
            if(!build.getOpponentCard().get().isManilha(build.getVira())){
                return CardToPlay.of(matarCartaComMenor(build));
            }

        }
        return CardToPlay.of(melhorCarta(build));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        //vendo qtdd manilha
        int manilha =  contManilha(intel.getCards(),intel.getVira());
        //vendo qtdd de tres
        int quantTres = contTres(intel);

        //verifica se a mão não esta na primeira , se tem zap e se eu ganhei a primeira mão
        if (!intel.getRoundResults().isEmpty() && temZap(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {

            return 1;
        }
        //verifica se a mão não esta na primeira , se tem copas e se eu ganhei a primeira mão
        if (!intel.getRoundResults().isEmpty() && temCopas(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {

            return 1;
        }

        //verifica se a mão não esta na primeira , se tem casal vermelho e perdeu a primeira
        if (!intel.getRoundResults().isEmpty() && temCasalVermelho(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {

            return 1;
        }

        //verifica se a mão não esta na primeira , se tem casal preto e perdeu a primeira
        if (!intel.getRoundResults().isEmpty() && temCasalPreto(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {

            return 1;
        }

        //verifica se a mão não esta na primeira , se tem casal maior e perdeu a primeira
        if (!intel.getRoundResults().isEmpty() && temCasalMaior(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {

            return 1;
        }

        //verifica se a mão não esta na primeira , se tem casal menor e perdeu a primeira
        if (!intel.getRoundResults().isEmpty() && temCasalMenor(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {

            return 1;
        }

        //se tivermos mais de uma, independente do nipe, desce
        if (manilha > 1) {

            return 0;
        }
        //se tivermos manilha e tres
        if (manilha >= 1 && temTres(intel)) {

            return 0;
        }

        if (temDois(intel) && temTres(intel)) {

            return 0;
        }

        //se tivermos mais de um tres
        if (quantTres > 1) {

            return 0;
        }
        // se tiver feito a primeira e tem tres pra segunda
        if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON && temTres(intel)) {

            return 0;
        }

        //se temos um tres na terceira e ganhamos a primeira
        if (!intel.getRoundResults().isEmpty() && temTres(intel) && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {

            return 0;
        }

        // aceita truco na primeira se tem manilha e valor da mão maior que 24
        if (intel.getRoundResults().isEmpty() && contManilha(intel.getCards(), intel.getVira()) > 0) {
            if(valorMao(intel) >= 24) {

                return 0;
            }
        }

        // aceita truco na primeira se tem manilha e valor da mão maior que 25
        if (intel.getRoundResults().isEmpty() && contManilha(intel.getCards(), intel.getVira()) == 0) {
            if(valorMao(intel) >= 28) {

                return 0;
            }
        }


        return -1;
    }
}
