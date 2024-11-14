package com.daniel.mateus.theroverbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class TheRoverTest {
    private TheRover theRover;
    private GameIntel.StepBuilder stepBuilder;

    @BeforeEach
    public void setup() {
        theRover = new TheRover();
    }

    @Nested
    @DisplayName("isPlayingFirst Tests")
    class isPlayingFirstTest {

        @Test
        @DisplayName("Should return false if player is playing second")
        void ShouldReturnFalseIfPlayerIsPlayingSecond() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(List.of(), 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertFalse(theRover.isPlayingFirst(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return true if player is playing first")
        void ShouldReturnTrueIfPlayerIsPlayingFirst() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(List.of(), 0)
                    .opponentScore(0);

            assertTrue(theRover.isPlayingFirst(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("getCurrentRound tests")
    class getCurrentRoundTest {

        @Test
        @DisplayName("Should return 1 if in the first round")
        void ShouldReturnOneIfInFirstRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(1, theRover.getCurrentRound(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return 2 if in the second round")
        void ShouldReturnTwoIfInSecondRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(2, theRover.getCurrentRound(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return 3 if in the third round")
        void ShouldReturnThreeIfInThirdRound() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(3, theRover.getCurrentRound(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Choose card first hand tests")
    class chooseCardFirstHandTest {

        @Nested
        @DisplayName("Playing first tests")
        class playingFirst {
            @Test
            @DisplayName("When player has one manilha one high card and one low card should return high card")
            void WhenPlayerHasOneManilhaOneHighCardAndOneLowCardShouldReturnHighCard() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.FIVE,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
                );
                 stepBuilder = GameIntel.StepBuilder.with()
                         .gameInfo(List.of(), List.of(),vira, 1)
                         .botInfo(cards, 0)
                         .opponentScore(0);
                 assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.SPADES), theRover.chooseCardFirstHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When player has two manilhas and one low card should return lower manilha")
            void WhenPlayerHasTwoManilhasAndOneLowCardShouldReturnLowerManilha() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.FIVE,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE,CardSuit.CLUBS),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
                );
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(),vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0);
                assertEquals(TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS), theRover.chooseCardFirstHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When player has three manilhas should return lower manilha")
            void WhenPlayerHasThreeManilhasShouldReturnLowerManilha() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.FIVE,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.FIVE,CardSuit.CLUBS),
                        TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS)
                );
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(),vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0);
                assertEquals(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS), theRover.chooseCardFirstHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When player has three regular card should return higher card")
            void WhenPlayerHasThreeRegularCardsShouldReturnHigherCard() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.ACE,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
                );
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(),vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0);
                assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.SPADES), theRover.chooseCardFirstHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When player has three cards of the same value return any of then")
            void WhenPlayerHasThreeCardsOfSameValue() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.ACE,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                );
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(),vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0);
                assertThat(theRover.chooseCardFirstHand(stepBuilder.build())).isIn(
                        TrucoCard.of(CardRank.ACE,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.ACE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                );
            }
        }

        @Nested
        @DisplayName("Playing second tests")
        class playingSecond {

            @Test
            @DisplayName("Should play only card that beat opponent card")
            void ShouldPlayOnlyCardThatBeatOpponentCard() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.ACE,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.SPADES), theRover.chooseCardFirstHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When two cards beat opponent card should use lowest card")
            void WhenTwoCardsBeatOpponentCardShouldUseLowestCard() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.CLUBS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.TWO,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.HEARTS), theRover.chooseCardFirstHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When none card can beat opponent card should play lowest card")
            void WhenNoneCardCanBeatOpponentCardShouldPlayLowestCard() {
                TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.TWO,CardSuit.HEARTS),
                        TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertEquals(TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS), theRover.chooseCardFirstHand(stepBuilder.build()));
            }
        }
    }

    @Nested
    @DisplayName("Get lowest card in hand that beat opponent card tests")
    class getLowestCardInHandThatBeatOpponentCardTests {
        @Test
        @DisplayName("When three cards beat opponent card should play lowest card")
        void WhenThreeCardsBeatOpponentCardShouldPlayLowestCard () {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.JACK, CardSuit.SPADES);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE,CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.HEARTS), theRover.getLowestCardInHandThatBeatOpponentCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("When nome card beat opponent card should return null")
        void WhenNoneCardBeatOpponentCardShouldReturnNull () {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.THREE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE,CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertNull(theRover.getLowestCardInHandThatBeatOpponentCard(stepBuilder.build()));
        }

        @Test
        @DisplayName("When two cards that tie beat opponent card should return either")
        void WhenTwoCardsThatTieBeatOpponentCardShouldReturnEither () {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE,CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertThat(theRover.getLowestCardInHandThatBeatOpponentCard(stepBuilder.build())).isIn(
                    TrucoCard.of(CardRank.THREE,CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE,CardSuit.SPADES)
            );
        }

        @Test
        @DisplayName("When opponent does not play card should return null")
        void WhenOpponentDoesNotPlayCardShouldReturnNull () {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE,CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertNull(theRover.getLowestCardInHandThatBeatOpponentCard(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Get lowest card in hand tests")
    class getLowestCardInHandTests {

        @Test
        @DisplayName("When three cards in hand should return lowest")
        void WhenThreeCardsInHandShouldReturnLowest () {
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FOUR,CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.KING,CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), theRover.getLowestCardInHand(stepBuilder.build()));
        }

        @Test
        @DisplayName("When two cards in hand tie should return either")
        void WhenTwoCardsInHandTieShouldReturnEither () {
            TrucoCard vira = TrucoCard.of(CardRank.TWO, CardSuit.HEARTS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.SIX,CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX,CardSuit.SPADES),
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertThat(theRover.getLowestCardInHand(stepBuilder.build())).isIn(
                    TrucoCard.of(CardRank.SIX,CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX,CardSuit.SPADES)
            );
        }

        @Test
        @DisplayName("When theres only one card in hand should return that card")
        void WhenTheresOnlyOneCardInHandShouldReturnThatCard () {
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.SEVEN,CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.SEVEN, CardSuit.CLUBS), theRover.getLowestCardInHand(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Choose card second hand tests")
    class chooseCardSecondHandTests {

        @Nested
        @DisplayName("Playing first")
        class playingFirst {

            @Test
            @DisplayName("When two cards in hand Should play lowest card")
            void WhenTwoCardsInHandShouldPlayLowestCard () {
                 TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
                 List<TrucoCard> cards = List.of(
                         TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                         TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS)
                 );

                 stepBuilder = GameIntel.StepBuilder.with()
                         .gameInfo(List.of(), List.of(), vira, 1)
                         .botInfo(cards, 0)
                         .opponentScore(0);
                 assertEquals(TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS), theRover.chooseCardSecondHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When both cards tie should play either")
            void WhenBothCardsTieShouldPlayEither() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.ACE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                );
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(),vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0);
                assertThat(theRover.chooseCardSecondHand(stepBuilder.build())).isIn(
                        TrucoCard.of(CardRank.ACE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS)
                );
            }

        }

        @Nested
        @DisplayName("Playing second")
        class PlayingSecond {

            @Test
            @DisplayName("When only one card beat opponent card should play that card")
            void WhenOnlyOneCardBeatOpponentCardShouldPlayThatCard () {
                TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.HEARTS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.TWO, CardSuit.CLUBS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.SPADES),theRover.chooseCardSecondHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When two cards beat opponent card should play lowest card")
            void WhenTwoCardsBeatOpponentCardShouldPlayLowestCard () {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.TWO,CardSuit.SPADES),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.SPADES),theRover.chooseCardSecondHand(stepBuilder.build()));
            }

            @Test
            @DisplayName("When two cards that tie beat opponent card should play either")
            void WhenTwoCardsThatTieBeatOpponentCardShouldPlayEither() {
                TrucoCard vira = TrucoCard.of(CardRank.QUEEN, CardSuit.HEARTS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.THREE,CardSuit.SPADES),
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                );
                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(),vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertThat(theRover.chooseCardSecondHand(stepBuilder.build())).isIn(
                        TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                        TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS)
                );
            }

            @Test
            @DisplayName("When no card beat opponent card should play first card on hand")
            void WhenNoCardBeatOpponentCardShouldPlayFirstCardOnHand() {
                TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);
                TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
                List<TrucoCard> cards = List.of(
                        TrucoCard.of(CardRank.TWO,CardSuit.DIAMONDS),
                        TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
                );

                stepBuilder = GameIntel.StepBuilder.with()
                        .gameInfo(List.of(), List.of(), vira, 1)
                        .botInfo(cards, 0)
                        .opponentScore(0)
                        .opponentCard(opponentCard);

                assertEquals(TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),theRover.chooseCardSecondHand(stepBuilder.build()));
            }
        }

    }

    @Nested
    @DisplayName("Choose card third hand tests")
    class chooseCardThirdHandTests {

        @Test
        @DisplayName("When third hand should play only card")
        void WhenThirdHandShouldPlayOnlyCard() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.KING,CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.KING, CardSuit.CLUBS), theRover.chooseCardThirdHand(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("Hand has card over relative value tests")
    class handHasCardOverRelativeValueTests {

        @Test
        @DisplayName("When hand has one card over relative value should return true")
        void WhenHandHasOneCardOverRelativeValueShouldReturnTrue() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE,CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING,CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.FOUR,CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertTrue(theRover.handHasCardOverRelativeValue(stepBuilder.build(), 8));
        }

        @Test
        @DisplayName("When hand has one card equal relative value should return false")
        void WhenHandHasOneCardEqualRelativeValueShouldReturnFalse() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE,CardSuit.CLUBS),
                    TrucoCard.of(CardRank.KING,CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE,CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertFalse(theRover.handHasCardOverRelativeValue(stepBuilder.build(), 9));
        }

        @Test
        @DisplayName("When hand has no card over relative value should return false")
        void WhenHandHasNoCardOverRelativeValueShouldReturnFalse() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE,CardSuit.CLUBS),
                    TrucoCard.of(CardRank.TWO,CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN,CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertFalse(theRover.handHasCardOverRelativeValue(stepBuilder.build(), 10));
        }
    }

    @Nested
    @DisplayName("Decide if raises")
    class decideIfRaisesTests {

        @Test
        @DisplayName("When in second hand and lost first hand should return false()")
        void WhenInSecondHandAndLostFirstHandShouldReturnsFalse() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);
            TrucoCard opponentCard = TrucoCard.of(CardRank.SIX, CardSuit.CLUBS);
            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0)
                    .opponentCard(opponentCard);

            assertFalse(theRover.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("When in first hand should return false")
        void WhenInFirstHandShouldReturnFalse() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertFalse(theRover.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("When won first round and have high card should return true")
        void WhenWonFirstRoundAndHaveHighCardShouldReturnTrue() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertTrue(theRover.decideIfRaises(stepBuilder.build()));
        }

        @Test
        @DisplayName("When in third round should return false")
        void WhenInThirdRoundShouldReturnFalse() {
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertFalse(theRover.decideIfRaises(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("count manilhas in hand")
    class countManilhasInHandTest{

        @Test
        @DisplayName("When has no manilha should return zero")
        void WhenHasNoManilhaShouldReturnZero(){
            TrucoCard vira = TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.TWO, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(0,theRover.countManilhasInHand(stepBuilder.build()));
        }
        @Test
        @DisplayName("When has one manilha should return one")
        void WhenHasOneManilhaShouldReturnOne(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(1,theRover.countManilhasInHand(stepBuilder.build()));
        }

        @Test
        @DisplayName("When has two manilha should return two")
        void WhenHasTwoManilhaShouldReturnTwo(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FIVE, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(2,theRover.countManilhasInHand(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("get lowest manilha in hand test")
    class getLowestManilhaInHandTest{

        @Test
        @DisplayName("When has three manilhas in hand should return lowest")
        void WhenHasThreeManilhasInHandSouldReturnLowest(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),theRover.getLowestManilhaInHand(stepBuilder.build()));
        }

        @Test
        @DisplayName("When has one manilha in hand should return that manilha")
        void WhenHasOneManilhaInHandShouldReturnThatManilha(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),theRover.getLowestManilhaInHand(stepBuilder.build()));
        }

        @Test
        @DisplayName("When has no manilha in hand should return null")
        void WhenHasNoManilhaInHandShouldReturnNull(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertNull(theRover.getLowestManilhaInHand(stepBuilder.build()));
        }

    }

    @Nested
    @DisplayName("Get highest card in hand tests")
    class getHighestCardInHandTest {

        @Test
        @DisplayName("When has three cards should return highest")
        void WhenHasThreeCardsShouldReturnHighest(){
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.THREE, CardSuit.SPADES),theRover.getHighestCardInHand(stepBuilder.build()));
        }

        @Test
        @DisplayName("When has two cards should return highest")
        void WhenHasTwoCardsShouldReturnHighest(){
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(TrucoCard.of(CardRank.ACE, CardSuit.SPADES),theRover.getHighestCardInHand(stepBuilder.build()));
        }

        @Test
        @DisplayName("When has two equals cards should return either")
        void WhenHasTwoEqualsCardsShouldReturnEither(){
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.JACK, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertThat(theRover.getHighestCardInHand(stepBuilder.build())).isIn(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
            );
        }
    }

    @Nested
    @DisplayName("Get mao de onze response tests")
    class getMaoDeOnzeResponseTest {

        @Test
        @DisplayName("When two high cards in hand should return true")
        void WhenTwoHighCardsInHandShouldReturnTrue(){
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertTrue(theRover.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("When Opponent Score Is Less Than Five Return True")
        void WhenOpponentScoreIsLessThanFiveReturnTrue(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(4);

            assertTrue(theRover.getMaoDeOnzeResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should Not Play With Low Cards And Opponent Score Higher Than Five")
        void ShouldNotPlayWithLowCardsAndOpponentScoreHigherThanFive() {
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.JACK, CardSuit.HEARTS),
                    TrucoCard.of(CardRank.FOUR, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SEVEN, CardSuit.SPADES)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(6);

            assertFalse(theRover.getMaoDeOnzeResponse(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Count cards in hand over relative value tests")
    class countCardsInHandOverRelativeValueTest {

        @Test
        @DisplayName("When Has Three Low Cards In Hand Should Return Zero")
        void WhenHasThreeLowCardsInHandShouldReturnZero() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(0,theRover.countCardsInHandOverRelativeValue(stepBuilder.build(), 9));
        }

        @Test
        @DisplayName("When Has Two Cards In Hand Over Relative Value Should Return Two")
        void WhenHasTwoCardsInHandOverRelativeValueShouldReturnTwo() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(2,theRover.countCardsInHandOverRelativeValue(stepBuilder.build(), 7));
        }

        @Test
        @DisplayName("When Has Three Cards In Hand Over Relative Value Should Return Three")
        void WhenHasThreeCardsInHandOverRelativeValueShouldReturnThree() {
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(3,theRover.countCardsInHandOverRelativeValue(stepBuilder.build(), 6));
        }

        @Test
        @DisplayName("When Has Two Cards In Hand On Relative Value Should Return Zero")
        void WhenHasTwoCardsInHandOnRelativeValueShouldReturnZero() {
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.ACE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(0,theRover.countCardsInHandOverRelativeValue(stepBuilder.build(), 7));
        }
    }

    @Nested
    @DisplayName("Get raise response tests")
    class getRaiseResponseTest {

        @Test
        @DisplayName("Should refused when all cards have relative values less than nine")
        void ShouldRefusedWhenAllCardsHaveRelativeValueLessThanNine() {
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.ACE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(-1,theRover.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should accept when won first hand and has card with relative value greater than eight")
        void ShouldAcceptWhenWonFirstHandAndHasCardWithRelativeValueGreaterThanEight() {
            List<GameIntel.RoundResult> results = List.of(GameIntel.RoundResult.WON);
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(results, List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(0,theRover.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should rise when won first hand and has two card with relative value greater than eight")
        void ShouldRiseWhenWonFirstHandAndHasTwoCardWithRelativeValueGreaterThanEight() {
            List<GameIntel.RoundResult> results = List.of(GameIntel.RoundResult.WON);
            TrucoCard vira = TrucoCard.of(CardRank.FIVE, CardSuit.HEARTS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS),
                    TrucoCard.of(CardRank.SIX, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(results, List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(1,theRover.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should accept in first hand if has two high cards")
        void ShouldAcceptInFirstHandIfHasTwoHighCards(){
            TrucoCard vira = TrucoCard.of(CardRank.FOUR, CardSuit.HEARTS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(0,theRover.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should rise in first hand if has two manilhas")
        void ShouldRiseInFirstHandIfHasTwoManilhas(){
            TrucoCard vira = TrucoCard.of(CardRank.SEVEN, CardSuit.HEARTS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.FIVE, CardSuit.CLUBS),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.SPADES),
                    TrucoCard.of(CardRank.QUEEN, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(1,theRover.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should Accept in third hand if has one high card")
        void ShouldAcceptInThirdHandIfHasOneHighCard(){
            TrucoCard vira = TrucoCard.of(CardRank.KING, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(0,theRover.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should Accept in third hand if has one manilha")
        void ShouldRiseInThirdHandIfHasOneManilha(){
            TrucoCard vira = TrucoCard.of(CardRank.ACE, CardSuit.SPADES);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.TWO, CardSuit.CLUBS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(List.of(), List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(1,theRover.getRaiseResponse(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should Accept when lost first hand and has two card with relative value greater than eight")
        void ShouldAcceptWhenLostFirstHandAndHasTwoCardWithRelativeValueGreaterThanEight() {
            List<GameIntel.RoundResult> results = List.of(GameIntel.RoundResult.LOST);
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);

            List<TrucoCard> cards = List.of(
                    TrucoCard.of(CardRank.THREE, CardSuit.SPADES),
                    TrucoCard.of(CardRank.KING, CardSuit.HEARTS)
            );

            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(results, List.of(), vira, 1)
                    .botInfo(cards, 0)
                    .opponentScore(0);

            assertEquals(0,theRover.getRaiseResponse(stepBuilder.build()));
        }
    }

    @Nested
    @DisplayName("Tests for wonFirstRound()")
    class wonFirstRoundTest {

        @Test
        @DisplayName("Should return false if loses first round")
        void ShouldReturnFalseIfLosesFirstRound() {
            List<GameIntel.RoundResult> results = List.of(GameIntel.RoundResult.LOST);
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(results, List.of(), vira, 1)
                    .botInfo(List.of(), 0)
                    .opponentScore(0);

            assertFalse(theRover.wonFirstRound(stepBuilder.build()));
        }

        @Test
        @DisplayName("Should return true if won first round")
        void ShouldReturnTrueIfWonFirstRound() {
            List<GameIntel.RoundResult> results = List.of(GameIntel.RoundResult.WON);
            TrucoCard vira = TrucoCard.of(CardRank.JACK, CardSuit.DIAMONDS);


            stepBuilder = GameIntel.StepBuilder.with()
                    .gameInfo(results, List.of(), vira, 1)
                    .botInfo(List.of(), 0)
                    .opponentScore(0);

            assertTrue(theRover.wonFirstRound(stepBuilder.build()));
        }
    }
}