package app.chinchon.game;

import app.chinchon.model.Card;
import app.chinchon.model.Combination;
import app.chinchon.model.CombinationType;
import app.chinchon.model.Suit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Encuentra las mejores combinaciones posibles en una lista de cartas.
 * Utiliza un enfoque de fuerza bruta optimizado para manos de 7 cartas.
 */
public final class CombinationFinder {

    private CombinationFinder() {
        // Clase utilitaria, no instanciable
    }

    /**
     * Encuentra el conjunto optimo de combinaciones no solapadas
     * que maximiza el numero de cartas combinadas.
     *
     * @param cards las cartas a analizar
     * @return la lista de mejores combinaciones
     */
    public static List<Combination> findBestCombinations(List<Card> cards) {
        List<IndexedCombination> candidates = getAllCandidates(cards);
        List<IndexedCombination> best = findOptimalSubset(candidates);
        return toCombinations(best, cards);
    }

    /**
     * Encuentra todos los grupos validos posibles en las cartas.
     *
     * @param cards las cartas a analizar
     * @return lista de grupos validos
     */
    public static List<Combination> findAllGroups(List<Card> cards) {
        return toCombinations(findGroupCandidates(cards), cards);
    }

    /**
     * Encuentra todas las escaleras validas posibles en las cartas.
     *
     * @param cards las cartas a analizar
     * @return lista de escaleras validas
     */
    public static List<Combination> findAllRuns(List<Card> cards) {
        return toCombinations(findRunCandidates(cards), cards);
    }

    /**
     * Obtiene las cartas que no forman parte de ninguna combinacion.
     *
     * @param cards        todas las cartas
     * @param combinations las combinaciones formadas
     * @return las cartas sueltas
     */
    public static List<Card> getUncombinedCards(List<Card> cards,
                                                List<Combination> combinations) {
        Set<Integer> usedIndices = getUsedIndices(cards, combinations);
        return IntStream.range(0, cards.size())
                .filter(i -> !usedIndices.contains(i))
                .mapToObj(cards::get)
                .toList();
    }

    /**
     * Calcula los puntos de las cartas no combinadas.
     *
     * @param cards las cartas de la mano
     * @return los puntos de las cartas sueltas
     */
    public static int calculateUncombinedPoints(List<Card> cards) {
        List<Combination> best = findBestCombinations(cards);
        List<Card> uncombined = getUncombinedCards(cards, best);
        return uncombined.stream()
                .mapToInt(Card::getPoints)
                .sum();
    }

    /**
     * Determina si el jugador puede cerrar con las cartas indicadas.
     * Requiere 6+ cartas combinadas. Si son 6, la suelta debe valer 1-5.
     *
     * @param cards las cartas de la mano
     * @return true si puede cerrar
     */
    public static boolean canClose(List<Card> cards) {
        List<Combination> best = findBestCombinations(cards);
        int combinedCount = getCombinedCardCount(best);
        if (combinedCount == GameConstants.HAND_SIZE) {
            return true;
        }
        if (combinedCount == GameConstants.MIN_CLOSE_COMBINED) {
            List<Card> uncombined = getUncombinedCards(cards, best);
            return uncombined.size() == 1
                    && uncombined.getFirst().getPoints()
                        <= GameConstants.MAX_LOOSE_CARD_VALUE;
        }
        return false;
    }

    /**
     * Determina si las cartas forman un chinchon
     * (7 cartas consecutivas del mismo palo).
     *
     * @param cards las cartas de la mano
     * @return true si es chinchon
     */
    public static boolean hasChinchon(List<Card> cards) {
        if (cards.size() != GameConstants.HAND_SIZE) {
            return false;
        }
        List<Combination> best = findBestCombinations(cards);
        return best.size() == 1
                && best.getFirst().size() == GameConstants.HAND_SIZE
                && best.getFirst().getType() == CombinationType.RUN;
    }

    /**
     * Obtiene el numero total de cartas combinadas.
     *
     * @param combinations las combinaciones
     * @return el total de cartas combinadas
     */
    public static int getCombinedCardCount(List<Combination> combinations) {
        return combinations.stream()
                .mapToInt(Combination::size)
                .sum();
    }

    // --- Metodos privados ---

    /**
     * Obtiene todos los candidatos a combinacion (grupos y escaleras).
     */
    private static List<IndexedCombination> getAllCandidates(List<Card> cards) {
        List<IndexedCombination> candidates = new ArrayList<>();
        candidates.addAll(findGroupCandidates(cards));
        candidates.addAll(findRunCandidates(cards));
        return candidates;
    }

    /**
     * Encuentra candidatos de tipo grupo indexados por posicion.
     */
    private static List<IndexedCombination> findGroupCandidates(
            List<Card> cards) {
        Map<Integer, List<Integer>> byValue = groupIndicesByValue(cards);
        List<IndexedCombination> result = new ArrayList<>();
        for (var entry : byValue.entrySet()) {
            List<Integer> indices = filterDistinctSuits(cards, entry.getValue());
            addGroupSubsets(indices, result);
        }
        return result;
    }

    /**
     * Agrupa los indices de las cartas por su valor.
     */
    private static Map<Integer, List<Integer>> groupIndicesByValue(
            List<Card> cards) {
        Map<Integer, List<Integer>> byValue = new HashMap<>();
        for (int i = 0; i < cards.size(); i++) {
            byValue.computeIfAbsent(cards.get(i).getValue(),
                    k -> new ArrayList<>()).add(i);
        }
        return byValue;
    }

    /**
     * Filtra indices para quedarse con uno por palo (el primero encontrado).
     */
    private static List<Integer> filterDistinctSuits(List<Card> cards,
                                                     List<Integer> indices) {
        Set<Suit> seenSuits = new HashSet<>();
        List<Integer> filtered = new ArrayList<>();
        for (int idx : indices) {
            if (seenSuits.add(cards.get(idx).getSuit())) {
                filtered.add(idx);
            }
        }
        return filtered;
    }

    /**
     * Genera subconjuntos de tamano 3+ como grupos.
     */
    private static void addGroupSubsets(List<Integer> indices,
                                        List<IndexedCombination> result) {
        if (indices.size() < GameConstants.MIN_COMBINATION_SIZE) {
            return;
        }
        for (int size = GameConstants.MIN_COMBINATION_SIZE;
             size <= indices.size(); size++) {
            generateSubsetsOfSize(indices, size, 0,
                    new ArrayList<>(), CombinationType.GROUP, result);
        }
    }

    /**
     * Encuentra candidatos de tipo escalera indexados por posicion.
     */
    private static List<IndexedCombination> findRunCandidates(
            List<Card> cards) {
        List<IndexedCombination> result = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            List<Integer> suitIndices = getSuitIndicesSorted(cards, suit);
            addRunsFromSorted(suitIndices, cards, result);
        }
        return result;
    }

    /**
     * Obtiene los indices de cartas de un palo ordenados por valor.
     */
    private static List<Integer> getSuitIndicesSorted(List<Card> cards,
                                                      Suit suit) {
        return IntStream.range(0, cards.size())
                .filter(i -> cards.get(i).getSuit() == suit)
                .boxed()
                .sorted((a, b) -> Integer.compare(
                        cards.get(a).getOrderIndex(),
                        cards.get(b).getOrderIndex()))
                .toList();
    }

    /**
     * Genera todas las escaleras posibles a partir de indices ordenados.
     */
    private static void addRunsFromSorted(List<Integer> sortedIndices,
                                          List<Card> cards,
                                          List<IndexedCombination> result) {
        List<List<Integer>> sequences = findConsecutiveSequences(
                sortedIndices, cards);
        for (List<Integer> seq : sequences) {
            addRunSubsets(seq, result);
        }
    }

    /**
     * Encuentra secuencias de indices consecutivos dentro del palo.
     */
    private static List<List<Integer>> findConsecutiveSequences(
            List<Integer> sortedIndices, List<Card> cards) {
        List<List<Integer>> sequences = new ArrayList<>();
        if (sortedIndices.isEmpty()) {
            return sequences;
        }
        List<Integer> current = new ArrayList<>();
        current.add(sortedIndices.getFirst());
        for (int i = 1; i < sortedIndices.size(); i++) {
            int prevOrder = cards.get(
                    current.getLast()).getOrderIndex();
            int currOrder = cards.get(
                    sortedIndices.get(i)).getOrderIndex();
            if (currOrder - prevOrder == 1) {
                current.add(sortedIndices.get(i));
            } else {
                addSequenceIfValid(current, sequences);
                current = new ArrayList<>();
                current.add(sortedIndices.get(i));
            }
        }
        addSequenceIfValid(current, sequences);
        return sequences;
    }

    /**
     * Anade la secuencia a la lista si tiene tamano suficiente.
     */
    private static void addSequenceIfValid(List<Integer> sequence,
                                           List<List<Integer>> sequences) {
        if (sequence.size() >= GameConstants.MIN_COMBINATION_SIZE) {
            sequences.add(new ArrayList<>(sequence));
        }
    }

    /**
     * Genera sub-escaleras de tamano 3+ a partir de una secuencia consecutiva.
     */
    private static void addRunSubsets(List<Integer> sequence,
                                      List<IndexedCombination> result) {
        for (int start = 0; start < sequence.size(); start++) {
            for (int end = start + GameConstants.MIN_COMBINATION_SIZE;
                 end <= sequence.size(); end++) {
                Set<Integer> indices = new HashSet<>(
                        sequence.subList(start, end));
                result.add(new IndexedCombination(
                        indices, CombinationType.RUN));
            }
        }
    }

    /**
     * Genera subconjuntos de un tamano especifico recursivamente.
     */
    private static void generateSubsetsOfSize(
            List<Integer> indices, int targetSize, int startIdx,
            List<Integer> current, CombinationType type,
            List<IndexedCombination> result) {
        if (current.size() == targetSize) {
            result.add(new IndexedCombination(
                    new HashSet<>(current), type));
            return;
        }
        int remaining = targetSize - current.size();
        for (int i = startIdx;
             i <= indices.size() - remaining; i++) {
            current.add(indices.get(i));
            generateSubsetsOfSize(indices, targetSize,
                    i + 1, current, type, result);
            current.removeLast();
        }
    }

    /**
     * Encuentra el subconjunto optimo de combinaciones no solapadas
     * que maximiza el numero de cartas combinadas.
     */
    private static List<IndexedCombination> findOptimalSubset(
            List<IndexedCombination> candidates) {
        List<IndexedCombination> bestResult = new ArrayList<>();
        findOptimalRecursive(candidates, 0,
                new ArrayList<>(), new HashSet<>(), bestResult);
        return bestResult;
    }

    /**
     * Busqueda recursiva del mejor subconjunto no solapado.
     */
    private static void findOptimalRecursive(
            List<IndexedCombination> candidates, int index,
            List<IndexedCombination> current, Set<Integer> usedIndices,
            List<IndexedCombination> bestResult) {
        if (index == candidates.size()) {
            updateBestIfBetter(current, bestResult);
            return;
        }
        // Opcion 1: omitir esta combinacion
        findOptimalRecursive(candidates, index + 1,
                current, usedIndices, bestResult);
        // Opcion 2: incluir si no solapa
        IndexedCombination candidate = candidates.get(index);
        if (!overlaps(candidate, usedIndices)) {
            includeAndRecurse(candidates, index, current,
                    usedIndices, bestResult, candidate);
        }
    }

    /**
     * Incluye una combinacion y continua la busqueda recursiva.
     */
    private static void includeAndRecurse(
            List<IndexedCombination> candidates, int index,
            List<IndexedCombination> current, Set<Integer> usedIndices,
            List<IndexedCombination> bestResult,
            IndexedCombination candidate) {
        current.add(candidate);
        usedIndices.addAll(candidate.indices());
        findOptimalRecursive(candidates, index + 1,
                current, usedIndices, bestResult);
        current.removeLast();
        usedIndices.removeAll(candidate.indices());
    }

    /**
     * Actualiza el mejor resultado si el actual tiene mas cartas combinadas.
     */
    private static void updateBestIfBetter(
            List<IndexedCombination> current,
            List<IndexedCombination> bestResult) {
        int currentCount = current.stream()
                .mapToInt(ic -> ic.indices().size()).sum();
        int bestCount = bestResult.stream()
                .mapToInt(ic -> ic.indices().size()).sum();
        if (currentCount > bestCount) {
            bestResult.clear();
            bestResult.addAll(current);
        }
    }

    /**
     * Verifica si la combinacion solapa con los indices ya usados.
     */
    private static boolean overlaps(IndexedCombination combo,
                                    Set<Integer> usedIndices) {
        return combo.indices().stream().anyMatch(usedIndices::contains);
    }

    /**
     * Obtiene los indices usados por las combinaciones.
     */
    private static Set<Integer> getUsedIndices(
            List<Card> cards, List<Combination> combinations) {
        Set<Integer> used = new HashSet<>();
        for (Combination combo : combinations) {
            markUsedIndices(cards, combo, used);
        }
        return used;
    }

    /**
     * Marca los indices de las cartas usadas por una combinacion.
     */
    private static void markUsedIndices(List<Card> cards,
                                        Combination combo,
                                        Set<Integer> used) {
        for (Card comboCard : combo.getCards()) {
            markSingleCardIndex(cards, comboCard, used);
        }
    }

    /**
     * Marca el indice de una carta individual en el conjunto de usados.
     *
     * @param cards     la lista de cartas
     * @param target    la carta a buscar
     * @param used      el conjunto de indices usados
     */
    private static void markSingleCardIndex(List<Card> cards,
                                            Card target,
                                            Set<Integer> used) {
        boolean found = false;
        for (int i = 0; i < cards.size() && !found; i++) {
            if (cards.get(i).equals(target) && !used.contains(i)) {
                used.add(i);
                found = true;
            }
        }
    }

    /**
     * Convierte combinaciones indexadas a combinaciones con cartas.
     */
    private static List<Combination> toCombinations(
            List<IndexedCombination> indexed, List<Card> cards) {
        return indexed.stream()
                .map(ic -> ic.toCombination(cards))
                .toList();
    }

    /**
     * Registro interno que asocia indices de cartas con tipo de combinacion.
     *
     * @param indices los indices de las cartas en la mano
     * @param type    el tipo de combinacion
     */
    private record IndexedCombination(Set<Integer> indices,
                                      CombinationType type) {

        /**
         * Convierte a una combinacion con las cartas reales.
         *
         * @param cards la lista de cartas de referencia
         * @return la combinacion creada
         */
        Combination toCombination(List<Card> cards) {
            List<Card> combinationCards = indices.stream()
                    .sorted()
                    .map(cards::get)
                    .toList();
            return new Combination(combinationCards, type);
        }
    }
}
