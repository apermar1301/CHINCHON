package app.chinchon.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa la mano de un jugador con sus cartas.
 */
public class Hand {

    private final List<Card> cards;

    /**
     * Crea una mano vacia.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Anade una carta a la mano.
     *
     * @param card la carta a anadir
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Elimina una carta de la mano por referencia.
     *
     * @param card la carta a eliminar
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * Elimina y devuelve la carta en la posicion indicada.
     *
     * @param index la posicion de la carta (0-based)
     * @return la carta eliminada
     */
    public Card removeCardAt(int index) {
        return cards.remove(index);
    }

    /**
     * Obtiene una vista inmutable de las cartas de la mano.
     *
     * @return lista de cartas
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    /**
     * Obtiene la carta en la posicion indicada.
     *
     * @param index la posicion de la carta (0-based)
     * @return la carta
     */
    public Card getCard(int index) {
        return cards.get(index);
    }

    /**
     * Obtiene el numero de cartas en la mano.
     *
     * @return cantidad de cartas
     */
    public int size() {
        return cards.size();
    }

    /**
     * Ordena las cartas de la mano por palo y valor.
     */
    public void sort() {
        Collections.sort(cards);
    }

    /**
     * Elimina todas las cartas de la mano.
     */
    public void clear() {
        cards.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            sb.append(String.format("%d. %s", i + 1, cards.get(i)));
            if (i < cards.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
