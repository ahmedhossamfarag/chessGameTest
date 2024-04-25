package org.example.models;

public class Board {
    public Cell[][] cells;

    public void build() {
        cells = new Cell[8][8];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Cell(new Point(i, j));
            }
        }
        Class<?>[] pieces = {Rook.class, Knight.class, Bishop.class, Queen.class, King.class, Bishop.class, Knight.class, Rook.class};
        try {
            for (int i = 0; i < pieces.length; i++) {
                cells[0][i].piece = (Piece) pieces[i].getConstructor(Cell.class, Board.class, Boolean.TYPE).newInstance(cells[0][i], this, false);
                cells[7][i].piece = (Piece) pieces[i].getConstructor(Cell.class, Board.class, Boolean.TYPE).newInstance(cells[7][i], this, true);
            }
            for (int i = 0; i < 8; i++) {
                cells[1][i].piece = new Pawn(cells[1][i], this, false);
                cells[6][i].piece = new Pawn(cells[6][i], this, true);
            }
        }catch (Exception ignored){

        }

    }
}
