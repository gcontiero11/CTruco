/*
 *  Copyright (C) 2024 Lucas Jundi Hikazudani - IFSP/SCL
 *  Copyright (C) 2024 Priscila de Luna Farias - IFSP/SCL
 *
 *  Contact: h <dot> jundi <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: luna <dot> p <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.luna.jundi.jokerBot.states;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public final class FirstToPlayRoundOneState implements RoundState {
    private final GameIntel intel;

    public FirstToPlayRoundOneState(GameIntel intel) {
        if (!(isValidRoundState().test(intel, 1)))
            throw new IllegalStateException("is not" + getClass().getSimpleName());
        this.intel = intel;
    }

    @Override
    public CardToPlay cardChoice() {
        return getBestCard().apply(intel);
    }

    @Override
    public boolean raiseDecision() {
        return raiseHandByMyCards(intel);
    }

    @Override
    public int raiseResponse() {
        return defaultRaiseResponse(intel);
    }
}
