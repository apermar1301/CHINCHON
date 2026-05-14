package app.chinchon.player;

import app.chinchon.model.Card;
import app.chinchon.model.DrawSource;
import app.util.ConsoleInput;
import app.chinchon.util.ConsoleOutput;

/**
 * Jugador humano que interactua mediante la consola.
 */
public class HumanPlayer extends Player {

    private final ConsoleInput consoleInput;

    /**
     * Crea un jugador humano con el nombre indicado.
     *
     * @param name         el nombre del jugador
     * @param consoleInput la utilidad de entrada por consola
     */
    public HumanPlayer(String name, ConsoleInput consoleInput) {
        super(name);
        this.consoleInput = consoleInput;
    }

    @Override
    public DrawSource chooseDrawSource(Card topDiscard) {
        ConsoleOutput.printMessage("Carta visible en el descarte: "
                + topDiscard);
        int option = consoleInput.readIntInRange(
                "Robar de: 1) Mazo  2) Descarte: ", 1, 2);
        return (option == 1) ? DrawSource.DECK : DrawSource.DISCARD;
    }

    @Override
    public int chooseDiscard() {
        ConsoleOutput.printMessage("Tu mano:");
        ConsoleOutput.printHand(getHand(), true);
        return consoleInput.readIntInRange(
                "Elige carta para descartar: ",
                1, getHand().size()) - 1;
    }

    @Override
    public boolean wantsToClose() {
        return consoleInput.readYesNo("Quieres cerrar la ronda?");
    }
}
