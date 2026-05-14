package app.chinchon.game;

import java.util.List;

/**
 * Configuracion de una partida de chinchon.
 * Almacena los parametros elegidos por el usuario.
 */
public class GameConfig {

    private final List<String> playerNames;
    private final int aiCount;
    private final int pointLimit;
    private final int deckCount;

    /**
     * Crea la configuracion de la partida.
     *
     * @param playerNames nombres de los jugadores humanos
     * @param aiCount     numero de jugadores IA
     * @param pointLimit  limite de puntos para eliminacion
     * @param deckCount   numero de barajas a usar
     */
    public GameConfig(List<String> playerNames, int aiCount,
                      int pointLimit, int deckCount) {
        this.playerNames = List.copyOf(playerNames);
        this.aiCount = aiCount;
        this.pointLimit = pointLimit;
        this.deckCount = deckCount;
    }

    /**
     * Obtiene los nombres de los jugadores humanos.
     *
     * @return lista inmutable de nombres
     */
    public List<String> getPlayerNames() {
        return playerNames;
    }

    /**
     * Obtiene el numero de jugadores IA.
     *
     * @return cantidad de jugadores IA
     */
    public int getAiCount() {
        return aiCount;
    }

    /**
     * Obtiene el limite de puntos para eliminacion.
     *
     * @return el limite de puntos
     */
    public int getPointLimit() {
        return pointLimit;
    }

    /**
     * Obtiene el numero de barajas a usar.
     *
     * @return cantidad de barajas
     */
    public int getDeckCount() {
        return deckCount;
    }

    /**
     * Obtiene el numero total de jugadores (humanos + IA).
     *
     * @return total de jugadores
     */
    public int getTotalPlayers() {
        return playerNames.size() + aiCount;
    }
}
