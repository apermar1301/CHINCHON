package app.chinchon.game;

import app.chinchon.model.Card;
import app.chinchon.model.Combination;
import app.chinchon.model.CombinationType;
import app.chinchon.model.Suit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests unitarios para la clase CombinationFinder.
 * Verifica la deteccion de combinaciones, chinchon, cierre y puntuacion.
 */
class CombinationFinderTest {

    private List<Card> cards;

    @BeforeEach
    void setUp() {
        cards = new ArrayList<>();
    }

    // --- findAllGroups ---

    /**
     * Enfoque de Caja Negra: Verifica el comportamiento esperado (salida) 
     * proporcionando una entrada de datos válidos comunes.
     * Verifica que se detecta un grupo de 3 cartas del mismo valor.
     */
    @Test
    void shouldFindGroupWhenThreeCardsHaveSameValue() {
        cards.add(new Card(Suit.OROS, 3));
        cards.add(new Card(Suit.COPAS, 3));
        cards.add(new Card(Suit.ESPADAS, 3));

        List<Combination> groups = CombinationFinder.findAllGroups(cards);

        assertFalse(groups.isEmpty());
        assertTrue(groups.stream().anyMatch(c ->
                c.size() == 3 && c.getType() == CombinationType.GROUP));
    }

    /**
     * Enfoque de Caja Blanca: Fuerza el recorrido por la rama condicional 
     * interna `if (indices.size() < MIN_COMBINATION_SIZE)` para comprobar el rechazo.
     * Verifica que no se detecta grupo con solo 2 cartas del mismo valor.
     */
    @Test
    void shouldNotFindGroupWhenOnlyTwoCardsHaveSameValue() {
        cards.add(new Card(Suit.OROS, 5));
        cards.add(new Card(Suit.COPAS, 5));
        cards.add(new Card(Suit.ESPADAS, 7));

        List<Combination> groups = CombinationFinder.findAllGroups(cards);

        assertTrue(groups.isEmpty());
    }

    /**
     * Verifica que se detecta un grupo de 4 cartas del mismo valor.
     */
    @Test
    void shouldFindGroupOfFourWhenFourCardsHaveSameValue() {
        cards.add(new Card(Suit.OROS, 7));
        cards.add(new Card(Suit.COPAS, 7));
        cards.add(new Card(Suit.ESPADAS, 7));
        cards.add(new Card(Suit.BASTOS, 7));

        List<Combination> groups = CombinationFinder.findAllGroups(cards);

        assertTrue(groups.stream().anyMatch(c -> c.size() == 4));
    }

    // --- findAllRuns ---

    /**
     * Enfoque de Caja Negra: Prueba funcional básica con datos válidos.
     * Verifica que se detecta una escalera de 3 cartas consecutivas del mismo palo.
     */
    @Test
    void shouldFindRunWhenThreeConsecutiveCardsSameSuit() {
        cards.add(new Card(Suit.OROS, 4));
        cards.add(new Card(Suit.OROS, 5));
        cards.add(new Card(Suit.OROS, 6));

        List<Combination> runs = CombinationFinder.findAllRuns(cards);

        assertFalse(runs.isEmpty());
        assertTrue(runs.stream().anyMatch(c ->
                c.size() == 3 && c.getType() == CombinationType.RUN));
    }

    /**
     * Enfoque de Caja Blanca: Prueba el bloque `else` interno de la función 
     * `findConsecutiveSequences` donde las cartas no cumplen `currOrder - prevOrder == 1`.
     * Verifica que no se detecta escalera si las cartas no son consecutivas.
     */
    @Test
    void shouldNotFindRunWhenCardsAreNotConsecutive() {
        cards.add(new Card(Suit.COPAS, 1));
        cards.add(new Card(Suit.COPAS, 3));
        cards.add(new Card(Suit.COPAS, 5));

        List<Combination> runs = CombinationFinder.findAllRuns(cards);

        assertTrue(runs.isEmpty());
    }

    /**
     * Verifica que 7 y 10 son consecutivos en la baraja espanola.
     */
    @Test
    void shouldFindRunWhenSevenAndTenAreConsecutive() {
        cards.add(new Card(Suit.ESPADAS, 6));
        cards.add(new Card(Suit.ESPADAS, 7));
        cards.add(new Card(Suit.ESPADAS, 10));

        List<Combination> runs = CombinationFinder.findAllRuns(cards);

        assertFalse(runs.isEmpty());
    }

    /**
     * Verifica que no se detecta escalera si las cartas son de palos distintos.
     */
    @Test
    void shouldNotFindRunWhenCardsAreDifferentSuits() {
        cards.add(new Card(Suit.OROS, 4));
        cards.add(new Card(Suit.COPAS, 5));
        cards.add(new Card(Suit.ESPADAS, 6));

        List<Combination> runs = CombinationFinder.findAllRuns(cards);

        assertTrue(runs.isEmpty());
    }

    // --- findBestCombinations ---

    /**
     * Enfoque de Caja Negra: Prueba de integración de la función principal.
     * Verifica que se encuentra la mejor combinacion en una mano mixta.
     */
    @Test
    void shouldFindBestCombinationsInMixedHand() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.OROS, 2));
        cards.add(new Card(Suit.OROS, 3));
        cards.add(new Card(Suit.COPAS, 5));
        cards.add(new Card(Suit.ESPADAS, 5));
        cards.add(new Card(Suit.BASTOS, 5));
        cards.add(new Card(Suit.OROS, 12));

        List<Combination> best =
                CombinationFinder.findBestCombinations(cards);
        int combined = CombinationFinder.getCombinedCardCount(best);

        assertEquals(6, combined);
    }

    /**
     * Enfoque de Caja Blanca: Verifica el caso base de la recursividad en 
     * `findOptimalSubset` donde el tamaño de combinaciones es 0.
     * Verifica que con cartas sin combinaciones no se forman combinaciones.
     */
    @Test
    void shouldReturnEmptyCombinationsWhenNoValidCombinations() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.COPAS, 3));
        cards.add(new Card(Suit.ESPADAS, 5));
        cards.add(new Card(Suit.BASTOS, 7));
        cards.add(new Card(Suit.OROS, 10));
        cards.add(new Card(Suit.COPAS, 11));
        cards.add(new Card(Suit.ESPADAS, 12));

        List<Combination> best =
                CombinationFinder.findBestCombinations(cards);

        assertTrue(best.isEmpty());
    }

    // --- getUncombinedCards ---

    /**
     * Enfoque de Caja Negra: Prueba de la salida esperada ante datos combinados mixtos.
     * Verifica que se identifican correctamente las cartas sueltas.
     */
    @Test
    void shouldReturnUncombinedCardsCorrectly() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.COPAS, 1));
        cards.add(new Card(Suit.ESPADAS, 1));
        cards.add(new Card(Suit.BASTOS, 12));

        List<Combination> combos =
                CombinationFinder.findBestCombinations(cards);
        List<Card> uncombined =
                CombinationFinder.getUncombinedCards(cards, combos);

        assertEquals(1, uncombined.size());
        assertEquals(12, uncombined.getFirst().getValue());
    }

    /**
     * Enfoque de Caja Blanca: Verifica la condición del Stream `filter` interno
     * donde ninguna carta está en `usedIndices`.
     * Verifica que todas las cartas son sueltas cuando no hay combinaciones.
     */
    @Test
    void shouldReturnAllCardsWhenNoCombinations() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.COPAS, 3));

        List<Card> uncombined =
                CombinationFinder.getUncombinedCards(cards, List.of());

        assertEquals(2, uncombined.size());
    }

    // --- calculateUncombinedPoints ---

    /**
     * Enfoque de Caja Negra: Prueba funcional de la suma de puntos.
     * Verifica el calculo de puntos de cartas no combinadas.
     */
    @Test
    void shouldCalculateUncombinedPointsCorrectly() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.COPAS, 1));
        cards.add(new Card(Suit.ESPADAS, 1));
        cards.add(new Card(Suit.BASTOS, 10));

        int points = CombinationFinder.calculateUncombinedPoints(cards);

        assertEquals(10, points);
    }

    /**
     * Enfoque de Caja Blanca: Prueba que el mapeo interno retorne un valor
     * numérico total de 0 al procesar una lista sin sobrantes.
     * Verifica que los puntos son 0 cuando todas las cartas estan combinadas.
     */
    @Test
    void shouldReturnZeroPointsWhenAllCardsCombined() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.COPAS, 1));
        cards.add(new Card(Suit.ESPADAS, 1));

        int points = CombinationFinder.calculateUncombinedPoints(cards);

        assertEquals(0, points);
    }

    // --- canClose ---

    /**
     * Enfoque de Caja Blanca: Fuerza la evaluación de la primera condición `if (combinedCount == GameConstants.HAND_SIZE)`.
     * Verifica que se puede cerrar con 7 cartas combinadas.
     */
    @Test
    void shouldAllowCloseWhenAllSevenCardsCombined() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.OROS, 2));
        cards.add(new Card(Suit.OROS, 3));
        cards.add(new Card(Suit.COPAS, 5));
        cards.add(new Card(Suit.ESPADAS, 5));
        cards.add(new Card(Suit.BASTOS, 5));
        cards.add(new Card(Suit.OROS, 5));

        assertTrue(CombinationFinder.canClose(cards));
    }

    /**
     * Enfoque de Caja Blanca: Fuerza la segunda condición de cierre donde 
     * `uncombined.size() == 1` y los puntos <= 5.
     * Verifica que se puede cerrar con 6 combinadas y 1 suelta de valor 1-5.
     */
    @Test
    void shouldAllowCloseWhenSixCombinedAndLooseCardValueUpToFive() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.OROS, 2));
        cards.add(new Card(Suit.OROS, 3));
        cards.add(new Card(Suit.COPAS, 5));
        cards.add(new Card(Suit.ESPADAS, 5));
        cards.add(new Card(Suit.BASTOS, 5));
        cards.add(new Card(Suit.COPAS, 3));

        assertTrue(CombinationFinder.canClose(cards));
    }

    /**
     * Enfoque de Caja Negra: Prueba de valores límite o condiciones no válidas de cierre.
     * Verifica que no se puede cerrar con 6 combinadas y carta suelta mayor a 5.
     */
    @Test
    void shouldNotAllowCloseWhenLooseCardValueExceedsFive() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.OROS, 2));
        cards.add(new Card(Suit.OROS, 3));
        cards.add(new Card(Suit.COPAS, 5));
        cards.add(new Card(Suit.ESPADAS, 5));
        cards.add(new Card(Suit.BASTOS, 5));
        cards.add(new Card(Suit.COPAS, 12));

        assertFalse(CombinationFinder.canClose(cards));
    }

    /**
     * Enfoque de Caja Blanca: Fuerza el `return false` final de `canClose`.
     * Verifica que no se puede cerrar con menos de 6 cartas combinadas.
     */
    @Test
    void shouldNotAllowCloseWhenLessThanSixCombined() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.OROS, 2));
        cards.add(new Card(Suit.OROS, 3));
        cards.add(new Card(Suit.COPAS, 7));
        cards.add(new Card(Suit.ESPADAS, 10));
        cards.add(new Card(Suit.BASTOS, 11));
        cards.add(new Card(Suit.COPAS, 12));

        assertFalse(CombinationFinder.canClose(cards));
    }

    // --- hasChinchon ---

    /**
     * Enfoque de Caja Negra: Prueba el escenario de éxito para Chinchón.
     * Verifica que se detecta un chinchon valido (7 consecutivas mismo palo).
     */
    @Test
    void shouldDetectChinchonWhenSevenConsecutiveCardsSameSuit() {
        cards.add(new Card(Suit.OROS, 4));
        cards.add(new Card(Suit.OROS, 5));
        cards.add(new Card(Suit.OROS, 6));
        cards.add(new Card(Suit.OROS, 7));
        cards.add(new Card(Suit.OROS, 10));
        cards.add(new Card(Suit.OROS, 11));
        cards.add(new Card(Suit.OROS, 12));

        assertTrue(CombinationFinder.hasChinchon(cards));
    }

    /**
     * Enfoque de Caja Blanca: Cubre la rama inicial rápida `if (cards.size() != GameConstants.HAND_SIZE)` que retorna falso.
     * Verifica que no es chinchon si no son 7 cartas.
     */
    @Test
    void shouldNotDetectChinchonWhenNotSevenCards() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.OROS, 2));
        cards.add(new Card(Suit.OROS, 3));

        assertFalse(CombinationFinder.hasChinchon(cards));
    }

    /**
     * Verifica que no es chinchon si las cartas son de distintos palos.
     */
    @Test
    void shouldNotDetectChinchonWhenDifferentSuits() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.OROS, 2));
        cards.add(new Card(Suit.OROS, 3));
        cards.add(new Card(Suit.OROS, 4));
        cards.add(new Card(Suit.OROS, 5));
        cards.add(new Card(Suit.OROS, 6));
        cards.add(new Card(Suit.COPAS, 7));

        assertFalse(CombinationFinder.hasChinchon(cards));
    }

    /**
     * Verifica que no es chinchon si las cartas no son consecutivas.
     */
    @Test
    void shouldNotDetectChinchonWhenNotConsecutive() {
        cards.add(new Card(Suit.OROS, 1));
        cards.add(new Card(Suit.OROS, 2));
        cards.add(new Card(Suit.OROS, 3));
        cards.add(new Card(Suit.OROS, 5));
        cards.add(new Card(Suit.OROS, 6));
        cards.add(new Card(Suit.OROS, 7));
        cards.add(new Card(Suit.OROS, 10));

        assertFalse(CombinationFinder.hasChinchon(cards));
    }

    // --- getCombinedCardCount ---

    /**
     * Verifica el conteo de cartas combinadas.
     */
    @Test
    void shouldCountCombinedCardsCorrectly() {
        List<Card> groupCards = List.of(
                new Card(Suit.OROS, 3),
                new Card(Suit.COPAS, 3),
                new Card(Suit.ESPADAS, 3)
        );
        Combination group = new Combination(groupCards, CombinationType.GROUP);

        List<Card> runCards = List.of(
                new Card(Suit.BASTOS, 5),
                new Card(Suit.BASTOS, 6),
                new Card(Suit.BASTOS, 7)
        );
        Combination run = new Combination(runCards, CombinationType.RUN);

        int count = CombinationFinder.getCombinedCardCount(
                List.of(group, run));

        assertEquals(6, count);
    }

    /**
     * Verifica que el conteo es 0 sin combinaciones.
     */
    @Test
    void shouldReturnZeroWhenNoCombinations() {
        int count = CombinationFinder.getCombinedCardCount(List.of());

        assertEquals(0, count);
    }

    // --- Caso borde: chinchon con secuencia 1-7 ---

    /**
     * Verifica chinchon con la secuencia mas baja posible (1-7).
     */
    @Test
    void shouldDetectChinchonWithLowestSequence() {
        cards.add(new Card(Suit.COPAS, 1));
        cards.add(new Card(Suit.COPAS, 2));
        cards.add(new Card(Suit.COPAS, 3));
        cards.add(new Card(Suit.COPAS, 4));
        cards.add(new Card(Suit.COPAS, 5));
        cards.add(new Card(Suit.COPAS, 6));
        cards.add(new Card(Suit.COPAS, 7));

        assertTrue(CombinationFinder.hasChinchon(cards));
    }

    // --- Caso borde: mano vacia ---

    /**
     * Enfoque de Caja Blanca: Caso límite para evitar NullPointerExceptions y fallos de aserción.
     * Verifica que una mano vacia no puede cerrar.
     */
    @Test
    void shouldNotAllowCloseWithEmptyHand() {
        assertFalse(CombinationFinder.canClose(cards));
    }

    /**
     * Verifica que una mano vacia no tiene chinchon.
     */
    @Test
    void shouldNotDetectChinchonWithEmptyHand() {
        assertFalse(CombinationFinder.hasChinchon(cards));
    }

    /**
     * Verifica que los puntos no combinados de mano vacia son 0.
     */
    @Test
    void shouldReturnZeroPointsForEmptyHand() {
        assertEquals(0, CombinationFinder.calculateUncombinedPoints(cards));
    }
}
