package app.chinchon.util;

import app.chinchon.model.Card;
import app.chinchon.model.Combination;
import app.chinchon.model.Hand;
import app.chinchon.player.Player;

import java.util.List;

/**
 * Utilidad para mostrar informacion al usuario por consola.
 */
public final class ConsoleOutput {

    private static final String SEPARATOR =
            "============================================";
    private static final String THIN_SEPARATOR =
            "--------------------------------------------";

    private ConsoleOutput() {
        // Clase utilitaria, no instanciable
    }

    /**
     * Muestra un mensaje por consola.
     *
     * @param message el mensaje a mostrar
     */
    public static void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Muestra la mano de un jugador numerada.
     *
     * @param hand    la mano a mostrar
     * @param visible si las cartas deben mostrarse visibles
     */
    public static void printHand(Hand hand, boolean visible) {
        if (visible) {
            System.out.println(hand.toString());
        } else {
            System.out.println("[" + hand.size() + " cartas ocultas]");
        }
    }

    /**
     * Muestra una carta por consola.
     *
     * @param card la carta a mostrar
     */
    public static void printCard(Card card) {
        System.out.println(card.toString());
    }

    /**
     * Muestra las combinaciones formadas.
     *
     * @param combinations las combinaciones a mostrar
     */
    public static void printCombinations(List<Combination> combinations) {
        if (combinations.isEmpty()) {
            System.out.println("  Sin combinaciones.");
            return;
        }
        for (int i = 0; i < combinations.size(); i++) {
            Combination combo = combinations.get(i);
            System.out.println("  Combinacion " + (i + 1) + ": " + combo);
        }
    }

    /**
     * Muestra el marcador de todos los jugadores.
     *
     * @param players la lista de jugadores
     */
    public static void printScoreboard(List<Player> players) {
        System.out.println(THIN_SEPARATOR);
        System.out.println("MARCADOR:");
        for (Player player : players) {
            String status = player.isEliminated()
                    ? " [ELIMINADO]" : "";
            System.out.println("  " + player.getName() + ": "
                    + player.getScore() + " puntos" + status);
        }
        System.out.println(THIN_SEPARATOR);
    }

    /**
     * Muestra la cabecera de una ronda.
     *
     * @param roundNumber el numero de ronda
     */
    public static void printRoundHeader(int roundNumber) {
        System.out.println(SEPARATOR);
        System.out.println("         RONDA " + roundNumber);
        System.out.println(SEPARATOR);
    }

    /**
     * Muestra la cabecera del turno de un jugador.
     *
     * @param player el jugador actual
     */
    public static void printTurnHeader(Player player) {
        System.out.println(THIN_SEPARATOR);
        System.out.println("Turno de: " + player.getName());
        System.out.println(THIN_SEPARATOR);
    }

    /**
     * Muestra al ganador de la partida.
     *
     * @param winner el jugador ganador
     */
    public static void printWinner(Player winner) {
        System.out.println(SEPARATOR);
        System.out.println("  GANADOR: " + winner.getName());
        System.out.println(SEPARATOR);
    }

    /**
     * Muestra un separador visual.
     */
    public static void printSeparator() {
        System.out.println(SEPARATOR);
    }
}
