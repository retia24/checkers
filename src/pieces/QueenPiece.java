package pieces;

import board.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

/**
 * A Piece osztályból öröklődik. A Piece attribútumain felül tartalmazza a bábu képét.
 * Szerializálható, mivel a bábuk 2D tömbjét kell tudni elmenteni.
 */
public class QueenPiece extends Piece implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient Image image;

    /**
     * Alapértelmezett konstruktor, meghívja az ős konstruktorát és beállítja a bábu helyzetét.
     * Az x,y pozíciót a tábla mező méretéből számolja.
     *
     * @param board A tábla amelyen megtalálható a bábu.
     * @param r A bábu sora.
     * @param c A bábu oszlopa.
     * @param co A bábu színe.
     */
    public QueenPiece(Board board, int r, int c, PieceColor co) {
        super(board);
        col = c;
        row = r;
        color = co;
        xPos = col * board.getRowandcolsize();
        yPos = row * board.getRowandcolsize();
    }

    /**
     * Megpróbálja betölteni a bábuhoz tartalmazó képet.
     * Amilyen színű a bábu, azt a png képet tölti be.
     * Méretét a board mező mérete alapján skálázza.
     */
    public void loadImage() {
        try {
            if (color == PieceColor.BLACK) image = ImageIO.read(ClassLoader.getSystemResourceAsStream("blackqueen.png")).getScaledInstance(board.getRowandcolsize(), board.getRowandcolsize(), BufferedImage.SCALE_SMOOTH);
            else image = ImageIO.read(ClassLoader.getSystemResourceAsStream("whitequeen.png")).getScaledInstance(board.getRowandcolsize(), board.getRowandcolsize(), BufferedImage.SCALE_SMOOTH);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Kirajzolja a bábuhoz tartalmazó képet, a bábuhoz tartozó x és y pozícióba.
     * @param g Graphics2D paraméter.
     */
    @Override
    public void paint(Graphics2D g) {
        g.drawImage(image, xPos, yPos, null);
    }
}
