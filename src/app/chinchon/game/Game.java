package app.chinchon.game;

import app.chinchon.player.AIPlayer;
import app.chinchon.player.HumanPlayer;
import app.chinchon.player.Player;
import app.util.ConsoleInput;
import app.chinchon.util.ConsoleOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que gestiona una partida completa de chinchon.
 * Controla las rondas, eliminaciones y determina al ganador.
 */
public class Game {

    private final List<Player> players;
    private final int pointLimit;
    private final int deckCount;
    private int roundNumber;

    /**
     * Crea una partida con la configuracion e input indicados.
     *
     * @param config       la configuracion de la partida
     * @param consoleInput la utilidad de entrada por consola
     */
    public Game(GameConfig config, ConsoleInput consoleInput) {
        this.players = createPlayers(config, consoleInput);
        this.pointLimit = config.getPointLimit();
        this.deckCount = config.getDeckCount();
        this.roundNumber = 0;
    }

    /**
     * Inicia y ejecuta la partida completa hasta que haya un ganador.
     */
    public void start() {
        ConsoleOutput.printMessage("\nComienza la partida de Chinchon!");
        ConsoleOutput.printMessage("Limite de puntos: " + pointLimit);
        ConsoleOutput.printMessage("Jugadores: " + players.size());
        ConsoleOutput.printSeparator();
        while (!isGameOver()) {
            playRound();
        }
        announceWinner();
    }

    /**
     * Ejecuta una ronda completa de la partida.
     */
    private void playRound() {
        roundNumber++;
        ConsoleOutput.printRoundHeader(roundNumber);
        List<Player> activePlayers = getActivePlayers();
        resetPlayersForRound(activePlayers);
        Round round = new Round(activePlayers, deckCount);
        round.play();
        if (round.isChinchonWin()) {
            handleChinchonVictory(round.getClosingPlayer());
            return;
        }
        eliminatePlayers();
        ConsoleOutput.printScoreboard(players);
    }

    /**
     * Gestiona la victoria por chinchon.
     *
     * @param winner el jugador que saco chinchon
     */
    private void handleChinchonVictory(Player winner) {
        for (Player player : players) {
            if (player != winner) {
                player.eliminate();
            }
        }
        ConsoleOutput.printMessage(winner.getName()
                + " gana la partida con CHINCHON!");
    }

    /**
     * Elimina a los jugadores que alcanzan o superan el limite de puntos.
     */
    private void eliminatePlayers() {
        for (Player player : players) {
            if (!player.isEliminated()
                    && player.getScore() >= pointLimit) {
                player.eliminate();
                ConsoleOutput.printMessage(player.getName()
                        + " ha sido eliminado con "
                        + player.getScore() + " puntos.");
            }
        }
    }

    /**
     * Comprueba si la partida ha terminado.
     *
     * @return true si queda un jugador o menos
     */
    private boolean isGameOver() {
        return getActivePlayers().size() <= 1;
    }

    /**
     * Obtiene la lista de jugadores no eliminados.
     *
     * @return jugadores activos
     */
    private List<Player> getActivePlayers() {
        return players.stream()
                .filter(p -> !p.isEliminated())
                .toList();
    }

    /**
     * Anuncia al ganador de la partida.
     */
    private void announceWinner() {
        List<Player> active = getActivePlayers();
        if (active.isEmpty()) {
            ConsoleOutput.printMessage("No queda ningun jugador.");
            return;
        }
        ConsoleOutput.printWinner(active.getFirst());
    }

    /**
     * Reinicia las manos y contadores de turno para una nueva ronda.
     *
     * @param activePlayers los jugadores activos
     */
    private void resetPlayersForRound(List<Player> activePlayers) {
        for (Player player : activePlayers) {
            player.resetHand();
            player.resetTurnCount();
        }
    }

    /**
     * Crea los jugadores a partir de la configuracion.
     *
     * @param config       la configuracion de la partida
     * @param consoleInput la utilidad de entrada por consola
     * @return la lista de jugadores
     */
    private List<Player> createPlayers(GameConfig config,
                                       ConsoleInput consoleInput) {
        List<Player> allPlayers = new ArrayList<>();
        for (String name : config.getPlayerNames()) {
            allPlayers.add(new HumanPlayer(name, consoleInput));
        }
        for (int i = 1; i <= config.getAiCount(); i++) {
            allPlayers.add(new AIPlayer("Maquina " + i));
        }
        return allPlayers;
    }
}
