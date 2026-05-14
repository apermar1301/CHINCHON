package app.chinchon.game;

/**
 * Constantes del juego del chinchon.
 */
public final class GameConstants {

    /** Numero minimo de jugadores. */
    public static final int MIN_PLAYERS = 2;

    /** Numero maximo de jugadores. */
    public static final int MAX_PLAYERS = 5;

    /** Numero de cartas en la mano. */
    public static final int HAND_SIZE = 7;

    /** Tamano minimo de una combinacion valida. */
    public static final int MIN_COMBINATION_SIZE = 3;

    /** Minimo de cartas combinadas para poder cerrar. */
    public static final int MIN_CLOSE_COMBINED = 6;

    /** Valor maximo de la carta suelta al cerrar con 6 combinadas. */
    public static final int MAX_LOOSE_CARD_VALUE = 5;

    /** Bonificacion por cerrar con 7 cartas combinadas. */
    public static final int BONUS_ALL_COMBINED = -10;

    /** Limite de puntos por defecto. */
    public static final int DEFAULT_POINT_LIMIT = 100;

    /** Valores posibles de las cartas de la baraja espanola. */
    public static final int[] CARD_VALUES = {1, 2, 3, 4, 5, 6, 7, 10, 11, 12};

    /** Numero de palos en la baraja. */
    public static final int SUITS_COUNT = 4;

    /** Minimo de barajas configurables. */
    public static final int MIN_DECK_COUNT = 1;

    /** Maximo de barajas configurables. */
    public static final int MAX_DECK_COUNT = 2;

    /** Valor del primer indice de las figuras (sota). */
    public static final int FIRST_FIGURE_VALUE = 10;

    /** Umbral de orden para separar numericas de figuras. */
    public static final int ORDER_THRESHOLD = 7;

    /** Desplazamiento para calcular el indice de orden de figuras. */
    public static final int ORDER_OFFSET = 3;

    private GameConstants() {
        // Clase de constantes, no instanciable
    }
}
