package org.example;

public class PokerGame {
    public static void main(String[] args) {
        Dealer dealer = new GoodDealer();
        Board board = dealer.dealCardsToPlayers();

        board = dealer.dealFlop(board);

        board = dealer.dealTurn(board);

        board = dealer.dealRiver(board);
//        Board board = new Board("4D,10H", "10S,JS", "2S,QS,KS", "AH", "AS");
        System.out.println(board);

        try {
            PokerResult result = dealer.decideWinner(board);
            System.out.println("Результат: " + result);
        } catch (InvalidPokerBoardException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}