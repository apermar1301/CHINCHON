package app.chinchon.model;

import app.chinchon.game.GameConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el mazo de cartas de la baraja espanola.
 */
public class Deck {

    private final List<Card> cards;

    /**
     * Crea un mazo con el numero de barajas indicado.
     *
     * @param deckCount numero de barajas a incluir
     */
    public Deck(int deckCount) {
        this.cards = new ArrayList<>();
        fillDeck(deckCount);
    }

    /**
     * Baraja las cartas del mazo aleatoriamente.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Roba la carta de la parte superior del mazo.
     *
     * @return la carta robada
     */
    public Card draw() {
        if (isEmpty()) {
            throw new IllegalStateException("El mazo esta vacio");
        }
        return cards.removeLast();
    }

    /**
     * Comprueba si el mazo esta vacio.
     *
     * @return true si no quedan cartas
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Obtiene el numero de cartas restantes en el mazo.
     *
     * @return cantidad de cartas
     */
    public int remainingCards() {
        return cards.size();
    }

    /**
     * Anade cartas al mazo.
     *
     * @param newCards las cartas a anadir
     */
    public void addCards(List<Card> newCards) {
        cards.addAll(newCards);
    }

    /**
     * Rellena el mazo con las cartas de las barajas indicadas.
     *
     * @param deckCount numero de barajas
     */
    private void fillDeck(int deckCount) {
        for (int d = 0; d < deckCount; d++) {
            for (Suit suit : Suit.values()) {
                for (int value : GameConstants.CARD_VALUES) {
                    cards.add(new Card(suit, value));
                }
            }
        }
    }
}
