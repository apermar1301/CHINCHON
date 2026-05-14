package app.chinchon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el monton de descarte donde se colocan las cartas descartadas.
 */
public class DiscardPile {

    private final List<Card> cards;

    /**
     * Crea un monton de descarte vacio.
     */
    public DiscardPile() {
        this.cards = new ArrayList<>();
    }

    /**
     * Coloca una carta en la parte superior del monton.
     *
     * @param card la carta a descartar
     */
    public void push(Card card) {
        cards.add(card);
    }

    /**
     * Retira y devuelve la carta de la parte superior.
     *
     * @return la carta retirada
     */
    public Card pop() {
        if (isEmpty()) {
            throw new IllegalStateException("El monton de descarte esta vacio");
        }
        return cards.removeLast();
    }

    /**
     * Consulta la carta de la parte superior sin retirarla.
     *
     * @return la carta superior
     */
    public Card peek() {
        if (isEmpty()) {
            throw new IllegalStateException("El monton de descarte esta vacio");
        }
        return cards.getLast();
    }

    /**
     * Comprueba si el monton esta vacio.
     *
     * @return true si no hay cartas
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Obtiene el numero de cartas en el monton.
     *
     * @return cantidad de cartas
     */
    public int size() {
        return cards.size();
    }

    /**
     * Retira todas las cartas excepto la superior y las devuelve.
     * Se utiliza para recargar el mazo cuando se agota.
     *
     * @return las cartas retiradas
     */
    public List<Card> removeAllExceptTop() {
        if (cards.size() <= 1) {
            return List.of();
        }
        Card top = cards.removeLast();
        List<Card> removed = new ArrayList<>(cards);
        cards.clear();
        cards.add(top);
        return removed;
    }
}
