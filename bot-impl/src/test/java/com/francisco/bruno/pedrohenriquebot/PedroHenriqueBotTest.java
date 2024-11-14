package com.francisco.bruno.pedrohenriquebot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.model.CardToPlay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardSuit.*;
import static com.bueno.spi.model.GameIntel.RoundResult.LOST;
import static com.bueno.spi.model.GameIntel.RoundResult.WON;
import static org.junit.jupiter.api.Assertions.*;

class PedroHenriqueBotTest {

    private PedroHenriqueBot sut;
    private GameIntel.StepBuilder intel;

    @BeforeEach
    void setUp() {
        sut = new PedroHenriqueBot();
    }

    @Test
    @DisplayName("Should return correct bot name")
    void shouldReturnCorrectBotName() {
        assertEquals("PedroHenrique", sut.getName());
    }

    @Nested
    @DisplayName("Testing getMaoDeOnzeResponse")
    class GetMaoDeOnzeResponseTest {
        @Test
        @DisplayName("Should accept Mao de Onze with 2 manilhas")
        void shouldAcceptMaoDeOnzeWith2Manilhas() {
            TrucoCard vira = TrucoCard.of(ACE, SPADES);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(ACE, DIAMONDS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards,11)
                    .opponentScore(8);

            assertTrue(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Decline Mao de Onze with weak hand")
        void declineMaoDeOnzeWithWeakHand() {
            TrucoCard vira = TrucoCard.of(SEVEN, DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(SIX, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards,11)
                    .opponentScore(6);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Accept Mao de Onze when opponent is close to winning")
        void acceptMaoDeOnzeWhenOpponentIsCloseToWinning() {
            TrucoCard vira = TrucoCard.of(QUEEN, HEARTS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(THREE, HEARTS),
                    TrucoCard.of(TWO, SPADES),
                    TrucoCard.of(JACK, DIAMONDS)
            );

            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira),vira,1)
                    .botInfo(botCards, 11)
                    .opponentScore(10);

            assertTrue(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Decline Mão de Onze when hand strength is low even if opponent is close to winning")
        void declineMaoDeOnzeWithWeakHandEvenIfOpponentClose() {
            TrucoCard vira = TrucoCard.of(KING, CLUBS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(SIX, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards, 11)
                    .opponentScore(10);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Accept Mão de Onze with 1 manilha and 2 high cards")
        void acceptMaoDeOnzeWithOneManilhaAndTwoHighCards() {
            TrucoCard vira = TrucoCard.of(JACK, HEARTS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(THREE, DIAMONDS),
                    TrucoCard.of(KING, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards, 11)
                    .opponentScore(7);

            assertTrue(sut.getMaoDeOnzeResponse(intel.build()));
        }

        @Test
        @DisplayName("Decline Mão de Onze when hand strength average is low")
        void declineMaoDeOnzeWithLowHandStrengthAverage() {
            TrucoCard vira = TrucoCard.of(KING, SPADES);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(SEVEN, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(SIX, DIAMONDS)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira,1)
                    .botInfo(botCards, 11)
                    .opponentScore(9);

            assertFalse(sut.getMaoDeOnzeResponse(intel.build()));
        }
    }

    @Nested
    @DisplayName("Testing chooseCard")
    class ChooseCardTest {

        @Nested
        @DisplayName("First Round")
        class FirstRound {
            @Test
            @DisplayName("Should use intermediate card when first to play")
            void shouldUseIntermediateCardWhenFirstToPlay() {
                TrucoCard vira = TrucoCard.of(ACE, SPADES);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(SEVEN, CLUBS),
                        TrucoCard.of(THREE, DIAMONDS)
                );
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira),vira,1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);
                CardToPlay chosenCard = sut.chooseCard(intel.build());
                assertEquals(CardToPlay.of(botCards.get(1)), chosenCard);
            }

            @Test
            @DisplayName("Should play minimal card to win when opponent plays first")
            void shouldPlayMinimalCardToWinWhenOpponentPlaysFirst() {
                TrucoCard vira = TrucoCard.of(ACE, SPADES);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(SEVEN, CLUBS),
                        TrucoCard.of(THREE, DIAMONDS)
                );
                TrucoCard opponentCard = TrucoCard.of(FIVE, CLUBS);
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(Collections.emptyList(), new ArrayList<>(), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                CardToPlay chosenCard = sut.chooseCard(intel.build());

                Optional<TrucoCard> expectedCardOptional = botCards.stream()
                        .filter(card -> card.relativeValue(vira) > opponentCard.relativeValue(vira))
                        .min(Comparator.comparingInt(card -> card.relativeValue(vira)));

                TrucoCard expectedCard = expectedCardOptional
                        .orElseGet(() -> botCards.stream()
                                .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                                .orElseThrow(() -> new IllegalStateException("No cards available")));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }

            @Test
            @DisplayName("Should choose weakest card if cannot win opponent")
            void shouldChooseWeakestCardIfCannotWinOpponent() {
                TrucoCard vira = TrucoCard.of(ACE, SPADES);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(FIVE, CLUBS),
                        TrucoCard.of(SIX, DIAMONDS)
                );
                TrucoCard opponentCard = TrucoCard.of(THREE, CLUBS);
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(),Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);
                CardToPlay chosenCard = sut.chooseCard(intel.build());

                TrucoCard expectedCard = botCards.stream()
                        .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                        .orElse(botCards.get(0));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }

            @Test
            @DisplayName("Should play second strongest card when strong hand")
            void shouldPlaySecondStrongestCardWhenStartingFirstRound() {
                TrucoCard vira = TrucoCard.of(FOUR, HEARTS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(ACE, SPADES),
                        TrucoCard.of(TWO, CLUBS),
                        TrucoCard.of(THREE, DIAMONDS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);
                CardToPlay chosenCard = sut.chooseCard(intel.build());

                List<TrucoCard> sortedCards = botCards.stream()
                        .sorted((card1, card2) -> Integer.compare(card2.relativeValue(vira), card1.relativeValue(vira)))
                        .collect(Collectors.toList());

                TrucoCard expectedCard = sortedCards.get(1);

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }
        }

        @Nested
        @DisplayName("Second Round")
        class SecondRound {
            @Test
            @DisplayName("Should play weak card if winner of the first round")
            void shouldPlayWeakCardIfWinnerOfTheFirstRound() {
                TrucoCard vira = TrucoCard.of(KING, CLUBS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(SEVEN, HEARTS),
                        TrucoCard.of(SIX, SPADES)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(WON), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                CardToPlay chosenCard = sut.chooseCard(intel.build());
                TrucoCard expectedCard = botCards.stream()
                        .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                        .orElse(botCards.get(0));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }

            @Test
            @DisplayName("Should play min card to win when lost first round")
            void shouldPlayMinCardToWinWhenLostFirstRound() {
                TrucoCard vira = TrucoCard.of(KING, CLUBS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(SEVEN, HEARTS),
                        TrucoCard.of(SIX, SPADES)
                );
                TrucoCard opponentCard = TrucoCard.of(FIVE, DIAMONDS);

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(LOST), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                CardToPlay chosenCard = sut.chooseCard(intel.build());
                TrucoCard expectedCard = botCards.stream()
                        .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                        .orElse(botCards.get(1));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }

            @Test
            @DisplayName("Should play strongest card if opponent did not play")
            void shouldPlayStrongestCardIfOpponentDidNotPlay() {
                TrucoCard vira = TrucoCard.of(KING, HEARTS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(TWO, SPADES),
                        TrucoCard.of(ACE, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(LOST), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);
                CardToPlay chosenCard = sut.chooseCard(intel.build());

                TrucoCard expectedCard = botCards.stream()
                        .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                        .orElse(botCards.get(0));

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }
        }

        @Nested
        @DisplayName("Third Round")
        class ThirdRound {
            @Test
            @DisplayName("Should play last card")
            void shouldPlayLastCard() {
                TrucoCard vira = TrucoCard.of(ACE, DIAMONDS);
                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(TWO, HEARTS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(WON, LOST), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                CardToPlay chosenCard = sut.chooseCard(intel.build());
                TrucoCard expectedCard = TrucoCard.of(TWO, HEARTS);

                assertEquals(CardToPlay.of(expectedCard), chosenCard);
            }
        }
    }

    @Nested
    @DisplayName("Testing decideIfRaises")
    class DecideIfRaisesTest {

        @Nested
        @DisplayName("First Round")
        class FirstRound {
            @Test
            @DisplayName("Should raise when has manilha in first round")
            void shouldRaiseFirstRoundWithManilha() {
                TrucoCard vira = TrucoCard.of(THREE, SPADES);

                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(ACE, CLUBS),
                        TrucoCard.of(SIX, DIAMONDS)
                );
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(5);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Do not raise with weak hand in first round")
            void doNotRaiseFirstRoundWeakHand() {
                TrucoCard vira = TrucoCard.of(SEVEN, DIAMONDS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(FIVE, CLUBS),
                        TrucoCard.of(SIX, SPADES)
                );
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(5);

                assertFalse(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Raise when opponent is close to winning")
            void raiseFirstRoundOpponentCloseToWinning() {
                TrucoCard vira = TrucoCard.of(KING, CLUBS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(QUEEN, HEARTS),
                        TrucoCard.of(JACK, CLUBS),
                        TrucoCard.of(KING, SPADES)
                );
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(10);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Do not raise when own score is high and ahead")
            void doNotRaiseWhenBotAhead() {
                TrucoCard vira = TrucoCard.of(QUEEN, HEARTS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(TWO, HEARTS),
                        TrucoCard.of(THREE, CLUBS),
                        TrucoCard.of(ACE, SPADES)
                );
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 10)
                        .opponentScore(8);

                assertFalse(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Decide to bluff when opponent is aggressive")
            void decideToBluffWhenOpponentAggressive() {
                sut.opponentRaiseCount = 4;

                TrucoCard vira = TrucoCard.of(SIX, HEARTS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(FIVE, CLUBS),
                        TrucoCard.of(SIX, SPADES)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 5)
                        .opponentScore(6);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should not raise when opponent is not aggressive")
            void shouldNotRaiseWhenOpponentNotAggressive() {

                sut.opponentRaiseCount = 1;

                TrucoCard vira = TrucoCard.of(SIX, HEARTS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(FOUR, HEARTS),
                        TrucoCard.of(FIVE, CLUBS),
                        TrucoCard.of(SIX, SPADES)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 5)
                        .opponentScore(6);

                assertFalse(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should raise with manilha and high card")
            void shouldRaiseWithManilhaAndHighCard() {
                TrucoCard vira = TrucoCard.of(ACE, SPADES);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(ACE, SPADES),
                        TrucoCard.of(TWO, CLUBS),
                        TrucoCard.of(SEVEN, HEARTS)
                );
                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                        .botInfo(botCards, 0)
                        .opponentScore(5);

                assertTrue(sut.decideIfRaises(intel.build()));
            }
        }

        @Nested
        @DisplayName("Second Round")
        class SecondRound {
            @Test
            @DisplayName("Should raise if won first round with high cards")
            void shouldRaiseIfWonFirstRoundWithHighCards() {
                TrucoCard vira = TrucoCard.of(ACE, SPADES);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(TWO, HEARTS),
                        TrucoCard.of(THREE, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(WON), Collections.singletonList(vira), vira, 2)
                        .botInfo(botCards, 0)
                        .opponentScore(7);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should not raise if score is high and is far winning")
            void shouldNotRaiseIfScoreIsHighAndIsFarWinning() {
                TrucoCard vira = TrucoCard.of(QUEEN, HEARTS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(KING, SPADES),
                        TrucoCard.of(QUEEN, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(WON), Collections.singletonList(vira), vira, 2)
                        .botInfo(botCards, 10)
                        .opponentScore(8);

                assertFalse(sut.decideIfRaises(intel.build()));
            }
            @Test
            @DisplayName("Should raise if have manilha or high cards")
            void shouldRaiseSecondRoundWithManilhaOrTwoHighCards() {
                TrucoCard vira = TrucoCard.of(KING, CLUBS);
                List<TrucoCard> botCards = Arrays.asList(
                        TrucoCard.of(ACE, SPADES),
                        TrucoCard.of(TWO, HEARTS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(LOST), Collections.singletonList(vira), vira, 2)
                        .botInfo(botCards, 5)
                        .opponentScore(6);

                assertTrue(sut.decideIfRaises(intel.build()));
            }
        }

        @Nested
        @DisplayName("Third Round")
        class ThirdRound {
            @Test
            @DisplayName("Should raise with high cards")
            void shouldRaiseWithHighCards() {
                TrucoCard vira = TrucoCard.of(KING, HEARTS);
                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(THREE, CLUBS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(WON, LOST), Collections.singletonList(vira), vira, 3)
                        .botInfo(botCards, 0)
                        .opponentScore(0);

                assertTrue(sut.decideIfRaises(intel.build()));
            }

            @Test
            @DisplayName("Should not raise if far winning")
            void shouldNotRaiseIfFarWinning() {
                TrucoCard vira = TrucoCard.of(SEVEN, DIAMONDS);
                List<TrucoCard> botCards = Collections.singletonList(
                        TrucoCard.of(FIVE, HEARTS)
                );

                intel = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(LOST, WON), Collections.singletonList(vira), vira, 3)
                        .botInfo(botCards, 10)
                        .opponentScore(8);

                assertFalse(sut.decideIfRaises(intel.build()));
            }
        }
    }

    @Nested
    @DisplayName("Testing getRaiseResponse")
    class GetRaiseResponseTest {
        @Test
        @DisplayName("Accept raise with strong hand in first round")
        void acceptRaiseStrongHandFirstRound() {
            TrucoCard vira = TrucoCard.of(THREE, SPADES);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(ACE, HEARTS),
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(THREE, DIAMONDS)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(5);

            int response = sut.getRaiseResponse(intel.build());
            assertEquals(0, response);
        }

        @Test
        @DisplayName("Decline raise with weak hand in first round")
        void declineRaiseWeakHandFirstRound() {
            TrucoCard vira = TrucoCard.of(SEVEN, DIAMONDS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(FOUR, HEARTS),
                    TrucoCard.of(FIVE, CLUBS),
                    TrucoCard.of(SIX, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(5);

            int response = sut.getRaiseResponse(intel.build());
            assertEquals(-1, response);
        }

        @Test
        @DisplayName("Accept raise with one manilha in hand")
        void acceptRaiseWithOneManilha() {
            TrucoCard vira = TrucoCard.of(KING, HEARTS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(ACE, HEARTS),
                    TrucoCard.of(SEVEN, CLUBS),
                    TrucoCard.of(SIX, SPADES)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(7);

            int response = sut.getRaiseResponse(intel.build());
            assertEquals(0, response);
        }

        @Test
        @DisplayName("Re-raise when have two manilhas")
        void reRaiseWithTwoManilhas() {
            TrucoCard vira = TrucoCard.of(ACE, HEARTS);
            List<TrucoCard> botCards = Arrays.asList(
                    TrucoCard.of(TWO, HEARTS),
                    TrucoCard.of(TWO, CLUBS),
                    TrucoCard.of(SEVEN, CLUBS)
            );
            intel = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), Collections.singletonList(vira), vira, 1)
                    .botInfo(botCards, 0)
                    .opponentScore(5);

            int response = sut.getRaiseResponse(intel.build());
            assertEquals(1, response);
        }
    }
}