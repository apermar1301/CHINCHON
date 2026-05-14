package app.chinchon.model;

/**
 * Representa los palos de la baraja espanola.
 */
public enum Suit {

    OROS("Oros"),
    COPAS("Copas"),
    ESPADAS("Espadas"),
    BASTOS("Bastos");

    private final String displayName;

    Suit(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Obtiene el nombre del palo para mostrar al usuario.
     *
     * @return el nombre legible del palo
     */
    public String getDisplayName() {
        return displayName;
    }
}
