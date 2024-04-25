package org.example.models;

import org.example.exceptions.InvalidMovementException;

public abstract class Piece {
    public Cell cell;

    public Board board;

    public boolean isWhite;

    public Piece(Cell cell, Board board, boolean isWhite) {
        this.cell = cell;
        this.board = board;
        this.isWhite = isWhite;
    }

    public abstract void move(Cell cell) throws InvalidMovementException;
}
