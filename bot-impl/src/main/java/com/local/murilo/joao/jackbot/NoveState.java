package com.local.murilo.joao.jackbot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

public class NoveState implements RaiseResponseStatePattern{
    JackBot bot = new JackBot();

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int roundNumber = bot.getRoundNumber(intel);

        return switch (roundNumber) {
            case 1 ->
                // Lógica específica para o primeiro round
                    handleFirstRound(intel);
            case 2 ->
                // Lógica específica para o segundo round
                    handleSecondRound(intel);
            case 3 ->
                // Lógica específica para o terceiro round
                    handleThirdRound(intel);
            default -> throw new IllegalArgumentException("Número de round inválido: " + roundNumber);
        };
    }

    @Override
    public int handleFirstRound(GameIntel intel) {
        // Verificar o tipo de mão que o bot possui
        if (bot.hasSuperPlusHand(intel)) {
            // Se tiver uma mão super plus, aceita o truco
            return 0; // Aceita
        } if (bot.hasSuperHand(intel)) {
            // Se tiver uma mão super, desiste do truco
            return -1; // Quit
        } if (bot.hasGoodHand(intel) || bot.hasStrongHand(intel) || bot.hasOkHand(intel) || bot.hasBadHand(intel) || bot.hasTheWorstHand(intel)) {
            // Se tiver uma mão boa, forte, ok, ruim ou péssima, desiste do truco
            return -1; // Quit
        } if (bot.hasExtremeHand(intel) || bot.hasInvincibleHand(intel)) {
            // Se tiver uma mão extrema ou invencível, sobe o valor do truco para doze
            return 1; // Re-raise
        } else {
            // Caso contrário, recuse, para garantir que funcione.
            return -1; // Fuga por padrão
        }
    }

    @Override
    public int handleSecondRound(GameIntel intel) {
        if (hasStrongSecondRoundHand(intel)) {
            return 1; // Re-raise
        } if (hasModerateSecondRoundHand(intel)) {
            return 0; // Aceita
        } else {
            return -1; // Quit
        }
    }

    @Override
    public boolean hasStrongSecondRoundHand(GameIntel intel) {
        return (bot.hasManilha(intel) && bot.wonFirstRound(intel)) ||
                (bot.getCurrentHandValue(intel) >= 21 && bot.drewFirstRound(intel)) ||
                (bot.hasZap(intel) && bot.lostFirstRound(intel)) ||
                (bot.hasZap(intel) && bot.drewFirstRound(intel));
    }

    @Override
    public boolean hasModerateSecondRoundHand(GameIntel intel) {
        return (bot.getCurrentHandValue(intel) >= 17 && bot.drewFirstRound(intel)) ||
                (bot.getCurrentHandValue(intel) >= 21 && bot.hasManilha(intel) && bot.lostFirstRound(intel)) ||
                (bot.wonFirstRound(intel) && bot.hasManilha(intel) && bot.getCurrentHandValue(intel) >= 17) ||
                (bot.getCurrentHandValue(intel) < 21 && bot.hasZap(intel) && bot.lostFirstRound(intel));
    }

    @Override
    public int handleThirdRound(GameIntel intel) {
        if (intel.getCards().size() == 1) {
            if (intel.getCards().get(0).isZap(intel.getVira())) return 1;
        }
        TrucoCard cardPlayed = intel.getOpenCards().get(intel.getOpenCards().size() - 1);
        if (cardPlayed.isZap(intel.getVira())) return 1;
        if (bot.drewFirstRound(intel) || bot.drewSecondRound(intel) && bot.hasZap(intel)) return 1;
        if (bot.wonFirstRound(intel) || bot.wonSecondRound(intel) && bot.hasZap(intel)) return 1;
        return -1;
    }
}
