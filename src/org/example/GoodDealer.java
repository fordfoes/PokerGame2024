package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoodDealer implements Dealer { // Создаем класс хороший дилер который реализует интерфейс Dealer
    private final List<String> deck; // Это колода
    private int currentCardIndex; // Это переменная которая хранит текущий индекс карты в колоде

    public GoodDealer() {
        this.deck = createDeck(); // Здесь создается колода карт и присваивается переменной
        this.currentCardIndex = 0;
        Collections.shuffle(deck); // Это метод класса коллекций который перемешивает элементы в списке, в данном случаем колоду карт
    }

    private List<String> createDeck() {
        List<String> deck = new ArrayList<>();
        String[] suits = {"C", "D", "H", "S"}; // Определяем масти C — червы (Clubs) D — бубны (Diamonds) H — червы (Hearts) S — пики (Spades)
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"}; // Определяем ранги номера от 2 до 10, а также валет (J), дама (Q), король (K) и туз (A)
        // Создаем карты
        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(rank + suit); // Добавляем созданную строку в список deck
            }
        }
        return deck;
    } // Создаем колоду

    private String dealCard() {
        if (currentCardIndex >= deck.size()) { // Проверяем достигнут ли конец колоды
            throw new InvalidPokerBoardException("Закончились карты в колоде");
        }
        return deck.get(currentCardIndex++); // Возвращаем карту по текущему индексу и увеличиваем на 1
    } // Раздаем карту

    @Override
    public Board dealCardsToPlayers() {
        String playerOne = dealCard() + dealCard();
        String playerTwo = dealCard() + dealCard();

        // Убедимся что карты уникальны
        if (playerOne.equals(playerTwo)) {
            throw new InvalidPokerBoardException("Обнаружены одинаковые карты у игроков");
        }

        return new Board(playerOne, playerTwo, null, null, null);
    } // Раздаем 2 карты игрокам и получаем доску

    @Override
    public Board dealFlop(Board board) {
        String flop = dealCard() + dealCard() + dealCard(); // Раздача 3 карт
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), flop, null, null); // Возвращаем на игровую доску уже розданные карты игрока и флоп
    }

    @Override
    public Board dealTurn(Board board) {
        String turn = dealCard();
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), turn, null); // Возвращаем на игровую доску уже розданные карты игрока, флоп и тёрн
    }

    @Override
    public Board dealRiver(Board board) {
        String river = dealCard();
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), board.getTurn(), river); // Возвращаем на игровую доску уже розданные карты игрока, флоп и тёрн и ривер
    }

    @Override
    public PokerResult decideWinner(Board board) throws InvalidPokerBoardException {
        validateBoard(board);// Проверка что на игровом столе все в порядке

        List<String> playerOneCards = new ArrayList<>();

        // Регулярное выражение для поиска групп по 2 или 3 символа, заканчивающихся на C, D, H или S
        Pattern pattern = Pattern.compile("\\d{1,2}[CDHS]|[A-Z][CDHS]");
        Matcher matcher = pattern.matcher(board.getPlayerOne());

        while (matcher.find()) {
            playerOneCards.add(matcher.group());
        }

        // Создаем список карт для второго игрока, разделяя их по запятой
        List<String> playerTwoCards = new ArrayList<>();

        matcher = pattern.matcher(board.getPlayerTwo());

        while (matcher.find()) {
            playerTwoCards.add(matcher.group());
        }

        // Создаем список общих карт
        List<String> communityCards = new ArrayList<>();

        // Добавляем флоп (первые три общие карты), если он не равен null
        if (board.getFlop() != null) {
            matcher = pattern.matcher(board.getFlop());

            while (matcher.find()) {
                communityCards.add(matcher.group());
            }
        }

        // Добавляем терн (четвертая общая карта), если он не равен null
        if (board.getTurn() != null) {
            matcher = pattern.matcher(board.getTurn());

            while (matcher.find()) {
                communityCards.add(matcher.group());
            }
        }

        // Добавляем ривер (пятая общая карта), если он не равен null
        if (board.getRiver() != null) {
            matcher = pattern.matcher(board.getRiver());

            while (matcher.find()) {
                communityCards.add(matcher.group());
            }
        }

        // Создаем руки игроков, используя их карты и общие карты
        Hand playerOneHand = new Hand(playerOneCards, communityCards);
        Hand playerTwoHand = new Hand(playerTwoCards, communityCards);
        Hand cardsOnBoard = new Hand(communityCards);


        // Сравниваем руки игроков
        HandComparison comparison = new HandComparison(playerOneHand, playerTwoHand, cardsOnBoard);
        int comparisonResult = comparison.comparison();

        // Определяем победителя на основе результата сравнения
        if (comparisonResult > 0) {
            return PokerResult.PLAYER_ONE_WIN; // Первый игрок выигрывает
        } else if (comparisonResult < 0) {
            return PokerResult.PLAYER_TWO_WIN; // Второй игрок выигрывает
        } else {
            return PokerResult.DRAW; // Ничья
        }
    }

    private void validateBoard(Board board) {
        HashSet<String> allCards = new HashSet<>(); //

        // Регулярное выражение для поиска групп по 2 или 3 символа, заканчивающихся на C, D, H или S
        Pattern pattern = Pattern.compile("\\d{1,2}[CDHS]|[A-Z][CDHS]");
        Matcher matcher = pattern.matcher(board.getPlayerOne());

        while (matcher.find()) {
            allCards.add(matcher.group());
        }


        matcher = pattern.matcher(board.getPlayerTwo());

        while (matcher.find()) {
            allCards.add(matcher.group());
        }


        // Добавляем флоп (первые три общие карты), если он не равен null
        if (board.getFlop() != null) {
            matcher = pattern.matcher(board.getFlop());

            while (matcher.find()) {
                allCards.add(matcher.group());
            }
        }

        // Добавляем терн (четвертая общая карта), если он не равен null
        if (board.getTurn() != null) {
            matcher = pattern.matcher(board.getTurn());

            while (matcher.find()) {
                allCards.add(matcher.group());
            }
        }

        // Добавляем ривер (пятая общая карта), если он не равен null
        if (board.getRiver() != null) {
            matcher = pattern.matcher(board.getRiver());

            while (matcher.find()) {
                allCards.add(matcher.group());
            }
        }

        // Сравниваем на дубликаты и количество карт
        if (allCards.size() != 9) {
            throw new InvalidPokerBoardException("Найдены дубликаты на игровом столе или неверное количество карт");
        }
    }
}