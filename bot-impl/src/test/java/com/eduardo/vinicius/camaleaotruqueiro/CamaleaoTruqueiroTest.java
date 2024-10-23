/*
 *  Copyright (C) 2024 Eduardo D. Derisso - IFSP/SCL and Vinicius S. G. Oliveira - IFSP/SCL
 *  Contact: duarte <dot> derisso <at> ifsp <dot> edu <dot> br
 *  Contact: vinicius <dot> goncalves1 <at> ifsp <dot> edu <dot> br
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

package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.*;
import com.felipe.fabiano.truccard.GameRound;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.eduardo.vinicius.camaleaotruqueiro.HandsCardSituation.evaluateHandSituation;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static com.eduardo.vinicius.camaleaotruqueiro.TrucoUtils.*;


public class CamaleaoTruqueiroTest {

    private CamaleaoTruqueiro camaleao;

    private GameIntel.StepBuilder builder;

    @BeforeEach
    public void config() {
        camaleao = new CamaleaoTruqueiro();
    }

    @Nested @DisplayName("Test of TrucoUtils package")
    class TestOfTrucoUtilsPackage{
        @ParameterizedTest
        @CsvSource({
                "JACK, SPADES, FOUR, HEARTS, FIVE, CLUBS, SIX, DIAMONDS, SIX, DIAMONDS",
                "FOUR, SPADES, FOUR, HEARTS, SEVEN, CLUBS, QUEEN, DIAMONDS, QUEEN, DIAMONDS",
                "ACE, CLUBS, TWO, CLUBS, TWO, HEARTS, THREE, CLUBS, TWO, CLUBS",
                "JACK, SPADES, KING, HEARTS, TWO, CLUBS, THREE, DIAMONDS, KING, HEARTS",
                "FIVE, DIAMONDS, SIX, CLUBS, FOUR, SPADES, THREE, CLUBS, SIX, CLUBS"
        })
        @DisplayName("Should return the greater rank card")
        void shouldReturnTheGreaterRankCard(CardRank viraRank, CardSuit viraSuit,
                                            CardRank card1Rank, CardSuit card1Suit,
                                            CardRank card2Rank, CardSuit card2Suit,
                                            CardRank card3Rank, CardSuit card3Suit,
                                            CardRank expectedRank, CardSuit expectedSuit) {
            TrucoCard vira = TrucoCard.of(viraRank, viraSuit);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(card1Rank, card1Suit),
                    TrucoCard.of(card2Rank, card2Suit),
                    TrucoCard.of(card3Rank, card3Suit)
            );

            TrucoCard greaterCard = getGreatestCard(cards, vira);
            assertEquals(greaterCard, TrucoCard.of(expectedRank, expectedSuit));
        }


        //menor carta
        @ParameterizedTest
        @CsvSource({
                "JACK, SPADES, FOUR, HEARTS, FIVE, CLUBS, SIX, DIAMONDS, FOUR, HEARTS",
                "THREE, SPADES, FOUR, HEARTS, SEVEN, CLUBS, QUEEN, DIAMONDS, SEVEN, CLUBS",
                "ACE, CLUBS, TWO, CLUBS, TWO, HEARTS, THREE, CLUBS, THREE, CLUBS"
        })
        @DisplayName("Should return the lowest card")
        void shouldReturnTheLowestCard(CardRank viraRank, CardSuit viraSuit,
                                       CardRank card1Rank, CardSuit card1Suit,
                                       CardRank card2Rank, CardSuit card2Suit,
                                       CardRank card3Rank, CardSuit card3Suit,
                                       CardRank expectedRank, CardSuit expectedSuit) {
            TrucoCard vira = TrucoCard.of(viraRank, viraSuit);

            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(card1Rank, card1Suit),
                    TrucoCard.of(card2Rank, card2Suit),
                    TrucoCard.of(card3Rank, card3Suit)
            );

            TrucoCard lowest = getLowestCard(cards, vira);
            assertThat(lowest).isEqualTo(TrucoCard.of(expectedRank, expectedSuit));
        }


        @ParameterizedTest
        @CsvSource({
                "THREE, HEARTS, FIVE, DIAMONDS, QUEEN, DIAMONDS, THREE, DIAMONDS, 0",
                "THREE, HEARTS, FIVE, DIAMONDS, FOUR, DIAMONDS, THREE, DIAMONDS, 1",
                "THREE, HEARTS, FIVE, DIAMONDS, FOUR, DIAMONDS, FOUR, CLUBS, 2",
                "THREE, HEARTS, FOUR, SPADES, FOUR, DIAMONDS, FOUR, CLUBS, 3"
        })
        @DisplayName("Should return number of manilhas")
        void shouldReturnNumberOfManilhas(CardRank viraRank, CardSuit viraSuit,
                                          CardRank card1Rank, CardSuit card1Suit,
                                          CardRank card2Rank, CardSuit card2Suit,
                                          CardRank card3Rank, CardSuit card3Suit,
                                          int expectedManilhas) {
            TrucoCard vira = TrucoCard.of(viraRank, viraSuit);
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(card1Rank, card1Suit),
                    TrucoCard.of(card2Rank, card2Suit),
                    TrucoCard.of(card3Rank, card3Suit)
            );
            assertThat(numberOfManilhas(cards, vira))
                    .as("Expected manilhas count")
                    .isEqualTo(expectedManilhas);
        }


        @ParameterizedTest
        @CsvSource({
                "THREE, HEARTS, FIVE, DIAMONDS, QUEEN, DIAMONDS, ACE, DIAMONDS, 0",
                "THREE, HEARTS, FIVE, DIAMONDS, ACE, DIAMONDS, TWO, DIAMONDS, 1",
                "THREE, HEARTS, FIVE, DIAMONDS, FOUR, DIAMONDS, TWO, CLUBS, 2",
                "THREE, HEARTS, TWO, SPADES, FOUR, DIAMONDS, THREE, CLUBS, 3"
        })
        @DisplayName("shouldReturnOneHighCards")
        void shouldReturnOneHighCard(CardRank viraRank, CardSuit viraSuit,
                                     CardRank card1Rank, CardSuit card1Suit,
                                     CardRank card2Rank, CardSuit card2Suit,
                                     CardRank card3Rank, CardSuit card3Suit,
                                     int numberOfManilhas) {

            TrucoCard vira = TrucoCard.of(viraRank, viraSuit);
            List<TrucoCard> cards = Arrays.asList(
                    TrucoCard.of(card1Rank, card1Suit),
                    TrucoCard.of(card2Rank, card2Suit),
                    TrucoCard.of(card3Rank, card3Suit)
            );
            assertThat(getNumberOfHighRankCards(cards, vira))
                    .as("Expected high cards count")
                    .isEqualTo(numberOfManilhas);
        }

        @ParameterizedTest
        @CsvSource({
                "10,9,true",
                "1,0,true",
                "9,4,true",
                "9,9,false",
                "8,9,false",
                "0,6,false",
                "0,0,false",
        })
        @DisplayName("shouldReturnIfTheBotIsWinning")
        void shouldReturnIfTheBotIsWinning(int botPoints, int opponentPoints, boolean expectedResult) {
            boolean result = isWinning(botPoints, opponentPoints);

            assertThat(result).isEqualTo(expectedResult);
        }

        @Nested
        @DisplayName("test for finding card of median rank of hand")
        class findingCardOfMedianRankOfHand {
            @ParameterizedTest
            @CsvSource({
                    "KING, HEARTS, ACE, CLUBS, ACE, DIAMONDS, SIX, HEARTS, ACE, DIAMONDS",
                    "JACK, SPADES, KING, CLUBS, KING, HEARTS, ACE, SPADES, KING, HEARTS",
                    "ACE, DIAMONDS, TWO, CLUBS, TWO, DIAMONDS, SEVEN, HEARTS, TWO, DIAMONDS"
            })
            @DisplayName("Should return a median card that is a Manilha")
            void shouldReturnAMedianCardThatIsAManilha(CardRank viraRank, CardSuit viraSuit,
                                                       CardRank card1Rank, CardSuit card1Suit,
                                                       CardRank card2Rank, CardSuit card2Suit,
                                                       CardRank card3Rank, CardSuit card3Suit,
                                                       CardRank expectedRank, CardSuit expectedSuit) {
                SoftAssertions softly = new SoftAssertions();
                TrucoCard vira = TrucoCard.of(viraRank, viraSuit);
                List<TrucoCard> cards = Arrays.asList(
                        TrucoCard.of(card1Rank, card1Suit),
                        TrucoCard.of(card2Rank, card2Suit),
                        TrucoCard.of(card3Rank, card3Suit)
                );

                TrucoCard medianCard = getMedianValue(cards, vira);

                softly.assertThat(medianCard.isManilha(vira)).isTrue();
                softly.assertThat(medianCard.getRank()).isEqualTo(expectedRank);
                softly.assertThat(medianCard.getSuit()).isEqualTo(expectedSuit);
                softly.assertAll();
            }

            @Test
            @DisplayName("Should return a median card that is the same as the lowest")
            void shouldReturnAMedianCardThatIsTheSameAsTheLowest() {
                SoftAssertions softly = new SoftAssertions();
                TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
                List<TrucoCard> cards = Arrays.asList(
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
                );
                TrucoCard lowestCard = getLowestCard(cards, vira);
                TrucoCard medianCard = getMedianValue(cards, vira);
                softly.assertThat(medianCard).isEqualTo(lowestCard);
                softly.assertAll();
            }

            @Test
            @DisplayName("Should return a median card that is the same as the greatest")
            void shouldReturnAMedianCardThatIsTheSameAsTheGreatest() {
                SoftAssertions softly = new SoftAssertions();
                TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.HEARTS);
                List<TrucoCard> cards = Arrays.asList(
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
                );
                TrucoCard greatestCard = getGreatestCard(cards, vira);
                TrucoCard medianCard = getMedianValue(cards, vira);
                softly.assertThat(medianCard).isEqualTo(greatestCard);
                softly.assertAll();
            }

            @Test
            @DisplayName("Should return the number of worst cards then the median card when it is Copas")
            void shouldReturnTheNumberOfWorstCardsThenTheMedianCardWhenItCopas() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
                List<TrucoCard> cards = Arrays.asList(
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                );
                TrucoCard medianCard = getMedianValue(cards, vira);
                assertThat(getNumberOfCardWorstThanMedianCard(medianCard, vira)).isEqualTo(38);
            }

            @Test
            @DisplayName("Should return the number of worst cards then the median card when it is Espadilhas")
            void shouldReturnTheNumberOfWorstCardsThenTheMedianCardWhenItIsEspadilhas() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
                List<TrucoCard> cards = Arrays.asList(
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.JACK, CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                );
                TrucoCard medianCard = getMedianValue(cards, vira);
                assertThat(getNumberOfCardWorstThanMedianCard(medianCard, vira)).isEqualTo(37);
            }

            @Test
            @DisplayName("Should return the number of worst cards then the median card when it is Ouros")
            void shouldReturnTheNumberOfWorstCardsThenTheMedianCardWhenItIsOuros() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.DIAMONDS);
                List<TrucoCard> cards = Arrays.asList(
                        TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.ACE, CardSuit.CLUBS)
                );
                TrucoCard medianCard = getMedianValue(cards, vira);
                assertThat(getNumberOfCardWorstThanMedianCard(medianCard, vira)).isEqualTo(36);
            }

            @Test
            @DisplayName("Should return the number of worst cards then the median card when vira is greater or equals then median card")
            void shouldReturnTheNumberOfWorstCardsThenTheMedianCardWhenViraIsGreaterOrEqualsThenMedianCard() {
                SoftAssertions softly = new SoftAssertions();

                TrucoCard viraSomeRankThenMedianCard = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);
                TrucoCard viraGreaterRankThenMedianCard = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
                TrucoCard medianCard = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
                softly.assertThat(getNumberOfCardWorstThanMedianCard(medianCard, viraSomeRankThenMedianCard)).isEqualTo(20);
                softly.assertThat(getNumberOfCardWorstThanMedianCard(medianCard, viraGreaterRankThenMedianCard)).isEqualTo(20);
                softly.assertAll();
            }


            @Test
            @DisplayName("Should return the number of worst cards then the median card when vira is lowest then median card")
            void shouldReturnTheNumberOfWorstCardsThenTheMedianCardWhenViraIsLowestThenMedianCard() {
                SoftAssertions softly = new SoftAssertions();

                TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);
                TrucoCard medianCard = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
                softly.assertThat(getNumberOfCardWorstThanMedianCard(medianCard, vira)).isEqualTo(15);
                vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
                medianCard = TrucoCard.of(CardRank.SIX, CardSuit.SPADES);
                softly.assertThat(getNumberOfCardWorstThanMedianCard(medianCard, vira)).isEqualTo(3);
                softly.assertAll();
            }

            @Test
            @DisplayName("Should return the number of worst cards then the median card when ranks of vira and median card are both 1")
            void shouldReturnTheNumberOfWorstCardsThenTheMedianCardWhenRanksOfViraAndMedianCardAreBoth1() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);
                TrucoCard medianCard = TrucoCard.of(CardRank.FOUR, CardSuit.SPADES);
                assertThat(getNumberOfCardWorstThanMedianCard(medianCard, vira)).isEqualTo(0);
            }

            @Test
            @DisplayName("Should return the chances of player has a absolut victory when he or she has best cards")
            void shouldReturnTheChancesOfPlayerHasAAbsolutVictoryWhenHeOrSheHasGreatestCards() {
                TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.SPADES);
                List<TrucoCard> playersHand = Arrays.asList(
                        TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES)
                );

                Float result = getProbabilityOfAbsolutVictoryHand(playersHand, vira);
                assertThat(result).isEqualTo(1);
            }

            @Test
            @DisplayName("Should return the chances of player has a absolut victory when he or she has worst cards")
            void shouldReturnTheChancesOfPlayerHasAAbsolutVictoryWhenHeOrSheHasWorstestCards() {
                TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
                List<TrucoCard> playersHand = Arrays.asList(
                        TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
                );

                Float result = getProbabilityOfAbsolutVictoryHand(playersHand, vira);
                assertThat(result).isEqualTo(0);
            }
        }

        @Test
        @DisplayName("Should return if the bot plays first")
        void shouldReturnIfTheBotPlaysFirst() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> myCards = List.of();
            List<TrucoCard> openCards = List.of(vira);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(1);

            assertTrue(theBotPlaysFirst(builder.build()));
        }

        @Test
        @DisplayName("should return if is the first round")
        void shouldReturnIfIsTheFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            List<TrucoCard> openCards = List.of(vira);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(1);

            assertTrue(isTheFirstRound(builder.build()));
        }

        @Test
        @DisplayName("Should return if is the second round")
        void shouldReturnIfIsTheSecondRound() {

            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
            );
            List<TrucoCard> openCards = List.of(vira, TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS));

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(1);

            assertTrue(isTheSecondRound(builder.build()));
        }

        @ParameterizedTest
        @CsvSource({
                "TWO, HEARTS, FOUR, HEARTS, THREE, SPADES, SEVEN, HEARTS, THREE, DIAMONDS, 1",
                "FOUR, DIAMONDS, ACE, CLUBS, JACK, HEARTS, SEVEN, CLUBS, QUEEN, HEARTS, 2",
                "TWO, CLUBS, FOUR, CLUBS, FIVE, SPADES, SIX, DIAMONDS, SEVEN, HEARTS, 0",
                "KING, SPADES, QUEEN, CLUBS, QUEEN, SPADES, ACE, DIAMONDS, KING, CLUBS, 1",
                "SEVEN, HEARTS, ACE, SPADES, KING, DIAMONDS, QUEEN, CLUBS, JACK, SPADES, 3"
        })
        @DisplayName("Should return correct number of cards stronger than the opponent")
        void shouldReturnCorrectNumberOfCardsStrongerThanTheOpponent(CardRank viraRank, CardSuit viraSuit,
                                                                     CardRank myCard1Rank, CardSuit myCard1Suit,
                                                                     CardRank myCard2Rank, CardSuit myCard2Suit,
                                                                     CardRank myCard3Rank, CardSuit myCard3Suit,
                                                                     CardRank opponentCardRank, CardSuit opponentCardSuit,
                                                                     int expectedStrongestCardCount) {
            TrucoCard vira = TrucoCard.of(viraRank, viraSuit);
            List<TrucoCard> myCards = List.of(
                    TrucoCard.of(myCard1Rank, myCard1Suit),
                    TrucoCard.of(myCard2Rank, myCard2Suit),
                    TrucoCard.of(myCard3Rank, myCard3Suit)
            );
            List<TrucoCard> openCards = List.of(vira);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(0);

            builder.opponentCard(TrucoCard.of(opponentCardRank, opponentCardSuit));

            List<TrucoCard> cards = haveStrongestCard(builder.build(), myCards);

            assertEquals(expectedStrongestCardCount, cards.size());
        }

        @ParameterizedTest
        @CsvSource({
                "ACE, SPADES, THREE, CLUBS, TWO, SPADES, FOUR, HEARTS, 0",
                "QUEEN, HEARTS, FIVE, DIAMONDS, QUEEN, HEARTS, THREE, CLUBS, 1",
                "QUEEN, DIAMONDS, JACK, CLUBS, KING, DIAMONDS, QUEEN, HEARTS, 2",
                "ACE, DIAMONDS, JACK, CLUBS, KING, DIAMONDS, QUEEN, HEARTS, 3",
        })
        @DisplayName("should return one medium cards")
        void shouldReturnOneMediumCard(CardRank viraCardRank, CardSuit viraCardSuit,
                                       CardRank card1Rank, CardSuit card1Suit,
                                       CardRank card2Rank, CardSuit card2Suit,
                                       CardRank card3Rank, CardSuit card3Suit,
                                       int expectedMedianRankCardCount) {
            TrucoCard vira = TrucoCard.of(viraCardRank, viraCardSuit);

            TrucoCard card1 = TrucoCard.of(card1Rank, card1Suit);
            TrucoCard card2 = TrucoCard.of(card2Rank, card2Suit);
            TrucoCard card3 = TrucoCard.of(card3Rank, card3Suit);
            List<TrucoCard> handCards = Arrays.asList(card1, card2, card3);

            int numberOfMediumCard = getNumberOfMediumRankCards(handCards, vira);
            assertEquals(expectedMedianRankCardCount, numberOfMediumCard);
        }

        @Test
        @DisplayName("shouldReturnOneLowCard")
        void shouldReturnOneLowCard() {
            TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            TrucoCard card2 = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
            TrucoCard card3 = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
            List<TrucoCard> handCards = Arrays.asList(card1, card2, card3);

            int numberOfLowCard = getNumberOfLowRankCards(handCards, vira);
            assertEquals(1, numberOfLowCard);
        }

        @Test
        @DisplayName("Should return if the bot won the first round")
        void shouldReturnIfTheBotWonTheFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> myCards = List.of();
            List<TrucoCard> openCards = List.of(vira);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(1);

            assertTrue(winFistRound(builder.build()));
        }

        @Test
        @DisplayName("Should return if the bot drew the first round")
        void shouldReturnIfTheBotDrewTheFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> myCards = List.of();
            List<TrucoCard> openCards = List.of(vira);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.DREW), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(1);

            assertTrue(drewFistRound(builder.build()));
        }

        @Test
        @DisplayName("should return if the bot lost the first round")
        void shouldReturnIfTheBotLostTheFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> myCards = List.of();
            List<TrucoCard> openCards = List.of(vira);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(1);

            assertTrue(lostFistRound(builder.build()));
        }

        @Test
        @DisplayName("Should return if the bot won the second round")
        void shouldReturnIfTheBotWonTheSecondRound() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> myCards = List.of();
            List<TrucoCard> openCards = List.of(vira);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST, GameIntel.RoundResult.WON), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(1);

            assertTrue(winSecondRound(builder.build()));
        }

        @Test
        @DisplayName("Should return if the bot lost the second round")
        void shouldReturnIfTheBotLostTheSecondRound() {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> myCards = List.of();
            List<TrucoCard> openCards = List.of(vira);

            builder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON, GameIntel.RoundResult.LOST), openCards, vira, 1)
                    .botInfo(myCards, 0)
                    .opponentScore(1);

            assertTrue(lostSecondRound(builder.build()));
        }

    }

    @Test
    @DisplayName("Should accept maoDeOnze whit high cards")
    void shouldAcceptMaoDeOnzeWhitHighCards() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
        );
        List<TrucoCard> openCards = List.of(vira);
        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(),openCards,vira,3)
                .botInfo(cards,0)
                .opponentScore(0);

        assertTrue(camaleao.getMaoDeOnzeResponse(builder.build()));
    }

    @Test @DisplayName("Should accept maoDeOnze when opponent score is less then 7 points and has one strong card")
    void shouldAcceptMaoDeOnzeWhenOpponentScoreIsLessThen7PointsAndHasOneStrongCard() {
        TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
        List<TrucoCard> cards = Arrays.asList(
                TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                TrucoCard.of(CardRank.THREE, CardSuit.CLUBS),
                TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
        );
        List<TrucoCard> openCards = List.of(vira);

        builder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(),openCards,vira,3)
                .botInfo(cards,0)
                .opponentScore(6);

        assertTrue(camaleao.getMaoDeOnzeResponse(builder.build()));
    }

    @ParameterizedTest
    @CsvSource({
            //Round number    ||   className expected   ||
            "1, FirstRoundStrategy",
            "2, SecondRoundStrategy",
            "3,ThirdRoundStrategy"
    })
    @DisplayName("Creating Round Strategy based on actual round number")
    void CreatingRoundStrategy(int roundNumber, String expectedStrategyClassName) {
        //When
        List<GameIntel.RoundResult> roundResults = new ArrayList<>();
        switch (roundNumber) {
            case 2: roundResults.add(GameIntel.RoundResult.DREW);
                break;
            case 3:
            {
                roundResults.add(GameIntel.RoundResult.DREW);
                roundResults.add(GameIntel.RoundResult.DREW);
            };
                break;
        }
        //Then
        GameIntel intel = GameIntel.StepBuilder.with()
                .gameInfo(roundResults,List.of(),TrucoCard.of(CardRank.KING, CardSuit.HEARTS),1)
                .botInfo(List.of(),0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(CardRank.HIDDEN, CardSuit.HIDDEN)).build();

        RoundStrategy strategy = RoundStrategy.of(intel);

        assertThat(strategy.getClass().getSimpleName()).isEqualTo(expectedStrategyClassName);
    }


    @Nested @DisplayName("Test of actions for when bots in each round possible")
    class TestOfBotActionInAnySituation {

        TrucoCard vira = TrucoCard.of(CardRank.THREE, CardSuit.HEARTS);

        @Nested @DisplayName("Hand´s Cards Situation Test")
        class HandsCardsSituation {

            @ParameterizedTest
            @CsvSource({
                    "FOUR, CLUBS, FIVE, CLUBS, FIVE, SPADES, FIVE, HEARTS,ALMOST_ABSOLUTE_VICTORY",
                    "FOUR, CLUBS, FIVE, CLUBS, FIVE, SPADES, TWO, HEARTS, ALMOST_ABSOLUTE_VICTORY",
                    "FOUR, CLUBS, FIVE, CLUBS, FIVE, SPADES, ACE, HEARTS, ALMOST_ABSOLUTE_VICTORY",
                    "FOUR, CLUBS, FIVE, CLUBS, FIVE, SPADES, FOUR, HEARTS, ALMOST_ABSOLUTE_VICTORY",

                    "FOUR, CLUBS, FIVE, CLUBS, TWO, SPADES, ACE, HEARTS, ALMOST_CERTAIN_VICTORY",
                    "FOUR, CLUBS, TWO, CLUBS, TWO, SPADES, TWO, HEARTS, ALMOST_CERTAIN_VICTORY",

                    "FOUR, CLUBS, TWO, CLUBS, TWO, SPADES, QUEEN, HEARTS, BLUFF_TO_GET_POINTS",
                    "FOUR, CLUBS, TWO, CLUBS, TWO, SPADES, FOUR, HEARTS, BLUFF_TO_GET_POINTS",

                    "FOUR, CLUBS, TWO, CLUBS, QUEEN, SPADES, QUEEN, HEARTS, BLUFF_TO_INTIMIDATE",
                    "FOUR, CLUBS, TWO, CLUBS, QUEEN, SPADES, FOUR, HEARTS, BLUFF_TO_INTIMIDATE",

                    "FOUR, CLUBS, QUEEN, CLUBS, QUEEN, SPADES, QUEEN, HEARTS, ALMOST_CERTAIN_DEFEAT",
                    "FOUR, CLUBS, QUEEN, CLUBS, QUEEN, SPADES, FOUR, HEARTS, ALMOST_CERTAIN_DEFEAT",
                    "FOUR, CLUBS, QUEEN, CLUBS, FOUR, SPADES, FOUR, HEARTS, ALMOST_CERTAIN_DEFEAT",
                    "FOUR, CLUBS, FOUR, CLUBS, FOUR, SPADES, FOUR, HEARTS, ALMOST_CERTAIN_DEFEAT",
            })
            @DisplayName("Should return the right type of hand cards situation when it´s 3 cards")
            void shouldReturnTheRightTypeOfHandCardsSituationWhenItsThreeCards(CardRank viraRank, CardSuit viraSuit, CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, CardRank cardRank3, CardSuit cardSuit3, HandsCardSituation situationExpected) {



                TrucoCard vira = TrucoCard.of(viraRank, viraSuit);
                TrucoCard card1 = TrucoCard.of(cardRank1, cardSuit1);
                TrucoCard card2 = TrucoCard.of(cardRank2, cardSuit2);
                TrucoCard card3 = TrucoCard.of(cardRank3, cardSuit3);

                List<TrucoCard> cards = Arrays.asList(
                        TrucoCard.of(cardRank1, cardSuit1),
                        TrucoCard.of(cardRank2, cardSuit2),
                        TrucoCard.of(cardRank3, cardSuit3)
                );


                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(),List.of(), vira, 0)
                        .botInfo(cards, 0)
                        .opponentScore(0)
                        .build();

                HandsCardSituation handsCardSituation = evaluateHandSituation(intel);
                assertThat(handsCardSituation).isEqualTo(situationExpected);
            }
        }

        @Nested @DisplayName("First Round Strategy")
        class FirstRoundStrategy {

            @ParameterizedTest
            @CsvSource({
                    // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | cardRank3 | cardSuit3 | willCallTruco | choosenCardRank | choosenCardSuit | highChangesOpponentRunFromTruco
                    //almost absolute victory
                    "FOUR, CLUBS, FOUR, HEARTS, SEVEN, SPADES, true, SEVEN, SPADES, false",
                    //almost certain victory
                    "FOUR, CLUBS, TWO, HEARTS, TWO, SPADES, true, FOUR, CLUBS, false",

                    //bluff to get points
                    "FOUR, CLUBS, TWO, HEARTS, KING, SPADES, true, FOUR, CLUBS, false",
                    "FOUR, CLUBS, TWO, HEARTS, SIX, SPADES, true, FOUR, CLUBS, false",

                    //bluff to intimidate
                    "FOUR, CLUBS, KING, HEARTS, KING, SPADES, false, KING, HEARTS, false",
                    "FOUR, CLUBS, KING, HEARTS, SIX, SPADES, false, SIX, SPADES, false",
                    //almost certain defeat
                    "KING, CLUBS, KING, HEARTS, SIX, SPADES, false, SIX, SPADES, false",
                    "KING, CLUBS, SIX, HEARTS, SIX, SPADES, false, SIX, HEARTS, false",
                    "SIX, CLUBS, SIX, HEARTS, SIX, SPADES, false, SIX, CLUBS, false",
                    //high changes opponent runs from truco
                    //"KING, CLUBS, KING, HEARTS, SIX, SPADES, true, KING, CLUBS, true",
            })
            @DisplayName("When bot opens hand")
            void whenBotOpensHand (CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, CardRank cardRank3, CardSuit cardSuit3,Boolean willCallTruco, CardRank cardChosenRank, CardSuit cardChosenSuit, Boolean highChangesOpponentRunFromTruco) {
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(cardRank1,cardSuit1),
                        TrucoCard.of(cardRank2,cardSuit2),
                        TrucoCard.of(cardRank3,cardSuit3)
                );
                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(),List.of(vira),vira,1)
                        .botInfo(botCards,0)
                        .opponentScore(0)
                        .build();

                boolean botDecideIfRaises =  camaleao.decideIfRaises(intel);
                TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(botDecideIfRaises).isEqualTo(willCallTruco);
                softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(cardChosenRank,cardChosenSuit));
                softly.assertAll();
            }

            @ParameterizedTest
            @CsvSource({
                    // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | cardRank3 | cardSuit3 | willCallTruco | choosenCardRank | choosenCardSuit | highChangesOpponentRunFromTruco | oppentCallsTruco | opennentCardRank | opennentCardSuit
                    //almost absolute victory
                    "FOUR, CLUBS, FOUR, HEARTS, SEVEN, SPADES, true, FOUR, CLUBS, false",
                    //almost certain victory
                    "FOUR, CLUBS, TWO, HEARTS, TWO, SPADES, false, TWO, SPADES, false",

                    //bluff to get points
                    "FOUR, CLUBS, TWO, HEARTS, KING, SPADES, false, KING, SPADES, false",
                    "FOUR, CLUBS, TWO, HEARTS, SIX, SPADES, false, SIX, SPADES, false",

                    //bluff to intimidate
                    "FOUR, CLUBS, KING, HEARTS, KING, SPADES, false, KING, SPADES, false",
                    "FOUR, CLUBS, KING, HEARTS, SIX, SPADES, false, KING, SPADES, false",
                    //almost certain defeat
                    "KING, CLUBS, KING, HEARTS, SIX, SPADES, false, KING, SPADES, false",
                    "KING, CLUBS, SIX, HEARTS, SIX, SPADES, false, KING, SPADES, false",
                    "SIX, CLUBS, SIX, HEARTS, SIX, SPADES, false, KING, SPADES, false",
                    //high changes opponent runs from truco
                    "KING, CLUBS, KING, HEARTS, SIX, SPADES, true, KING, SPADES, true",
            })
            @DisplayName("When bot is second to play")
            void whenBotIsSecondToPlay(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, CardRank cardRank3, CardSuit cardSuit3, Boolean willCallTruco, CardRank cardChosenRank, CardSuit cardChosenSuit, Boolean highChangesOpponentRunFromTruco) {
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(cardRank1,cardSuit1),
                        TrucoCard.of(cardRank2,cardSuit2),
                        TrucoCard.of(cardRank3,cardSuit3)
                );
                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(),List.of(vira),vira,1)
                        .botInfo(botCards,0)
                        .opponentScore(0)
                        .build();

                SoftAssertions softly = new SoftAssertions();

            }

        }

        @Nested @DisplayName("Second Round Strategy")
        class SecondRoundStrategy{


            @ParameterizedTest
            @CsvSource({
                    // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | willCallTruco | expectedCardRank | expectedCardSuit
                    //almost absolute victory
                    "FOUR, CLUBS, FOUR, HEARTS, true, DREW, FOUR, CLUBS",
                    //almost certain victory
                    "FOUR, CLUBS, TWO, HEARTS, true, DREW, FOUR, CLUBS",
                    "TWO, CLUBS, TWO, HEARTS, true, WON, TWO, CLUBS",

                    //bluff to get points
                    "TWO, CLUBS, KING, HEARTS, false, DREW, TWO, CLUBS",
                    "TWO, CLUBS, FIVE, HEARTS, false, DREW, TWO, CLUBS",

                    //bluff to intimidate
                    "KING, CLUBS, KING, HEARTS, false, DREW, KING, CLUBS",

                    //almost certain defeat
                    "KING, CLUBS, FIVE, HEARTS, false, DREW, KING, CLUBS",
                    "FIVE, CLUBS, FIVE, HEARTS, false, DREW, FIVE, CLUBS",

                    //high changes opponent runs from truco
                    //"KING, CLUBS, KING, HEARTS, true, true",
            })
            @DisplayName("When bot opens hand")
            void whenBotOpensHand (CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, Boolean willCallTruco, GameIntel.RoundResult roundResult, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                List<TrucoCard> botCards = List.of(
                        TrucoCard.of(cardRank1,cardSuit1),
                        TrucoCard.of(cardRank2,cardSuit2)
                );
                GameIntel intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(roundResult),List.of(vira),vira,1)
                        .botInfo(botCards,0)
                        .opponentScore(0)
                        .build();

                boolean botDecideIfRaises =  camaleao.decideIfRaises(intel);
                //TrucoCard botChosenCard = camaleao.chooseCard(intel).content();
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(botDecideIfRaises).isEqualTo(willCallTruco);
                //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                //if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                softly.assertAll();
            }
            @Nested
            @DisplayName("when bot is second to play")
            class whenBotIsSecondToPlayAndOponnentPlayedCard{

                @Nested
                @DisplayName("bot is winning")
                class botIsWinning{
                    int botScore = 6;
                    int opponentScore = 4;

                    @Nested
                    @DisplayName("bot won the last round")
                    class youWonTheLastRound{

                        List<GameIntel.RoundResult> roundResults = List.of(
                                GameIntel.RoundResult.WON
                        );

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | expectedRaiseResponse
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS,  1",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, 0",
                                "FOUR, CLUBS, QUEEN, HEARTS, 0",
                                "FOUR, CLUBS, FIVE, HEARTS, 0",
                                "TWO, CLUBS, FIVE, HEARTS, 0",
                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, 0",
                                "TWO, CLUBS, FIVE, HEARTS, 0",
                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, 0",
                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, -1",
                                "FIVE, CLUBS, FIVE, HEARTS, -1",
                        })
                        @DisplayName("opponent calls truco")
                        void OppenentCallsTruco(
                                CardRank cardRank1, CardSuit cardSuit1,
                                CardRank cardRank2, CardSuit cardSuit2,
                                int expectedResponse
                        ) {
                            List<TrucoCard> cards = Arrays.asList(
                                    TrucoCard.of(cardRank1, cardSuit1),
                                    TrucoCard.of(cardRank2, cardSuit2)
                            );
                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira), vira, 3)
                                    .botInfo(cards, botScore)
                                    .opponentScore(opponentScore).build();

                            int response = camaleao.getRaiseResponse(intel);


                            assertEquals(expectedResponse, response);
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS, true, FOUR, CLUBS",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, true, FOUR, CLUBS",
                                "TWO, CLUBS, TWO, HEARTS, true, TWO, CLUBS",

                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, true, TWO, CLUBS",
                                "TWO, CLUBS, FIVE, HEARTS, true, TWO, CLUBS",

                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, true, KING, CLUBS",

                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, true, KING, CLUBS",
                                "FIVE, CLUBS, FIVE, HEARTS, true, FIVE, CLUBS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played vincible card")
                        void opponentPlayedVincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).getRank().value() == 1) {
                                oppenentCardRank = CardRank.THREE;
                                oppenentCardSuit = CardSuit.DIAMONDS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()-1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()-1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botDecidesIfRaises =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botDecidesIfRaises).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | responseToTruco | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, HEARTS, FOUR, SPADES, true, FOUR, SPADES",
                                //almost certain victory
                                "FOUR, HEARTS, TWO, SPADES, true, TWO, SPADES",
                                "TWO, HEARTS, TWO, HEARTS, true, TWO, CLUBS",

                                //bluff to get points
                                "TWO, HEARTS, KING, HEARTS, true, KING, HEARTS",
                                "TWO, HEARTS, FIVE, HEARTS, true, FIVE, HEARTS",

                                //bluff to intimidate
                                "KING, HEARTS, KING, HEARTS, true, KING, CLUBS",

                                //almost certain defeat
                                "KING, HEARTS, FIVE, HEARTS, true, KING, CLUBS",
                                "FIVE, HEARTS, FIVE, HEARTS, true, FIVE, CLUBS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played invincible card")
                        void opponentPlayedInvincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).isCopas(vira)) {
                                oppenentCardRank = CardRank.FOUR;
                                oppenentCardSuit = CardSuit.CLUBS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()+1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()+1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botRaiseResponse =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botRaiseResponse).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }
                    }

                    @Nested
                    @DisplayName("bot drew the last round")
                    class botDrewTheLastRound{

                        List<GameIntel.RoundResult> roundResults = List.of(
                                GameIntel.RoundResult.DREW
                        );

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | expectedRaiseResponse
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS,  1",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, 0",
                                "FOUR, CLUBS, QUEEN, HEARTS, 0",
                                "FOUR, CLUBS, FIVE, HEARTS, 0",
                                "TWO, CLUBS, FIVE, HEARTS, 0",
                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, 0",
                                "TWO, CLUBS, FIVE, HEARTS, 0",
                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, 0",
                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, -1",
                                "FIVE, CLUBS, FIVE, HEARTS, -1",
                        })
                        @DisplayName("opponent calls truco")
                        void OppenentCallsTruco(
                                CardRank cardRank1, CardSuit cardSuit1,
                                CardRank cardRank2, CardSuit cardSuit2,
                                int expectedResponse
                        ) {
                            List<TrucoCard> cards = Arrays.asList(
                                    TrucoCard.of(cardRank1, cardSuit1),
                                    TrucoCard.of(cardRank2, cardSuit2)
                            );
                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira), vira, 3)
                                    .botInfo(cards, botScore)
                                    .opponentScore(opponentScore).build();

                            int response = camaleao.getRaiseResponse(intel);


                            assertEquals(expectedResponse, response);
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS, true, FOUR, CLUBS",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, true, FOUR, CLUBS",
                                "TWO, CLUBS, TWO, HEARTS, true, TWO, CLUBS",

                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, true, TWO, CLUBS",
                                "TWO, CLUBS, FIVE, HEARTS, true, TWO, CLUBS",

                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, true, KING, CLUBS",

                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, true, KING, CLUBS",
                                "FIVE, CLUBS, FIVE, HEARTS, true, FIVE, CLUBS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played vincible card")
                        void opponentPlayedVincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).getRank().value() == 1) {
                                oppenentCardRank = CardRank.THREE;
                                oppenentCardSuit = CardSuit.DIAMONDS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()-1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()-1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botDecidesIfRaises =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botDecidesIfRaises).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | responseToTruco | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, HEARTS, FOUR, SPADES, true, FOUR, SPADES",
                                //almost certain victory
                                "FOUR, HEARTS, TWO, SPADES, true, TWO, SPADES",
                                "TWO, HEARTS, TWO, HEARTS, true, TWO, CLUBS",

                                //bluff to get points
                                "TWO, HEARTS, KING, HEARTS, true, KING, HEARTS",
                                "TWO, HEARTS, FIVE, HEARTS, true, FIVE, HEARTS",

                                //bluff to intimidate
                                "KING, HEARTS, KING, HEARTS, true, KING, CLUBS",

                                //almost certain defeat
                                "KING, HEARTS, FIVE, HEARTS, true, KING, CLUBS",
                                "FIVE, HEARTS, FIVE, HEARTS, true, FIVE, CLUBS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played invincible card")
                        void opponentPlayedInvincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).isCopas(vira)) {
                                oppenentCardRank = CardRank.FOUR;
                                oppenentCardSuit = CardSuit.CLUBS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()+1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()+1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botRaiseResponse =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botRaiseResponse).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }
                    }

                    @Nested
                    @DisplayName("bot lost the last round")
                    class botLostTheLastRound{

                        List<GameIntel.RoundResult> roundResults = List.of(
                                GameIntel.RoundResult.LOST
                        );

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | expectedRaiseResponse
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS,  1",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, 0",
                                "FOUR, CLUBS, QUEEN, HEARTS, 0",
                                "FOUR, CLUBS, FIVE, HEARTS, 0",
                                "TWO, CLUBS, TWO, HEARTS, 0",
                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, -1",
                                "TWO, CLUBS, FIVE, HEARTS, -1",
                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, -1",
                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, -1",
                                "FIVE, CLUBS, FIVE, HEARTS, -1",
                        })
                        @DisplayName("opponent calls truco")
                        void OppenentCallsTruco(
                                CardRank cardRank1, CardSuit cardSuit1,
                                CardRank cardRank2, CardSuit cardSuit2,
                                int expectedResponse
                        ) {
                            List<TrucoCard> cards = Arrays.asList(
                                    TrucoCard.of(cardRank1, cardSuit1),
                                    TrucoCard.of(cardRank2, cardSuit2)
                            );
                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira), vira, 3)
                                    .botInfo(cards, botScore)
                                    .opponentScore(opponentScore).build();

                            int response = camaleao.getRaiseResponse(intel);


                            assertEquals(expectedResponse, response);
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS, true, FOUR, CLUBS",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, false, FOUR, CLUBS",
                                "TWO, CLUBS, TWO, HEARTS, false, TWO, CLUBS",

                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, false, TWO, CLUBS",
                                "TWO, CLUBS, FIVE, HEARTS, false, TWO, CLUBS",

                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, false, KING, CLUBS",

                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, false, KING, CLUBS",
                                "FIVE, CLUBS, FIVE, HEARTS, false, FIVE, CLUBS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played vincible card")
                        void opponentPlayedVincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).getRank().value() == 1) {
                                oppenentCardRank = CardRank.THREE;
                                oppenentCardSuit = CardSuit.DIAMONDS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()-1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()-1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botDecidesIfRaises =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botDecidesIfRaises).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | responseToTruco | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, HEARTS, FOUR, SPADES, true, FOUR, HEARTS",
                                //almost certain victory
                                "FOUR, HEARTS, TWO, SPADES, false, TWO, SPADES",
                                "TWO, HEARTS, TWO, HEARTS, false, TWO, HEARTS",

                                //bluff to get points
                                "TWO, HEARTS, KING, HEARTS, false, KING, HEARTS",
                                "TWO, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",

                                //bluff to intimidate
                                "KING, HEARTS, KING, HEARTS, false, KING, HEARTS",

                                //almost certain defeat
                                "KING, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",
                                "FIVE, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played invincible card")
                        void opponentPlayedInvincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).isCopas(vira)) {
                                oppenentCardRank = CardRank.FOUR;
                                oppenentCardSuit = CardSuit.CLUBS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()+1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()+1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botRaiseResponse =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botRaiseResponse).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }
                    }
                }

                @Nested
                @DisplayName("bot is losing")
                class botIsLosing{

                    int botScore = 4;
                    int opponentScore = 6;

                    @Nested
                    @DisplayName("bot won the last round")
                    class youWonTheLastRound{

                        List<GameIntel.RoundResult> roundResults = List.of(
                                GameIntel.RoundResult.WON
                        );

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | expectedRaiseResponse
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS,  1",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, 0",
                                "FOUR, CLUBS, QUEEN, HEARTS, 0",
                                "FOUR, CLUBS, FIVE, HEARTS, 0",
                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, -1",
                                "TWO, CLUBS, FIVE, HEARTS, -1",
                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, -1",
                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, -1",
                                "FIVE, CLUBS, FIVE, HEARTS, -1",
                        })
                        @DisplayName("opponent calls truco")
                        void OppenentCallsTruco(
                                CardRank cardRank1, CardSuit cardSuit1,
                                CardRank cardRank2, CardSuit cardSuit2,
                                int expectedResponse
                        ) {
                            List<TrucoCard> cards = Arrays.asList(
                                    TrucoCard.of(cardRank1, cardSuit1),
                                    TrucoCard.of(cardRank2, cardSuit2)
                            );
                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira), vira, 3)
                                    .botInfo(cards, botScore)
                                    .opponentScore(opponentScore).build();

                            int response = camaleao.getRaiseResponse(intel);

                            assertEquals(expectedResponse, response);
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS, true, FOUR, CLUBS",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, true, FOUR, CLUBS",
                                "TWO, CLUBS, TWO, HEARTS, true, TWO, CLUBS",

                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, false, TWO, CLUBS",
                                "TWO, CLUBS, FIVE, HEARTS, false, TWO, CLUBS",

                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, false, KING, CLUBS",

                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, false, KING, CLUBS",
                                "FIVE, CLUBS, FIVE, HEARTS, false, FIVE, CLUBS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played vincible card")
                        void opponentPlayedVincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).getRank().value() == 1) {
                                oppenentCardRank = CardRank.THREE;
                                oppenentCardSuit = CardSuit.DIAMONDS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()-1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()-1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botDecidesIfRaises =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botDecidesIfRaises).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | responseToTruco | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, HEARTS, FOUR, SPADES, true, FOUR, SPADES",
                                //almost certain victory
                                "FOUR, HEARTS, TWO, SPADES, true, TWO, SPADES",
                                "TWO, HEARTS, TWO, HEARTS, true, TWO, CLUBS",

                                //bluff to get points
                                "TWO, HEARTS, KING, HEARTS, false, KING, HEARTS",
                                "TWO, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",

                                //bluff to intimidate
                                "KING, HEARTS, KING, SPADES, false, KING, HEARTS",

                                //almost certain defeat
                                "KING, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",
                                "FIVE, HEARTS, FIVE, SPADES, false, FIVE, HEARTS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played invincible card")
                        void opponentPlayedInvincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).isCopas(vira)) {
                                oppenentCardRank = CardRank.FOUR;
                                oppenentCardSuit = CardSuit.CLUBS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()+1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()+1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botRaiseResponse =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botRaiseResponse).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }
                    }

                    @Nested
                    @DisplayName("bot drew the last round")
                    class botDrewTheLastRound{

                        List<GameIntel.RoundResult> roundResults = List.of(
                                GameIntel.RoundResult.DREW
                        );

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | expectedRaiseResponse
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS,  1",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, 0",
                                "FOUR, CLUBS, QUEEN, HEARTS, 0",
                                "FOUR, CLUBS, FIVE, HEARTS, 0",
                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, -1",
                                "TWO, CLUBS, FIVE, HEARTS, -1",
                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, -1",
                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, -1",
                                "FIVE, CLUBS, FIVE, HEARTS, -1",
                        })
                        @DisplayName("opponent calls truco")
                        void OppenentCallsTruco(
                                CardRank cardRank1, CardSuit cardSuit1,
                                CardRank cardRank2, CardSuit cardSuit2,
                                int expectedResponse
                        ) {
                            List<TrucoCard> cards = Arrays.asList(
                                    TrucoCard.of(cardRank1, cardSuit1),
                                    TrucoCard.of(cardRank2, cardSuit2)
                            );
                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira), vira, 3)
                                    .botInfo(cards, botScore)
                                    .opponentScore(opponentScore).build();

                            int response = camaleao.getRaiseResponse(intel);

                            assertEquals(expectedResponse, response);
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS, true, FOUR, CLUBS",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, true, FOUR, CLUBS",
                                "TWO, CLUBS, TWO, HEARTS, true, TWO, CLUBS",

                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, false, TWO, CLUBS",
                                "TWO, CLUBS, FIVE, HEARTS, false, TWO, CLUBS",

                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, false, KING, CLUBS",

                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, false, KING, CLUBS",
                                "FIVE, CLUBS, FIVE, HEARTS, false, FIVE, CLUBS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played vincible card")
                        void opponentPlayedVincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).getRank().value() == 1) {
                                oppenentCardRank = CardRank.THREE;
                                oppenentCardSuit = CardSuit.DIAMONDS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()-1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()-1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botDecidesIfRaises =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botDecidesIfRaises).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | responseToTruco | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, HEARTS, FOUR, SPADES, true, FOUR, SPADES",
                                //almost certain victory
                                "FOUR, HEARTS, TWO, SPADES, true, TWO, SPADES",
                                "TWO, HEARTS, TWO, HEARTS, true, TWO, CLUBS",

                                //bluff to get points
                                "TWO, HEARTS, KING, HEARTS, false, KING, HEARTS",
                                "TWO, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",

                                //bluff to intimidate
                                "KING, HEARTS, KING, SPADES, false, KING, HEARTS",

                                //almost certain defeat
                                "KING, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",
                                "FIVE, HEARTS, FIVE, SPADES, false, FIVE, HEARTS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played invincible card")
                        void opponentPlayedInvincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).isCopas(vira)) {
                                oppenentCardRank = CardRank.FOUR;
                                oppenentCardSuit = CardSuit.CLUBS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()+1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()+1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botRaiseResponse =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botRaiseResponse).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }
                    }

                    @Nested
                    @DisplayName("bot lost the last round")
                    class botLostTheLastRound{

                        List<GameIntel.RoundResult> roundResults = List.of(
                                GameIntel.RoundResult.LOST
                        );

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | expectedRaiseResponse
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS,  1",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, 0",
                                "FOUR, CLUBS, QUEEN, HEARTS, 0",
                                "FOUR, CLUBS, FIVE, HEARTS, 0",
                                "TWO, CLUBS, TWO, HEARTS, 0",
                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, -1",
                                "TWO, CLUBS, FIVE, HEARTS, -1",
                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, -1",
                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, -1",
                                "FIVE, CLUBS, FIVE, HEARTS, -1",
                        })
                        @DisplayName("opponent calls truco")
                        void OppenentCallsTruco(
                                CardRank cardRank1, CardSuit cardSuit1,
                                CardRank cardRank2, CardSuit cardSuit2,
                                int expectedResponse
                        ) {
                            List<TrucoCard> cards = Arrays.asList(
                                    TrucoCard.of(cardRank1, cardSuit1),
                                    TrucoCard.of(cardRank2, cardSuit2)
                            );
                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira), vira, botScore)
                                    .botInfo(cards, opponentScore)
                                    .opponentScore(0).build();

                            int response = camaleao.getRaiseResponse(intel);

                            assertEquals(expectedResponse, response);
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, CLUBS, FOUR, HEARTS, true, FOUR, CLUBS",
                                //almost certain victory
                                "FOUR, CLUBS, TWO, HEARTS, false, FOUR, CLUBS",
                                "TWO, CLUBS, TWO, HEARTS, false, TWO, CLUBS",

                                //bluff to get points
                                "TWO, CLUBS, KING, HEARTS, false, TWO, CLUBS",
                                "TWO, CLUBS, FIVE, HEARTS, false, TWO, CLUBS",

                                //bluff to intimidate
                                "KING, CLUBS, KING, HEARTS, false, KING, CLUBS",

                                //almost certain defeat
                                "KING, CLUBS, FIVE, HEARTS, false, KING, CLUBS",
                                "FIVE, CLUBS, FIVE, HEARTS, false, FIVE, CLUBS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played vincible card")
                        void opponentPlayedVincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).getRank().value() == 1) {
                                oppenentCardRank = CardRank.THREE;
                                oppenentCardSuit = CardSuit.DIAMONDS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()-1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()-1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botDecidesIfRaises =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botDecidesIfRaises).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }

                        @ParameterizedTest
                        @CsvSource({
                                // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | responseToTruco | expectedCardRank | expectedCardSuit
                                //almost absolute victory
                                "FOUR, HEARTS, FOUR, SPADES, true, FOUR, HEARTS",
                                //almost certain victory
                                "FOUR, HEARTS, TWO, SPADES, false, TWO, SPADES",
                                "TWO, HEARTS, TWO, HEARTS, false, TWO, HEARTS",

                                //bluff to get points
                                "TWO, HEARTS, KING, HEARTS, false, KING, HEARTS",
                                "TWO, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",

                                //bluff to intimidate
                                "KING, HEARTS, KING, HEARTS, false, KING, HEARTS",

                                //almost certain defeat
                                "KING, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",
                                "FIVE, HEARTS, FIVE, HEARTS, false, FIVE, HEARTS",

                                //high changes opponent runs from truco
                                //"KING, CLUBS, KING, HEARTS, true, true",
                        })
                        @DisplayName("opponent played invincible card")
                        void opponentPlayedInvincibleCard(CardRank cardRank1, CardSuit cardSuit1, CardRank cardRank2, CardSuit cardSuit2, boolean expectedDecideIfRaises, CardRank expectedCardRank, CardSuit expectedCardSuit) {
                            List<TrucoCard> botCards = List.of(
                                    TrucoCard.of(cardRank1,cardSuit1),
                                    TrucoCard.of(cardRank2,cardSuit2)
                            );
                            CardRank oppenentCardRank;
                            CardSuit oppenentCardSuit;
                            if (getGreatestCard(botCards, vira).isCopas(vira)) {
                                oppenentCardRank = CardRank.FOUR;
                                oppenentCardSuit = CardSuit.CLUBS;
                            } else {
                                oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()+1];
                                oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()+1];
                            }

                            GameIntel intel = GameIntel.StepBuilder.with()
                                    .gameInfo(roundResults,List.of(vira, TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    )),vira,1)
                                    .botInfo(botCards,botScore)
                                    .opponentScore(opponentScore)
                                    .opponentCard(TrucoCard.of(
                                            oppenentCardRank,
                                            oppenentCardSuit
                                    ))
                                    .build();

                            boolean botRaiseResponse =  camaleao.decideIfRaises(intel);
                            TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                            SoftAssertions softly = new SoftAssertions();
                            softly.assertThat(botRaiseResponse).isEqualTo(expectedDecideIfRaises);
                            //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                            if(!camaleao.decideIfRaises(intel)) softly.assertThat(botChosenCard).isEqualTo(TrucoCard.of(expectedCardRank,expectedCardSuit));
                            softly.assertAll();
                        }
                    }
                }


            }
        }

        @Nested
        @DisplayName("Thrid Round Strategy")
        class thridRoundStrategy{


            int winningScore = 6;
            int losingScore = 4;

            @Nested
            @DisplayName("bot is winngin game")
            class botIsWinnginGame{

                @Nested
                @DisplayName("bot won first hand and lost in second")
                class botWonFirstHandAndLostInSecond{

                    List<GameIntel.RoundResult> roundResults = List.of(
                            GameIntel.RoundResult.WON,
                            GameIntel.RoundResult.LOST
                    );

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | expectedRaiseResponse
                            //almost absolute victory
                            "FOUR, CLUBS,  1",
                            //almost certain victory
                            "TWO, CLUBS, 0",
                            //almost certain defeat
                            "KING, CLUBS, -1",
                            "FIVE, CLUBS, -1",
                    })
                    @DisplayName("opponent calls truco")
                    void OppenentCallsTruco(
                            CardRank cardRank1, CardSuit cardSuit1,
                            int expectedResponse
                    ) {
                        List<TrucoCard> cards = Arrays.asList(
                                TrucoCard.of(cardRank1, cardSuit1)
                        );
                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira), vira, 3)
                                .botInfo(cards, winningScore)
                                .opponentScore(losingScore).build();

                        int response = camaleao.getRaiseResponse(intel);

                        assertEquals(expectedResponse, response);
                    }

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                            //almost absolute victory
                            "FOUR, CLUBS, true",
                            //almost certain victory
                            "TWO, CLUBS, true",

                            //almost certain defeat
                            "KING, CLUBS, true",
                            "FIVE, CLUBS, true",

                            //high changes opponent runs from truco
                            //"KING, CLUBS, KING, HEARTS, true, true",
                    })
                    @DisplayName("opponent played vincible card")
                    void opponentPlayedVincibleCard(CardRank cardRank1, CardSuit cardSuit1, boolean expectedDecideIfRaises) {
                        List<TrucoCard> botCards = List.of(
                                TrucoCard.of(cardRank1,cardSuit1)
                        );
                        CardRank oppenentCardRank;
                        CardSuit oppenentCardSuit;
                        if (getGreatestCard(botCards, vira).getRank().value() == 1) {
                            oppenentCardRank = CardRank.THREE;
                            oppenentCardSuit = CardSuit.DIAMONDS;
                        } else {
                            oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()-1];
                            oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()-1];
                        }

                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                        oppenentCardRank,
                                        oppenentCardSuit
                                )),vira,1)
                                .botInfo(botCards,winningScore)
                                .opponentScore(losingScore)
                                .opponentCard(TrucoCard.of(
                                        oppenentCardRank,
                                        oppenentCardSuit
                                ))
                                .build();

                        boolean botDecidesIfRaises =  camaleao.decideIfRaises(intel);
                        TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                        SoftAssertions softly = new SoftAssertions();
                        softly.assertThat(botDecidesIfRaises).isEqualTo(expectedDecideIfRaises);
                        //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                        softly.assertAll();
                    }

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | responseToTruco | expectedCardRank | expectedCardSuit
                            //almost absolute victory
                            "FOUR, HEARTS, true",
                            //almost certain victory
                            "TWO, HEARTS, true",

                            //almost certain defeat
                            "KING, HEARTS, true",
                            "FIVE, HEARTS, true",

                            //high changes opponent runs from truco
                            //"KING, CLUBS, KING, HEARTS, true, true",
                    })
                    @DisplayName("opponent played invincible card")
                    void opponentPlayedInvincibleCard(CardRank cardRank1, CardSuit cardSuit1, boolean expectedDecideIfRaises) {
                        List<TrucoCard> botCards = List.of(
                                TrucoCard.of(cardRank1,cardSuit1)
                        );
                        CardRank oppenentCardRank;
                        CardSuit oppenentCardSuit;
                        if (getGreatestCard(botCards, vira).isCopas(vira)) {
                            oppenentCardRank = CardRank.FOUR;
                            oppenentCardSuit = CardSuit.CLUBS;
                        } else {
                            oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()+1];
                            oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()+1];
                        }

                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                        oppenentCardRank,
                                        oppenentCardSuit
                                )),vira,1)
                                .botInfo(botCards,winningScore)
                                .opponentScore(losingScore)
                                .opponentCard(TrucoCard.of(
                                        oppenentCardRank,
                                        oppenentCardSuit
                                ))
                                .build();

                        boolean botRaiseResponse =  camaleao.decideIfRaises(intel);
                        TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                        SoftAssertions softly = new SoftAssertions();
                        softly.assertThat(botRaiseResponse).isEqualTo(expectedDecideIfRaises);
                        //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                        softly.assertAll();
                    }

                }

                @Nested
                @DisplayName("bot lost first hand and won in second")
                class botLostFirstHandAndWonInSecond{

                    List<GameIntel.RoundResult> roundResults = List.of(
                            GameIntel.RoundResult.LOST,
                            GameIntel.RoundResult.WON
                    );

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | expectedRaiseResponse
                            //almost absolute victory
                            "FOUR, CLUBS,  true",
                            //almost certain victory
                            "TWO, CLUBS, false",
                            //almost certain defeat
                            "KING, CLUBS, false",
                            "FIVE, CLUBS, false",
                    })
                    @DisplayName("bot thinks if calls truco")
                    void botThinksIfCallsTruco(
                            CardRank cardRank1, CardSuit cardSuit1,
                            boolean expectedDecideIfRaises
                    ) {
                        List<TrucoCard> cards = Arrays.asList(
                                TrucoCard.of(cardRank1, cardSuit1)
                        );
                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira), vira, 1)
                                .botInfo(cards, winningScore)
                                .opponentScore(losingScore).build();

                        boolean response = camaleao.decideIfRaises(intel);

                        assertEquals(expectedDecideIfRaises, response);
                    }

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                            //almost absolute victory
                            "FOUR, CLUBS, 1",
                            //almost certain victory
                            "TWO, CLUBS, 0",

                            //almost certain defeat
                            "KING, CLUBS, -1",
                            "FIVE, CLUBS, -1",

                            //high changes opponent runs from truco
                            //"KING, CLUBS, KING, HEARTS, true, true",
                    })
                    @DisplayName("opponent played vincible card")
                    void opponentCallsTruco(CardRank cardRank1, CardSuit cardSuit1, int expectedGetRaiseResponse) {
                        List<TrucoCard> botCards = List.of(
                                TrucoCard.of(cardRank1,cardSuit1)
                        );

                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira),vira,1)
                                .botInfo(botCards,winningScore)
                                .opponentScore(losingScore)
                                .build();

                        int botGetRaiseResponse =  camaleao.getRaiseResponse(intel);

                        SoftAssertions softly = new SoftAssertions();
                        softly.assertThat(botGetRaiseResponse).isEqualTo(expectedGetRaiseResponse);
                        //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                        softly.assertAll();
                    }

                }
            }

            @Nested
            @DisplayName("bot is lossing game")
            class botIsLossingGame{

                @Nested
                @DisplayName("bot won first hand and lost in second")
                class botWonFirstHandAndLostInSecond{

                    List<GameIntel.RoundResult> roundResults = List.of(
                            GameIntel.RoundResult.WON,
                            GameIntel.RoundResult.LOST
                    );

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | expectedRaiseResponse
                            //almost absolute victory
                            "FOUR, CLUBS,  1",
                            //almost certain victory
                            "TWO, CLUBS, 0",
                            //almost certain defeat
                            "KING, CLUBS, -1",
                            "FIVE, CLUBS, -1",
                    })
                    @DisplayName("opponent calls truco")
                    void OppenentCallsTruco(
                            CardRank cardRank1, CardSuit cardSuit1,
                            int expectedResponse
                    ) {
                        List<TrucoCard> cards = Arrays.asList(
                                TrucoCard.of(cardRank1, cardSuit1)
                        );
                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira), vira, 3)
                                .botInfo(cards, losingScore)
                                .opponentScore(winningScore).build();

                        int response = camaleao.getRaiseResponse(intel);

                        assertEquals(expectedResponse, response);
                    }

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                            //almost absolute victory
                            "FOUR, CLUBS, true",
                            //almost certain victory
                            "TWO, CLUBS, true",

                            //almost certain defeat
                            "KING, CLUBS, true",
                            "FIVE, CLUBS, true",

                            //high changes opponent runs from truco
                            //"KING, CLUBS, KING, HEARTS, true, true",
                    })
                    @DisplayName("opponent played vincible card")
                    void opponentPlayedVincibleCard(CardRank cardRank1, CardSuit cardSuit1, boolean expectedDecideIfRaises) {
                        List<TrucoCard> botCards = List.of(
                                TrucoCard.of(cardRank1,cardSuit1)
                        );
                        CardRank oppenentCardRank;
                        CardSuit oppenentCardSuit;
                        if (getGreatestCard(botCards, vira).getRank().value() == 1) {
                            oppenentCardRank = CardRank.THREE;
                            oppenentCardSuit = CardSuit.DIAMONDS;
                        } else {
                            oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()-1];
                            oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()-1];
                        }

                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                        oppenentCardRank,
                                        oppenentCardSuit
                                )),vira,1)
                                .botInfo(botCards,losingScore)
                                .opponentScore(winningScore)
                                .opponentCard(TrucoCard.of(
                                        oppenentCardRank,
                                        oppenentCardSuit
                                ))
                                .build();

                        boolean botDecidesIfRaises =  camaleao.decideIfRaises(intel);
                        TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                        SoftAssertions softly = new SoftAssertions();
                        softly.assertThat(botDecidesIfRaises).isEqualTo(expectedDecideIfRaises);
                        //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                        softly.assertAll();
                    }

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | responseToTruco | expectedCardRank | expectedCardSuit
                            //almost absolute victory
                            "FOUR, HEARTS, true",
                            //almost certain victory
                            "TWO, HEARTS, true",

                            //almost certain defeat
                            "KING, HEARTS, true",
                            "FIVE, HEARTS, true",

                            //high changes opponent runs from truco
                            //"KING, CLUBS, KING, HEARTS, true, true",
                    })
                    @DisplayName("opponent played invincible card")
                    void opponentPlayedInvincibleCard(CardRank cardRank1, CardSuit cardSuit1, boolean expectedDecideIfRaises) {
                        List<TrucoCard> botCards = List.of(
                                TrucoCard.of(cardRank1,cardSuit1)
                        );
                        CardRank oppenentCardRank;
                        CardSuit oppenentCardSuit;
                        if (getGreatestCard(botCards, vira).isCopas(vira)) {
                            oppenentCardRank = CardRank.FOUR;
                            oppenentCardSuit = CardSuit.CLUBS;
                        } else {
                            oppenentCardRank = CardRank.values()[getGreatestCard(botCards, vira).getRank().value()+1];
                            oppenentCardSuit = CardSuit.values()[getGreatestCard(botCards, vira).getSuit().ordinal()+1];
                        }

                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira,vira,vira, TrucoCard.of(
                                        oppenentCardRank,
                                        oppenentCardSuit
                                )),vira,1)
                                .botInfo(botCards,losingScore)
                                .opponentScore(winningScore)
                                .opponentCard(TrucoCard.of(
                                        oppenentCardRank,
                                        oppenentCardSuit
                                ))
                                .build();

                        boolean botRaiseResponse =  camaleao.decideIfRaises(intel);
                        TrucoCard botChosenCard = camaleao.chooseCard(intel).content();

                        SoftAssertions softly = new SoftAssertions();
                        softly.assertThat(botRaiseResponse).isEqualTo(expectedDecideIfRaises);
                        //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                        softly.assertAll();
                    }

                }

                @Nested
                @DisplayName("bot lost first hand and won in second")
                class botLostFirstHandAndWonInSecond{

                    List<GameIntel.RoundResult> roundResults = List.of(
                            GameIntel.RoundResult.LOST,
                            GameIntel.RoundResult.WON
                    );

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | expectedRaiseResponse
                            //almost absolute victory
                            "FOUR, CLUBS,  true",
                            //almost certain victory
                            "TWO, CLUBS, false",
                            //almost certain defeat
                            "KING, CLUBS, false",
                            "FIVE, CLUBS, false",
                    })
                    @DisplayName("bot thinks if calls truco")
                    void botThinksIfCallsTruco(
                            CardRank cardRank1, CardSuit cardSuit1,
                            boolean expectedDecideIfRaises
                    ) {
                        List<TrucoCard> cards = Arrays.asList(
                                TrucoCard.of(cardRank1, cardSuit1)
                        );
                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira), vira, 1)
                                .botInfo(cards, losingScore)
                                .opponentScore(winningScore).build();

                        boolean response = camaleao.decideIfRaises(intel);


                        assertEquals(expectedDecideIfRaises, response);
                    }

                    @ParameterizedTest
                    @CsvSource({
                            // cardRank1 | cardSuit1 | cardRank2 | cardSuit2 | decideIfRaise | expectedCardRank | expectedCardSuit
                            //almost absolute victory
                            "FOUR, CLUBS, 1",
                            //almost certain victory
                            "TWO, CLUBS, 0",

                            //almost certain defeat
                            "KING, CLUBS, -1",
                            "FIVE, CLUBS, -1",

                            //high changes opponent runs from truco
                            //"KING, CLUBS, KING, HEARTS, true, true",
                    })
                    @DisplayName("opponent played vincible card")
                    void opponentCallsTruco(CardRank cardRank1, CardSuit cardSuit1, int expectedGetRaiseResponse) {
                        List<TrucoCard> botCards = List.of(
                                TrucoCard.of(cardRank1,cardSuit1)
                        );

                        GameIntel intel = GameIntel.StepBuilder.with()
                                .gameInfo(roundResults,List.of(vira),vira,1)
                                .botInfo(botCards,losingScore)
                                .opponentScore(winningScore)
                                .build();

                        int botGetRaiseResponse =  camaleao.getRaiseResponse(intel);

                        SoftAssertions softly = new SoftAssertions();
                        softly.assertThat(botGetRaiseResponse).isEqualTo(expectedGetRaiseResponse);
                        //softly.assertThat(isHighChangesOpponentRunFromTruco(intel)).isEqualTo(highChangesOpponentRunFromTruco);
                        softly.assertAll();
                    }

                }
            }
        }

    }
}
