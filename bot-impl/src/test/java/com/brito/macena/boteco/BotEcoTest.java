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

package com.brito.macena.boteco;

import com.bueno.spi.model.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("BotEco Tests")
public class BotEcoTest {

    private BotEco botEco;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Starting BotEco tests.");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("All BotEco tests finished.");
    }

    @BeforeEach
    void setUp() {
        botEco = new BotEco();
    }

    @Nested
    @DisplayName("Mão de Onze tests")
    class MaoDeOnzeTests {

        @Test
        @DisplayName("Should accept hand of eleven when hand power is greater or equal to 17")
        void shouldAcceptMaoDeOnzeWhenHandPowerIsGreaterOrEqual17() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0)
                    .build();

            boolean response = botEco.getMaoDeOnzeResponse(intel);

            assertTrue(response);
        }

        @Test
        @DisplayName("Should not accept hand of eleven when hand power is less than 17")
        void shouldNotAcceptMaoDeOnzeWhenHandPowerIsLessThan17() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0)
                    .build();

            boolean response = botEco.getMaoDeOnzeResponse(intel);

            assertFalse(response);
        }
    }

    @Nested
    @DisplayName("Decide if raises tests")
    class DecideIfRaisesTests {
        @Test
        @DisplayName("Should call Truco when status is Excellent")
        void shouldCallTrucoWhenStatusIsExcellent() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0)
                    .build();

            boolean shouldRaise = botEco.decideIfRaises(intel);
            assertTrue(shouldRaise);
        }

        @Test
        @DisplayName("Should not call Truco when status is MEDIUM")
        void shouldNotCallTrucoWhenStatusIsMedium() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS)
            );
            TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS);
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(botEcoHand, 0)
                    .opponentScore(0)
                    .build();

            boolean shouldRaise = botEco.decideIfRaises(intel);
            assertFalse(shouldRaise);
        }
    }

    @Nested
    @DisplayName("Raise response tests")
    class RaiseResponseTests {
        @Test
        @DisplayName("Should increase truco when hand power is 17 or more and score difference is greater than 6")
        void shouldIncreaseTrucoWhenHandPowerIsSufficientAndScoreDifferenceIsHigh() {
            List<TrucoCard> botEcoHand = List.of(
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
            );
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            GameIntel intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(GameIntel.RoundResult.WON),
                            List.of(
                                    TrucoCard.of(CardRank.ACE, CardSuit.CLUBS),
                                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
                            ), vira, 1)
                    .botInfo(botEcoHand, 2)
                    .opponentScore(9)
                    .build();

            int response = botEco.getRaiseResponse(intel);

            assertThat(response).isEqualTo(1);
        }
    }
}

