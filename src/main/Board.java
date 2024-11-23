package main;

import pieces.NormalPiece;
import pieces.Piece;
import pieces.PieceColor;
import pieces.QueenPiece;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int rows = 8;
    private final int cols = 8;
    private final int rowandcolsize = 75;

    public Piece[][] pieces;
    private Piece selectedPiece = null;

    private final GameLogic gameLogic;
    private List<Move> listOfMoves;
    private List<Capture> listOfCaptures;
    private boolean captureLock = false;

    private PieceColor currentPlayer;
    private outputArea outputArea;

    LeaderBoard leaderBoard;
    private boolean gameWon;

    private BoardRenderer boardRenderer;

    public Board() {
        pieces = new Piece[rows][cols];
        listOfMoves = new ArrayList<>();
        listOfCaptures = new ArrayList<>();
        currentPlayer = PieceColor.WHITE;
        gameLogic = new GameLogic();
        CursorInput input = new CursorInput(this, gameLogic);
        boardRenderer = new BoardRenderer(this);

        gameWon = false;

        setPreferredSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setMinimumSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setMaximumSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setBackground(Color.BLACK);

        addMouseListener(input);
        addMouseMotionListener(input);

        initBoard();
    }

    public void initBoard() {
        setupPieces(PieceColor.BLACK, 0, 3);
        setupPieces(PieceColor.WHITE, rows - 3, rows);
    }

    private void setupPieces(PieceColor color, int startRow, int endRow) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < cols; j++) {
                if (colorOfField(i, j) == FieldColor.DARK) {
                    pieces[i][j] = new NormalPiece(this, i, j, color);
                }
            }
        }
    }

    public void loadPieceImages() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (pieces[r][c] != null) {
                    pieces[r][c].loadImage();
                }
            }
        }
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece piece) {
        selectedPiece = piece;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getHeightofBoard() {
        return cols * rowandcolsize;
    }

    public int getRowandcolsize() {
        return rowandcolsize;
    }

    public List<Capture> getCaptures() {
        return listOfCaptures;
    }

    public boolean getCaptureLock() {
        return captureLock;
    }

    public void setCaptureLock(boolean lock) {
        captureLock = lock;
    }

    public void setOutputArea(outputArea oa) {
        outputArea = oa;
        if (outputArea.getPlayers()[0].isCurrentPlayer()) {
            currentPlayer = PieceColor.WHITE;
        }
        else {
            currentPlayer = PieceColor.BLACK;
        }
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[][] p) {
        pieces = p;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setLeaderBoard(LeaderBoard lb) {
        leaderBoard = lb;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public PieceColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void resetSelectedPiecePos() {
        getSelectedPiece().setxPos(getSelectedPiece().getCol() * getRowandcolsize());
        getSelectedPiece().setyPos(getSelectedPiece().getRow() * getRowandcolsize());
    }

    private void removeCapturedPiece(int row, int col, List<Capture> captureList) {
        for (Capture m : captureList) {
            if (m.getRow() == row && m.getCol() == col) {
                pieces[m.getCapturedPiece().getRow()][m.getCapturedPiece().getCol()] = null;
            }
        }
    }

    public void makeMove(Board board, Piece p, int row, int col) {
        pieces[p.getRow()][p.getCol()] = null;
        if (p instanceof NormalPiece && (p.getColor() == PieceColor.WHITE && row == 0) || (p.getColor() == PieceColor.BLACK && row == 7)) {
            p = new QueenPiece(board, row, col, p.getColor());
            p.loadImage();
        }
        p.setRow(row);
        p.setCol(col);
        p.setxPos(col * rowandcolsize);
        p.setyPos(row * rowandcolsize);
        pieces[row][col] = p;
        if (!gameLogic.checkGameOver(pieces, outputArea))
            changePlayer();
        else
            gameWon = true;
    }

    public void makeCapture(Board board, Piece p, int row, int col) {
        pieces[p.getRow()][p.getCol()] = null;
        listOfCaptures = gameLogic.updateListOfCaptures(pieces, selectedPiece, currentPlayer);
        removeCapturedPiece(row, col, listOfCaptures);
        if (p instanceof NormalPiece && (p.getColor() == PieceColor.WHITE && row == 0) || (p.getColor() == PieceColor.BLACK && row == 7)) {
            p = new QueenPiece(board, row, col, p.getColor());
            p.loadImage();
        }
        p.setRow(row);
        p.setCol(col);
        p.setxPos(col * rowandcolsize);
        p.setyPos(row * rowandcolsize);
        pieces[row][col] = p;
        if (listOfCaptures.isEmpty()) {
            if (!gameLogic.checkGameOver(pieces, outputArea))
                changePlayer();
            else
                gameWon = true;
        }
        else {
            listOfMoves.clear();
            captureLock = true;
        }
    }

    public void showPlayer() {
        if (currentPlayer == PieceColor.WHITE) {
            outputArea.whitesTurn();
        }
        else {
            outputArea.blacksTurn();
        }
    }

    public void changePlayer() {
        if (currentPlayer == PieceColor.BLACK) {
            currentPlayer = PieceColor.WHITE;
            outputArea.getPlayers()[1].incrementMovecount();
            outputArea.whitesTurn();
        }
        else {
            currentPlayer = PieceColor.BLACK;
            outputArea.getPlayers()[0].incrementMovecount();
            outputArea.blacksTurn();
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        boardRenderer.render(g2);
    }

    public FieldColor colorOfField(int row, int col) {
        return (row + col) % 2 == 0 ? FieldColor.LIGHT : FieldColor.DARK;
    }
}
