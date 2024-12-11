package test.java;

import org.example.Board;
import org.example.Dealer;
import org.example.GoodDealer;
import org.example.PokerResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MyCodeTest {

    @Test
    public void testCombination() {
        Dealer dealer = new GoodDealer();
        Board board = new Board("2D3S", "3D3H", "QSJSKS", "10S", "AS"); //DRAW
        PokerResult result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.DRAW, "DRAW");

        board = new Board("AD,3S", "QD,3H", "QS,JS,KS", "10S", "9S"); // PLAYER_ONE_WIN
        result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.PLAYER_ONE_WIN, "PLAYER_ONE_WIN");

        board = new Board("10S,3S", "7D,7S", "7H,9C,10C", "10H", "3D"); // PLAYER_ONE_WIN        result = dealer.decideWinner(board);
        result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.PLAYER_ONE_WIN, "PLAYER_ONE_WIN");

        board = new Board("QS5S", "JD6D", "KC4H8C", "7H", "4D"); // PLAYER_ONE_WIN        result = dealer.decideWinner(board);
        result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.PLAYER_ONE_WIN, "PLAYER_ONE_WIN");

        board = new Board("10C5S", "9H6D", "KC2SAC", "KS", "KD"); // PLAYER_ONE_WIN        result = dealer.decideWinner(board);
        result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.PLAYER_ONE_WIN, "PLAYER_ONE_WIN");

        board = new Board("10C5S", "9H6D", "KCJSAC", "KS", "KD"); //DRAW
        result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.DRAW, "DRAW");

        board = new Board("5CAD", "KCAC", "4S9H2C", "8D", "7C"); //PLAYER_TWO_WIN
        result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.PLAYER_TWO_WIN, "PLAYER_TWO_WIN");

        board = new Board("2C7D", "2DJD", "2H2SKH", "QC", "AS"); //DRAW
        result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.DRAW, "DRAW");

        board = new Board("3D3H", "KHKS", "AHKD3C", "2H", "4S"); //PLAYER_TWO_WIN
        result = dealer.decideWinner(board);
        assertTrue(result == PokerResult.PLAYER_TWO_WIN, "PLAYER_TWO_WIN");


/**
* Board board = new Board("2D3S", "3D3H", "QSJSKS", "10S", "AS"); //DRAW
* Board board = new Board("AD,3S", "QD,3H", "QS,JS,KS", "10S", "9S"); // PLAYER_ONE_WIN
* Board board = new Board("AH,5D", "4S,6H", "3D,9S,3S", "10D", "8S"); // PLAYER_ONE_WIN
* Board board = new Board("10S,3S", "7D,7S", "7H,9C,10C", "10H", "3D"); // PLAYER_ONE_WIN
* Board board = new Board("QS5S", "JD6D", "KC4H8C", "7H", "4D"); // PLAYER_ONE_WIN
* Board board = new Board("10C5S", "9H6D", "KC2SAC", "KS", "KD"); // PLAYER_ONE_WIN
* Board board = new Board("10C5S", "9H6D", "KCJSAC", "KS", "KD"); //DRAW
* Board board = new Board("5CAD", "KCAC", "4S9H2C", "8D", "7C"); //PLAYER_TWO_WIN
* Board board = new Board("2C7D", "2DJD", "2H2SKH", "QC", "AS"); //DRAW
* Board board = new Board("3D3H", "KHKS", "AHKD3C", "2H", "4S"); //PLAYER_TWO_WIN
*/
    }
}
