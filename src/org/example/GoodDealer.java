package org.example; // Указываем пакет
// Указываем импорты
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;// Указываем импорты// Указываем импорты

public class GoodDealer implements Dealer { // Создаем класс хороший дилер который реализует интерфейс Dealer
    // Определяем поля класса
    private final List<String> deck; // Это колода
    private int currentCardIndex; // Это переменная которая хранит текущий индекс карты в колоде

    public GoodDealer() { // Это конструктор
        this.deck = createDeck(); // Здесь создается колода карт и присваивается переменной
        this.currentCardIndex = 0; // Индекс списка начинается с 0
        Collections.shuffle(deck); // Это метод класса коллекций который перемешивает элементы в списке, в данном случаем колоду карт
    } // Чешем колоду

    private List<String> createDeck() { // Метод возвращает список строк
        List<String> deck = new ArrayList<>(); // Создаем объект класса ArrayList<>(), который будет хранить карты
        String[] suits = {"C", "D", "H", "S"}; // Определяем масти C — червы (Clubs) D — бубны (Diamonds) H — червы (Hearts) S — пики (Spades)
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"}; // Определяем ранги номера от 2 до 10, а также валет (J), дама (Q), король (K) и туз (A)
        // Создаем карты
        for (String suit : suits) { // Перебираем масти
            for (String rank : ranks) { // Перебираем ранги
                deck.add(rank + suit); // Добавляем созданную строку в список deck
            }
        }
        return deck; // Возвращаем колоду
    } // Создаем колоду

    private String dealCard() { // Это метод раздачи колоды
        if (currentCardIndex >= deck.size()) { // Проверяем достигнут ли конец колоды
            throw new InvalidPokerBoardException("Закончились карты в колоде");
        }
        return deck.get(currentCardIndex++); // Возвращаем карту по текущему индексу и увеличиваем на 1
    } // Раздаем карту

    // Переопределяем метод интерфейса раздача карт игрокам
    @Override
    public Board dealCardsToPlayers() {
        String playerOne = dealCard() + "," + dealCard(); // Раздаем две карты первому игроку
        String playerTwo = dealCard() + "," + dealCard(); // Раздаем две карты второму игроку

        // Убедимся что карты уникальны
        if (playerOne.equals(playerTwo)) {
            throw new InvalidPokerBoardException("Обнаружены одинаковые карты у игроков");
        }

        return new Board(playerOne, playerTwo, null, null, null);
    } // Раздаем 2 карты игрокам и получаем доску

    @Override // Переопределяем метод интерфейса раздача 3 карт общих для игроков
    public Board dealFlop(Board board) {
        String flop = dealCard() + "," + dealCard() + "," + dealCard(); // Раздача 3 карт
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), flop, null, null); // Возвращаем на игровую доску уже розданные карты игрока и флоп
    }

    @Override // Переопределяем метод интерфейса раздача еще 1 общей карты (тёрн) для игроков
    public Board dealTurn(Board board) {
        String turn = dealCard();
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), turn, null); // Возвращаем на игровую доску уже розданные карты игрока, флоп и тёрн
    }

    @Override // Переопределяем метод интерфейса раздача еще 1 общей карты (ривер) для игроков
    public Board dealRiver(Board board) {
        String river = dealCard();
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), board.getTurn(), river); // Возвращаем на игровую доску уже розданные карты игрока, флоп и тёрн и ривер
    }

    @Override
    public PokerResult decideWinner(Board board) throws InvalidPokerBoardException { // Определяем победителя
        validateBoard(board);// Проверка что на игровом столе все в порядке

        // Создаем список карт для первого игрока, разделяя их по запятой
        List<String> playerOneCards = new ArrayList<>();
        Collections.addAll(playerOneCards, board.getPlayerOne().split(","));

        // Создаем список карт для второго игрока, разделяя их по запятой
        List<String> playerTwoCards = new ArrayList<>();
        Collections.addAll(playerTwoCards, board.getPlayerTwo().split(","));

        // Создаем список общих карт
        List<String> communityCards = new ArrayList<>();

        // Добавляем флоп (первые три общие карты), если он не равен null
        if (board.getFlop() != null) {
            Collections.addAll(communityCards, board.getFlop().split(","));
        }

        // Добавляем терн (четвертая общая карта), если он не равен null
        if (board.getTurn() != null) {
            communityCards.add(board.getTurn());
        }

        // Добавляем ривер (пятая общая карта), если он не равен null
        if (board.getRiver() != null) {
            communityCards.add(board.getRiver());
        }

        // Создаем руки игроков, используя их карты и общие карты
        Hand playerOneHand = new Hand(playerOneCards, communityCards);
        Hand playerTwoHand = new Hand(playerTwoCards, communityCards);

        // Сравниваем руки игроков
        int comparisonResult = playerOneHand.comparison(playerTwoHand);

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
        HashSet<String> allCards = new HashSet<>(); // Создаем HashSet для хранения уникальных карт
        // Разделяем карты игрока 1
        Collections.addAll(allCards, board.getPlayerOne().split(","));

        // Разделяем карты игрока 2
        Collections.addAll(allCards, board.getPlayerTwo().split(","));

        // Разделяем карты флопа, если они есть
        if (board.getFlop() != null) {
            Collections.addAll(allCards, board.getFlop().split(","));
        }

        // Добавляем карту тёрна, если она есть
        if (board.getTurn() != null) {
            Collections.addAll(allCards, board.getTurn().split(","));
        }

        // Добавляем карту ривера, если она есть
        if (board.getRiver() != null) {
            Collections.addAll(allCards, board.getRiver().split(","));
        }

        // Сравниваем на дубликаты и количество карт
        if (allCards.size() != 9) {
            throw new InvalidPokerBoardException("Найдены дубликаты на игровом столе или неверное количество карт");
        }
    }
}