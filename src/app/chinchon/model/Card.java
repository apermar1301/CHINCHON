package app.chinchon.model;

import app.chinchon.game.GameConstants;
import java.util.Objects;

/**
 * Representa una carta de la baraja espanola.
 */
public class Card implements Comparable<Card> {

    private final Suit suit;
    private final int value;

    /**
     * Crea una carta con el palo y valor indicados.
     *
     * @param suit  el palo de la carta
     * @param value el valor numerico de la carta
     */
    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * Obtiene el palo de la carta.
     *
     * @return el palo
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Obtiene el valor numerico de la carta.
     *
     * @return el valor
     */
    public int getValue() {
        return value;
    }

    /**
     * Obtiene los puntos que vale la carta (coincide con su valor).
     *
     * @return los puntos de la carta
     */
    public int getPoints() {
        return value;
    }

    /**
     * Obtiene el indice ordinal para determinar consecutividad.
     * Mapeo: 1-7 se mapean a 0-6, 10-12 se mapean a 7-9.
     *
     * @return el indice de orden
     */
    public int getOrderIndex() {
        return (value <= GameConstants.ORDER_THRESHOLD)
                ? value - 1
                : value - GameConstants.ORDER_OFFSET;
    }

    @Override
    public int compareTo(Card other) {
        int suitCompare = this.suit.compareTo(other.suit);
        return (suitCompare != 0)
                ? suitCompare
                : Integer.compare(this.getOrderIndex(), other.getOrderIndex());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Card other)) {
            return false;
        }
        return this.value == other.value && this.suit == other.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, value);
    }

    @Override
    public String toString() {
        return value + " de " + suit.getDisplayName();
    }
}
