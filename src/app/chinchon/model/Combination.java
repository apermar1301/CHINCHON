package app.chinchon.model;

import app.chinchon.game.GameConstants;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Representa una combinacion de cartas valida (grupo o escalera).
 */
public class Combination {

    private final List<Card> cards;
    private final CombinationType type;

    /**
     * Crea una combinacion con las cartas y tipo indicados.
     *
     * @param cards las cartas que forman la combinacion
     * @param type  el tipo de combinacion
     */
    public Combination(List<Card> cards, CombinationType type) {
        this.cards = List.copyOf(cards);
        this.type = type;
    }

    /**
     * Obtiene las cartas de la combinacion.
     *
     * @return lista inmutable de cartas
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Obtiene el tipo de combinacion.
     *
     * @return el tipo
     */
    public CombinationType getType() {
        return type;
    }

    /**
     * Obtiene el numero de cartas en la combinacion.
     *
     * @return el tamano
     */
    public int size() {
        return cards.size();
    }

    /**
     * Verifica si la combinacion es valida segun las reglas del chinchon.
     *
     * @return true si la combinacion es valida
     */
    public boolean isValid() {
        if (cards.size() < GameConstants.MIN_COMBINATION_SIZE) {
            return false;
        }
        return switch (type) {
            case GROUP -> isValidGroup();
            case RUN -> isValidRun();
        };
    }

    /**
     * Verifica si la carta indicada pertenece a esta combinacion.
     *
     * @param card la carta a buscar
     * @return true si la carta esta en la combinacion
     */
    public boolean containsCard(Card card) {
        return cards.contains(card);
    }

    @Override
    public String toString() {
        return type + ": " + cards;
    }

    /**
     * Valida un grupo: todas las cartas tienen el mismo valor y palos distintos.
     *
     * @return true si es un grupo valido
     */
    private boolean isValidGroup() {
        boolean sameValue = cards.stream()
                .allMatch(c -> c.getValue() == cards.getFirst().getValue());
        long distinctSuits = cards.stream()
                .map(Card::getSuit)
                .distinct()
                .count();
        return sameValue && distinctSuits == cards.size();
    }

    /**
     * Valida una escalera: todas las cartas son del mismo palo y consecutivas.
     *
     * @return true si es una escalera valida
     */
    private boolean isValidRun() {
        long distinctSuits = cards.stream()
                .map(Card::getSuit)
                .distinct()
                .count();
        if (distinctSuits != 1) {
            return false;
        }
        List<Card> sorted = cards.stream().sorted().toList();
        return isConsecutive(sorted);
    }

    /**
     * Verifica que las cartas estan en orden consecutivo.
     *
     * @param sorted las cartas ordenadas
     * @return true si son consecutivas
     */
    private boolean isConsecutive(List<Card> sorted) {
        return IntStream.range(1, sorted.size())
                .allMatch(i -> sorted.get(i).getOrderIndex()
                        - sorted.get(i - 1).getOrderIndex() == 1);
    }
}
