package org.example.models;

import org.example.exceptions.InvalidMovementException;

public class Queen extends Piece{


    public Queen(Cell cell, Board board, boolean isWhite) {
        super(cell, board, isWhite);
    }

    @Override
    public void move(Cell cell) throws InvalidMovementException {

    }
}
