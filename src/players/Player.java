package players;

import pieces.PieceColor;

import java.io.Serializable;

/**
 * Player osztály a játékosok adatainak tárolásához.
 * Tartalmazza a játékos nevét, színét, azt, hogy éppen ő lép-e, illetve a lépésszámát is az aktuális játékban.
 * Szerializálható, mivel a playereket is el kell tudni menteni.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private int movecount;
    private PieceColor color;
    private boolean isCurrentPlayer;

    /**
     * Beállítja a játékos nevét, illetve színét.
     * Beállítja a szín alapján, hogy kezdésként ő jön-e.
     * @param n Név.
     * @param c Szín.
     */
    public Player(String n, PieceColor c) {
        name = n;
        movecount = 1;
        color = c;
        if (color == PieceColor.WHITE) {
            isCurrentPlayer = true;
        }
        else {
            isCurrentPlayer = false;
        }
    }

    public String toString() {
        return name;
    }

    public int getMovecount() {
        return movecount;
    }

    /**
     * Növeli a lépésszámot.
     */
    public void incrementMovecount() {
        movecount++;
    }

    public PieceColor getColor() {
        return color;
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
    }
}
