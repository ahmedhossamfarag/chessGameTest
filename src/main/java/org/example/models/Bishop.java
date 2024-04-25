package org.example.models;

import org.example.exceptions.InvalidMovementException;

public class  Bishop extends Piece{

    public Bishop(Cell cell, Board board, boolean isWhite) {
        super(cell, board, isWhite);
    }

    @Override
    public void move(Cell cell) throws InvalidMovementException {

    }
}
