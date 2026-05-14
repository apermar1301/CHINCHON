package app.chinchon.player;

import app.chinchon.model.Card;
import app.chinchon.model.DrawSource;
import app.chinchon.model.Hand;

/**
 * Clase base abstracta que representa un jugador del chinchon.
 */
public abstract class Player {

    private final String name;
    private final Hand hand;
    private int score;
    private boolean eliminated;
    private int turnCount;

    /**
     * Crea un jugador con el nombre indicado.
     *
     * @param name el nombre del jugador
     */
    protected Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.score = 0;
        this.eliminated = false;
        this.turnCount = 0;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return el nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la mano del jugador.
     *
     * @return la mano
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Obtiene la puntuacion acumulada del jugador.
     *
     * @return los puntos
     */
    public int getScore() {
        return score;
    }

    /**
     * Anade puntos a la puntuacion del jugador.
     *
     * @param points los puntos a sumar (puede ser negativo)
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Comprueba si el jugador ha sido eliminado.
     *
     * @return true si esta eliminado
     */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Marca al jugador como eliminado.
     */
    public void eliminate() {
        this.eliminated = true;
    }

    /**
     * Reinicia la mano del jugador para una nueva ronda.
     */
    public void resetHand() {
        hand.clear();
    }

    /**
     * Obtiene el numero de turnos jugados en la ronda actual.
     *
     * @return el contador de turnos
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * Incrementa el contador de turnos.
     */
    public void incrementTurnCount() {
        this.turnCount++;
    }

    /**
     * Reinicia el contador de turnos para una nueva ronda.
     */
    public void resetTurnCount() {
        this.turnCount = 0;
    }

    /**
     * Decide de donde robar una carta.
     *
     * @param topDiscard la carta visible en el descarte
     * @return el origen elegido
     */
    public abstract DrawSource chooseDrawSource(Card topDiscard);

    /**
     * Decide que carta descartar de la mano.
     *
     * @return el indice de la carta a descartar (0-based)
     */
    public abstract int chooseDiscard();

    /**
     * Decide si quiere cerrar la ronda.
     *
     * @return true si quiere cerrar
     */
    public abstract boolean wantsToClose();

    @Override
    public String toString() {
        return name;
    }
}
