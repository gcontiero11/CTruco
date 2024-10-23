/*
 *  Copyright (C) 2023 Mauricio Brito Teixeira - IFSP/SCL and Vinicius Eduardo Alves Macena - IFSP/SCL
 *  Contact: brito <dot> mauricio <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: macena <dot> v <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.brito.macena.boteco.utils;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Game Tests")
public class GameTest {

    private GameIntel intel;

    @BeforeAll
    static void setupAll() { System.out.println("Starting Game tests..."); }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finishing Game tests...");
    }

    @Nested
    @DisplayName("Tests for wonFirstRound method")
    class WonFirstRoundTests {
        @Test
        @DisplayName("Test wonFirstRound when first round is won")
        public void testWonFirstRoundWhenFirstRoundIsWon() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.wonFirstRound(intel);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Test wonFirstRound when first round is lost")
        public void testWonFirstRoundWhenFirstRoundIsLost() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(), null, 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.wonFirstRound(intel);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Test wonFirstRound when no rounds are played")
        public void testWonFirstRoundWhenNoRoundsArePlayed() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 0)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.wonFirstRound(intel);
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Tests for lostFirstRound method")
    class LostFirstRoundTests {
        @Test
        @DisplayName("Test lostFirstRound when first round is lost")
        public void testLostFirstRoundWhenFirstRoundIsLost() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.LOST), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.lostFirstRound(intel);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Test lostFirstRound when first round is won")
        public void testLostFirstRoundWhenFirstRoundIsWon() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.lostFirstRound(intel);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Test lostFirstRound when no rounds are played")
        public void testLostFirstRoundWhenNoRoundsArePlayed() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.lostFirstRound(intel);
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Tests for hasManilha method")
    class HasManilhaTests {
        @Test
        @DisplayName("Test hasManilha when there is no manilha")
        public void testHasManilhaWhenThereIsNoManilha() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES);
            TrucoCard card1 = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
            TrucoCard card2 = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);
            TrucoCard card3 = TrucoCard.of(CardRank.KING, CardSuit.CLUBS);
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(card1, card2, card3), vira, 2)
                    .botInfo(List.of(card1, card2, card3), 0)
                    .opponentScore(0)
                    .build();
            boolean result = Game.hasManilha(intel);
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Tests for isCriticalSituation method")
    class IsCriticalSituationTests {
        @Test
        @DisplayName("Returns true when score difference is 6 or more")
        void returnsTrueWhenScoreDifferenceIsSixOrMore() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 0)
                    .botInfo(List.of(), 4)
                    .opponentScore(10)
                    .build();

            assertThat(Game.isCriticalSituation(intel)).isTrue();
        }

        @Test
        @DisplayName("Returns true when hand points are 6 or more")
        void returnsTrueWhenHandPointsAreSixOrMore() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 6)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();

            assertThat(Game.isCriticalSituation(intel)).isTrue();
        }

        @Test
        @DisplayName("Returns false when score difference and hand points are less than 6")
        void returnsFalseWhenScoreDifferenceAndHandPointsAreLessThanSix() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 5)
                    .botInfo(List.of(), 5)
                    .opponentScore(10)
                    .build();

            assertThat(Game.isCriticalSituation(intel)).isFalse();
        }

        @Test
        @DisplayName("Returns true when score difference is exactly 6")
        void returnsTrueWhenScoreDifferenceIsExactlySix() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 0)
                    .botInfo(List.of(), 4)
                    .opponentScore(10)
                    .build();

            assertThat(Game.isCriticalSituation(intel)).isTrue();
        }

        @Test
        @DisplayName("Returns true when hand points are exactly 6")
        void returnsTrueWhenHandPointsAreExactlySix() {
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), null, 6)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .build();

            assertThat(Game.isCriticalSituation(intel)).isTrue();
        }
    }
}