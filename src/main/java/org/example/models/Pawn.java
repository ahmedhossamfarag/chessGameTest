package org.example.models;

import org.example.exceptions.InvalidMovementException;

public class Pawn extends Piece{


    public Pawn(Cell cell, Board board, boolean isWhite) {
        super(cell, board, isWhite);
    }

    @Override
    public void move(Cell cell) throws InvalidMovementException {

    }
}
