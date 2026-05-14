package app.chinchon.game;

import app.chinchon.model.Card;
import app.chinchon.model.Combination;
import app.chinchon.model.Deck;
import app.chinchon.model.DiscardPile;
import app.chinchon.model.DrawSource;
import app.chinchon.player.Player;
import app.chinchon.util.ConsoleOutput;

import java.util.List;

/**
 * Representa una ronda completa del chinchon.
 * Gestiona el reparto, los turnos y el cierre de la ronda.
 */
public class Round {

    private final List<Player> players;
    private final Deck deck;
    private final DiscardPile discardPile;
    private Player closingPlayer;
    private boolean roundOver;
    private boolean chinchonWin;

    /**
     * Crea una ronda con los jugadores y el numero de barajas indicados.
     *
     * @param players   los jugadores activos
     * @param deckCount el numero de barajas a usar
     */
    public Round(List<Player> players, int deckCount) {
        this.players = players;
        this.deck = new Deck(deckCount);
        this.discardPile = new DiscardPile();
        this.closingPlayer = null;
        this.roundOver = false;
        this.chinchonWin = false;
    }

    /**
     * Ejecuta la ronda completa: reparto, turnos y puntuacion.
     */
    public void play() {
        prepareDeck();
        dealCards();
        placeInitialDiscard();
        playTurns();
        calculateScores();
    }

    /**
     * Obtiene el jugador que cerro la ronda.
     *
     * @return el jugador que cerro, o null si nadie cerro
     */
    public Player getClosingPlayer() {
        return closingPlayer;
    }

    /**
     * Indica si la ronda termino por chinchon.
     *
     * @return true si hubo chinchon
     */
    public boolean isChinchonWin() {
        return chinchonWin;
    }

    /**
     * Baraja el mazo.
     */
    private void prepareDeck() {
        deck.shuffle();
    }

    /**
     * Reparte las cartas iniciales a cada jugador.
     */
    private void dealCards() {
        for (int i = 0; i < GameConstants.HAND_SIZE; i++) {
            for (Player player : players) {
                player.getHand().addCard(deck.draw());
            }
        }
    }

    /**
     * Coloca la primera carta del mazo en el descarte.
     */
    private void placeInitialDiscard() {
        discardPile.push(deck.draw());
    }

    /**
     * Ejecuta los turnos de todos los jugadores hasta que alguien cierre.
     */
    private void playTurns() {
        while (!roundOver) {
            for (int i = 0; i < players.size() && !roundOver; i++) {
                playTurn(players.get(i));
            }
        }
    }

    /**
     * Ejecuta el turno de un jugador.
     *
     * @param player el jugador que juega
     */
    private void playTurn(Player player) {
        if (roundOver) {
            return;
        }
        ConsoleOutput.printTurnHeader(player);
        refillDeckIfNeeded();
        handleDraw(player);
        player.incrementTurnCount();
        if (attemptClose(player)) {
            return;
        }
        handleDiscard(player);
    }

    /**
     * Gestiona la fase de robar carta del turno.
     *
     * @param player el jugador que roba
     */
    private void handleDraw(Player player) {
        DrawSource source = player.chooseDrawSource(discardPile.peek());
        Card drawn = switch (source) {
            case DECK -> deck.draw();
            case DISCARD -> discardPile.pop();
        };
        player.getHand().addCard(drawn);
        ConsoleOutput.printMessage("Carta robada: " + drawn);
    }

    /**
     * Intenta cerrar la ronda si el jugador quiere y puede.
     *
     * @param player el jugador que intenta cerrar
     * @return true si la ronda se cerro
     */
    private boolean attemptClose(Player player) {
        if (player.getTurnCount() < 1) {
            return false;
        }
        if (!CombinationFinder.canClose(player.getHand().getCards())) {
            return false;
        }
        if (!player.wantsToClose()) {
            return false;
        }
        handleClosing(player);
        return true;
    }

    /**
     * Procesa el cierre de la ronda por un jugador.
     *
     * @param player el jugador que cierra
     */
    private void handleClosing(Player player) {
        closingPlayer = player;
        roundOver = true;
        chinchonWin = CombinationFinder.hasChinchon(
                player.getHand().getCards());
        ConsoleOutput.printMessage(player.getName() + " cierra la ronda.");
        if (chinchonWin) {
            ConsoleOutput.printMessage("CHINCHON!");
        }
        handleClosingDiscard(player);
    }

    /**
     * Gestiona el descarte al cerrar (cerrar implica descartar).
     *
     * @param player el jugador que cierra
     */
    private void handleClosingDiscard(Player player) {
        List<Combination> combos = CombinationFinder.findBestCombinations(
                player.getHand().getCards());
        int combinedCount = CombinationFinder.getCombinedCardCount(combos);
        if (combinedCount < GameConstants.HAND_SIZE) {
            List<Card> uncombined = CombinationFinder.getUncombinedCards(
                    player.getHand().getCards(), combos);
            Card toDiscard = uncombined.getFirst();
            player.getHand().removeCard(toDiscard);
            discardPile.push(toDiscard);
        } else {
            Card toDiscard = player.getHand().removeCardAt(
                    player.getHand().size() - 1);
            discardPile.push(toDiscard);
        }
    }

    /**
     * Gestiona la fase de descartar carta del turno.
     *
     * @param player el jugador que descarta
     */
    private void handleDiscard(Player player) {
        int discardIndex = player.chooseDiscard();
        Card discarded = player.getHand().removeCardAt(discardIndex);
        discardPile.push(discarded);
        ConsoleOutput.printMessage("Carta descartada: " + discarded);
    }

    /**
     * Recarga el mazo con las cartas del descarte si se agota.
     */
    private void refillDeckIfNeeded() {
        if (deck.isEmpty()) {
            List<Card> recycled = discardPile.removeAllExceptTop();
            deck.addCards(recycled);
            deck.shuffle();
            ConsoleOutput.printMessage("El mazo se ha recargado "
                    + "con las cartas del descarte.");
        }
    }

    /**
     * Calcula y asigna las puntuaciones al final de la ronda.
     */
    private void calculateScores() {
        ConsoleOutput.printMessage("\nResultado de la ronda:");
        for (Player player : players) {
            calculatePlayerScore(player);
        }
    }

    /**
     * Calcula la puntuacion de un jugador individual.
     *
     * @param player el jugador a puntuar
     */
    private void calculatePlayerScore(Player player) {
        List<Card> cards = player.getHand().getCards();
        List<Combination> combos =
                CombinationFinder.findBestCombinations(cards);
        int points = calculatePoints(player, combos);
        player.addScore(points);
        printPlayerRoundResult(player, combos, points);
    }

    /**
     * Calcula los puntos de un jugador segun sus combinaciones.
     *
     * @param player el jugador
     * @param combos sus combinaciones
     * @return los puntos de la ronda
     */
    private int calculatePoints(Player player, List<Combination> combos) {
        if (player == closingPlayer) {
            return calculateClosingPlayerPoints(combos);
        }
        List<Card> uncombined = CombinationFinder.getUncombinedCards(
                player.getHand().getCards(), combos);
        return uncombined.stream().mapToInt(Card::getPoints).sum();
    }

    /**
     * Calcula los puntos del jugador que cierra.
     *
     * @param combos las combinaciones del jugador
     * @return los puntos (puede ser negativo si combino las 7)
     */
    private int calculateClosingPlayerPoints(List<Combination> combos) {
        int combinedCount = CombinationFinder.getCombinedCardCount(combos);
        if (combinedCount >= GameConstants.HAND_SIZE) {
            return GameConstants.BONUS_ALL_COMBINED;
        }
        return 0;
    }

    /**
     * Muestra el resultado de la ronda para un jugador.
     *
     * @param player el jugador
     * @param combos sus combinaciones
     * @param points los puntos de la ronda
     */
    private void printPlayerRoundResult(Player player,
                                        List<Combination> combos,
                                        int points) {
        ConsoleOutput.printMessage("  " + player.getName() + ":");
        ConsoleOutput.printMessage("    Mano: " + player.getHand().getCards());
        ConsoleOutput.printCombinations(combos);
        ConsoleOutput.printMessage("    Puntos esta ronda: " + points);
        ConsoleOutput.printMessage("    Total acumulado: "
                + player.getScore());
    }
}
