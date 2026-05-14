package app.chinchon.ai;

import app.chinchon.game.CombinationFinder;
import app.chinchon.model.Card;
import app.chinchon.model.Combination;
import app.chinchon.model.Hand;

import java.util.ArrayList;
import java.util.List;

/**
 * Estrategia heuristica de la inteligencia artificial para el chinchon.
 * Prioriza completar combinaciones y descarta cartas de alto valor sin uso.
 */
public class AIStrategy {

    /**
     * Decide si la IA debe robar del monton de descarte.
     * Roba del descarte si la carta completa una combinacion existente.
     *
     * @param hand       la mano actual del jugador
     * @param topDiscard la carta superior del descarte
     * @return true si debe robar del descarte
     */
    public boolean shouldDrawFromDiscard(Hand hand, Card topDiscard) {
        List<Card> currentCards = hand.getCards();
        int currentCombined = getCombinedCount(currentCards);
        List<Card> withDiscard = createHandWith(currentCards, topDiscard);
        int newCombined = getCombinedCount(withDiscard);
        return newCombined > currentCombined;
    }

    /**
     * Elige la carta a descartar de la mano.
     * Descarta la carta de mayor valor que no participe en combinaciones.
     *
     * @param hand la mano del jugador
     * @return el indice de la carta a descartar (0-based)
     */
    public int chooseCardToDiscard(Hand hand) {
        List<Card> cards = hand.getCards();
        List<Combination> combinations =
                CombinationFinder.findBestCombinations(cards);
        List<Card> uncombined =
                CombinationFinder.getUncombinedCards(cards, combinations);
        if (uncombined.isEmpty()) {
            return findHighestValueIndex(hand);
        }
        return findHighestUncombinedIndex(hand, uncombined);
    }

    /**
     * Decide si la IA debe cerrar la ronda.
     *
     * @param hand la mano del jugador
     * @return true si debe cerrar
     */
    public boolean shouldClose(Hand hand) {
        return CombinationFinder.canClose(hand.getCards());
    }

    /**
     * Obtiene el numero de cartas combinadas en una lista.
     *
     * @param cards las cartas a evaluar
     * @return el total de cartas combinadas
     */
    private int getCombinedCount(List<Card> cards) {
        List<Combination> combos =
                CombinationFinder.findBestCombinations(cards);
        return CombinationFinder.getCombinedCardCount(combos);
    }

    /**
     * Crea una lista simulada con las cartas actuales mas la carta indicada.
     *
     * @param currentCards las cartas de la mano
     * @param extra        la carta adicional
     * @return nueva lista con la carta extra
     */
    private List<Card> createHandWith(List<Card> currentCards, Card extra) {
        List<Card> withExtra = new ArrayList<>(currentCards);
        withExtra.add(extra);
        return withExtra;
    }

    /**
     * Encuentra el indice de la carta con mayor valor en la mano.
     *
     * @param hand la mano del jugador
     * @return el indice de la carta mas valiosa
     */
    private int findHighestValueIndex(Hand hand) {
        int maxIndex = 0;
        for (int i = 1; i < hand.size(); i++) {
            if (hand.getCard(i).getPoints()
                    > hand.getCard(maxIndex).getPoints()) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * Encuentra el indice de la carta no combinada con mayor valor.
     *
     * @param hand       la mano del jugador
     * @param uncombined las cartas no combinadas
     * @return el indice en la mano de la carta suelta mas valiosa
     */
    private int findHighestUncombinedIndex(Hand hand,
                                           List<Card> uncombined) {
        Card highest = uncombined.stream()
                .max((a, b) -> Integer.compare(
                        a.getPoints(), b.getPoints()))
                .orElse(uncombined.getFirst());
        return findCardIndex(hand, highest);
    }

    /**
     * Encuentra el indice de una carta en la mano.
     *
     * @param hand la mano
     * @param card la carta a buscar
     * @return el indice de la carta
     */
    private int findCardIndex(Hand hand, Card card) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.getCard(i).equals(card)) {
                return i;
            }
        }
        return 0;
    }
}
