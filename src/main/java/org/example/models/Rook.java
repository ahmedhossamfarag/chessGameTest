package org.example.models;

import org.example.exceptions.InvalidMovementException;

public class Rook extends Piece{

    public Rook(Cell cell, Board board, boolean isWhite) {
        super(cell, board, isWhite);
    }

    @Override
    public void move(Cell cell) throws InvalidMovementException {

    }
}
