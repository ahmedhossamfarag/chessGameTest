package org.example.models;

import org.example.exceptions.InvalidMovementException;

public class Knight extends Piece{


    public Knight(Cell cell, Board board, boolean isWhite) {
        super(cell, board, isWhite);
    }

    @Override
    public void move(Cell cell) throws InvalidMovementException {

    }
}
