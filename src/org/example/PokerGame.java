package org.example;

public class PokerGame {
    public static void main(String[] args) {
        Dealer dealer = new GoodDealer();
        Board board = dealer.dealCardsToPlayers();

        board = dealer.dealFlop(board);

        board = dealer.dealTurn(board);

        board = dealer.dealRiver(board);
//        Board board = new Board("2D3S", "3D3H", "QSJSKS", "10S", "AS"); //DRAW
//        Board board = new Board("AD,3S", "QD,3H", "QS,JS,KS", "10S", "9S"); // PLAYER_ONE_WIN
//        Board board = new Board("AH,5D", "4S,6H", "3D,9S,3S", "10D", "8S"); // PLAYER_ONE_WIN
//        Board board = new Board("10S,3S", "7D,7S", "7H,9C,10C", "10H", "3D"); // PLAYER_ONE_WIN
//        Board board = new Board("QS5S", "JD6D", "KC4H8C", "7H", "4D"); // PLAYER_ONE_WIN
//        Board board = new Board("10C5S", "9H6D", "KC2SAC", "KS", "KD"); // PLAYER_ONE_WIN
//        Board board = new Board("10C5S", "9H6D", "KCJSAC", "KS", "KD"); //DRAW

        System.out.println(board);

        try {
            PokerResult result = dealer.decideWinner(board);
            System.out.println("Результат: " + result);
        } catch (InvalidPokerBoardException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}