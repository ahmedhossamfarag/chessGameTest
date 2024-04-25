package org.example.engine;

import org.example.models.Board;

public class Engine {

    public Board board;

    public void createGame(){
        board = new Board();
        board.build();
    }
}
