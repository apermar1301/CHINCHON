package app.chinchon.player;

import app.chinchon.ai.AIStrategy;
import app.chinchon.model.Card;
import app.chinchon.model.DrawSource;
import app.chinchon.util.ConsoleOutput;

/**
 * Jugador controlado por la inteligencia artificial.
 */
public class AIPlayer extends Player {

    private final AIStrategy strategy;

    /**
     * Crea un jugador IA con el nombre indicado.
     *
     * @param name el nombre del jugador
     */
    public AIPlayer(String name) {
        super(name);
        this.strategy = new AIStrategy();
    }

    @Override
    public DrawSource chooseDrawSource(Card topDiscard) {
        DrawSource source = strategy.shouldDrawFromDiscard(
                getHand(), topDiscard)
                ? DrawSource.DISCARD
                : DrawSource.DECK;
        ConsoleOutput.printMessage(getName() + " roba del "
                + (source == DrawSource.DECK ? "mazo" : "descarte") + ".");
        return source;
    }

    @Override
    public int chooseDiscard() {
        int index = strategy.chooseCardToDiscard(getHand());
        ConsoleOutput.printMessage(getName() + " descarta: "
                + getHand().getCard(index));
        return index;
    }

    @Override
    public boolean wantsToClose() {
        boolean close = strategy.shouldClose(getHand());
        if (close) {
            ConsoleOutput.printMessage(getName() + " decide cerrar la ronda.");
        }
        return close;
    }
}
