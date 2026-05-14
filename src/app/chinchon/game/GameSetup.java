package app.chinchon.game;

import app.util.ConsoleInput;
import app.chinchon.util.ConsoleOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Configura los parametros de una partida de chinchon mediante la consola.
 */
public class GameSetup {

    private final ConsoleInput consoleInput;

    /**
     * Crea el configurador con la utilidad de entrada.
     *
     * @param consoleInput la utilidad de entrada por consola
     */
    public GameSetup(ConsoleInput consoleInput) {
        this.consoleInput = consoleInput;
    }

    /**
     * Solicita al usuario los parametros de la partida.
     *
     * @return la configuracion creada
     */
    public GameConfig configure() {
        ConsoleOutput.printSeparator();
        ConsoleOutput.printMessage("  CONFIGURACION DE PARTIDA");
        ConsoleOutput.printSeparator();
        int totalPlayers = askNumberOfPlayers();
        int humanCount = askHumanCount(totalPlayers);
        List<String> playerNames = askPlayerNames(humanCount);
        int aiCount = totalPlayers - humanCount;
        int pointLimit = askPointLimit();
        int deckCount = askDeckCount();
        return new GameConfig(playerNames, aiCount,
                pointLimit, deckCount);
    }

    /**
     * Pregunta el numero total de jugadores.
     *
     * @return el numero de jugadores
     */
    private int askNumberOfPlayers() {
        return consoleInput.readIntInRange(
                "Numero de jugadores (" + GameConstants.MIN_PLAYERS
                        + "-" + GameConstants.MAX_PLAYERS + "): ",
                GameConstants.MIN_PLAYERS,
                GameConstants.MAX_PLAYERS);
    }

    /**
     * Pregunta cuantos jugadores son humanos.
     *
     * @param totalPlayers el total de jugadores
     * @return el numero de humanos
     */
    private int askHumanCount(int totalPlayers) {
        return consoleInput.readIntInRange(
                "Cuantos jugadores humanos (0-" + totalPlayers + "): ",
                0, totalPlayers);
    }

    /**
     * Solicita los nombres de los jugadores humanos.
     *
     * @param humanCount el numero de jugadores humanos
     * @return lista de nombres
     */
    private List<String> askPlayerNames(int humanCount) {
        List<String> names = new ArrayList<>();
        for (int i = 1; i <= humanCount; i++) {
            String name = consoleInput.readString(
                    "Nombre del jugador " + i + ": ");
            names.add(name);
        }
        return names;
    }

    /**
     * Pregunta el limite de puntos para eliminacion.
     *
     * @return el limite de puntos
     */
    private int askPointLimit() {
        return consoleInput.readInt(
                "Limite de puntos (ej: 100): ");
    }

    /**
     * Pregunta el numero de barajas a usar.
     *
     * @return el numero de barajas
     */
    private int askDeckCount() {
        return consoleInput.readIntInRange(
                "Numero de barajas (" + GameConstants.MIN_DECK_COUNT
                        + "-" + GameConstants.MAX_DECK_COUNT + "): ",
                GameConstants.MIN_DECK_COUNT,
                GameConstants.MAX_DECK_COUNT);
    }
}
