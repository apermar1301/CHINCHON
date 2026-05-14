package app.chinchon;

import app.chinchon.game.Game;
import app.chinchon.game.GameConfig;
import app.chinchon.game.GameSetup;
import app.util.ConsoleInput;
import app.chinchon.util.ConsoleOutput;

import java.util.Scanner;

/**
 * Punto de entrada del juego del chinchon.
 * Solo crea instancias y llama metodos.
 */
public class Main {

    /**
     * Metodo principal que inicia la aplicacion.
     *
     * @param args argumentos de linea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        ConsoleOutput.printMessage("Bienvenido al Chinchon!");
        ConsoleOutput.printSeparator();
        Scanner scanner = new Scanner(System.in);
        ConsoleInput consoleInput = new ConsoleInput(scanner);
        GameSetup setup = new GameSetup(consoleInput);
        GameConfig config = setup.configure();
        Game game = new Game(config, consoleInput);
        game.start();
        ConsoleOutput.printMessage("Gracias por jugar al Chinchon!");
        scanner.close();
    }
}
