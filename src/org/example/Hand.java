package org.example;

import java.util.*;

public class Hand {
    private List<String> cards;
    private List<String> cardsOFPlayer;
    private List<String> cardsOnBoard;

    public Hand(List<String> playerCards, List<String> communityCards) {
        this.cards = new ArrayList<>(playerCards);
        this.cards.addAll(communityCards);
        int n = cards.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (getRank(cards.get(j)) < getRank(cards.get(j + 1))) {
                    // Меняем местами карты
                    String temp = cards.get(j);
                    cards.set(j, cards.get(j + 1));
                    cards.set(j + 1, temp);
                }
            }
        } // Сортировка по убыванию
        this.cardsOFPlayer = playerCards;

        int n1 = cardsOFPlayer.size();
        for (int i = 0; i < n1 - 1; i++) {
            for (int j = 0; j < n1 - 1 - i; j++) {
                if (getRank(cardsOFPlayer.get(j)) < getRank(cardsOFPlayer.get(j + 1))) {
                    // Меняем местами карты
                    String temp = cardsOFPlayer.get(j);
                    cardsOFPlayer.set(j, cardsOFPlayer.get(j + 1));
                    cardsOFPlayer.set(j + 1, temp);
                }
            }
        } // Сортировка по убыванию

        this.cardsOnBoard = communityCards;

        int n2 = cardsOnBoard.size();
        for (int i = 0; i < n2 - 1; i++) {
            for (int j = 0; j < n2 - 1 - i; j++) {
                if (getRank(cardsOnBoard.get(j)) < getRank(cardsOnBoard.get(j + 1))) {
                    // Меняем местами карты
                    String temp = cardsOnBoard.get(j);
                    cardsOnBoard.set(j, cardsOnBoard.get(j + 1));
                    cardsOnBoard.set(j + 1, temp);
                }
            }
        } // Сортировка по убыванию

    } // В конструкторе принимаем карты игрока, общие карты и сортируем их от большего к меньшему

    public Hand(List<String> cardsOnTable){
        this.cards = cardsOnTable;
        int n = cards.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (getRank(cards.get(j)) < getRank(cards.get(j + 1))) {
                    // Меняем местами карты
                    String temp = cards.get(j);
                    cards.set(j, cards.get(j + 1));
                    cards.set(j + 1, temp);
                }
            }
        } // Сортировка по убыванию
    }

    private Integer getRank(String card) {
        String rank = card.trim().substring(0, card.length() - 1).trim();
        switch (rank) {
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "10":
                return 10;
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            case "A":
                return 14;
            default:
                return 0;
        }
    } // Получаем ранг карты

    private String getSuit(String card) {
        return card.substring(card.length() - 1); // Получаем масть
    }

    public HandRank evaluateHand() {
        // Хранит карты по рангам
        Map<String, List<String>> rankCounts = new HashMap<>();
        // Хранит количество карт по мастям
        Map<String, Integer> suitCounts = new HashMap<>();
        //System.out.println(cards);
        // Проходим по всем картам в руке
        for (String card : cards) {
            // Извлекаем ранг карты (все символы, кроме последнего)
            String rank = card.substring(0, card.length() - 1);

            // Если такого ранга еще нет в rankCounts, добавляем его
            rankCounts.putIfAbsent(rank, new ArrayList<>());
            // Добавляем карту в список карт данного ранга
            rankCounts.get(rank).add(card);

            // Извлекаем масть карты
            String suit = getSuit(card);
            // Увеличиваем счетчик для данной масти
            suitCounts.put(suit, suitCounts.getOrDefault(suit, 0) + 1);
        }

        // Проверяем, есть ли флеш (5 или более карт одной масти)
        boolean isFlush = false;
        for (Integer count : suitCounts.values()) {
            if (count >= 5) {
                isFlush = true; // Установим флаг, если нашли флеш
                break; // Достаточно найти одну масть с 5 картами
            }
        }

        // Собираем количество карт по рангам в список
        List<Integer> counts = new ArrayList<>();
        for (List<String> rankList : rankCounts.values()) {
            counts.add(rankList.size()); // Добавляем количество карт для каждого ранга
        }
        // Сортируем количество карт по убыванию
        Collections.sort(counts, Collections.reverseOrder());

        // Проверяем, есть ли стрит (пять последовательных карт)
        boolean isStraight = checkStraight();

        // Проверяем комбинации рук, начиная с наиболее сильных
        if (isFlush && isStraight) {
            // Если флеш и стрит, проверяем, является ли это Роял Флеш
            if (getRank(cards.get(0)) == 14) {
                if (cards.contains("10S") && cards.contains("JS") && cards.contains("QS") &&
                        cards.contains("KS") && cards.contains("AS")) {
                    return HandRank.ROYAL_FLUSH;
                }
                if (cards.contains("10H") && cards.contains("JH") && cards.contains("QH") &&
                        cards.contains("KH") && cards.contains("AH")) {
                    return HandRank.ROYAL_FLUSH;
                }
                if (cards.contains("10D") && cards.contains("JD") && cards.contains("QD") &&
                        cards.contains("KD") && cards.contains("AD")) {
                    return HandRank.ROYAL_FLUSH;
                }
                if (cards.contains("10C") && cards.contains("JC") && cards.contains("QC") &&
                        cards.contains("KC") && cards.contains("AC")) {
                    return HandRank.ROYAL_FLUSH;
                }
            }
            return HandRank.STRAIGHT_FLUSH;
        }
        if (counts.get(0) == 4) return HandRank.FOUR_OF_A_KIND; // Четверка
        if (counts.get(0) == 3 && counts.size() > 1 && counts.get(1) == 2) return HandRank.FULL_HOUSE; // Фулл Хаус
        if (isFlush) return HandRank.FLUSH; // Флеш
        if (isStraight) return HandRank.STRAIGHT; // Стрит
        if (counts.get(0) == 3) return HandRank.THREE_OF_A_KIND; // Тройка
        if (counts.get(0) == 2 && counts.size() > 1 && counts.get(1) == 2) return HandRank.TWO_PAIR; // Две пары
        if (counts.get(0) == 2) return HandRank.ONE_PAIR; // Пара

        // Если ни одна из комбинаций не подошла, возвращаем старшую карту
        return HandRank.HIGH_CARD;
    }

    private boolean checkStraight() {
        Set<Integer> uniqueRanks = new HashSet<>();
        for (String card : cards) {
            uniqueRanks.add(getRank(card));
        } // Добавим в Set уникальные карты

        List<Integer> sortedRanks = new ArrayList<>(uniqueRanks);
        Collections.sort(sortedRanks); // Отсортируем от меньшего к большему

        // Проверка обычного стрита
        for (int i = 0; i <= sortedRanks.size() - 5; i++) {
            if (sortedRanks.get(i + 4) - sortedRanks.get(i) == 4) {
                return true;
            }
        }

        // Проверка стрита с тузом как низкой картой (A, 2, 3, 4, 5)
        return uniqueRanks.contains(14) && uniqueRanks.contains(2) && uniqueRanks.contains(3) && uniqueRanks.contains(4) && uniqueRanks.contains(5);
    }

    public int comparison(Hand other) {
        HandRank thisRank = this.evaluateHand();
        System.out.println("1 player: " + thisRank);

        HandRank otherRank = other.evaluateHand();
        System.out.println("2 player: " + otherRank);

        Hand boardCards = new Hand(cardsOnBoard);

        HandRank boardRank = boardCards.evaluateHand();
        System.out.println("Board Rank: " + boardRank);

        int rankComparison = thisRank.compareTo(otherRank);

        if (rankComparison != 0) { // Если руки не равны, то возвращаем значение
            return rankComparison;
        }
        // Если у нас одинаковая комбинация, сравниваем старшие карты
        List<Integer> thisHighCards = this.getHighCards();// 1 игрок
        List<Integer> otherHighCards = other.getHighCards();// 2 игрок



        List<Integer> boardHighCards = boardCards.getHighCards(); // Старшая карта игрового стола

        List<String> thisCardsP1 = this.cardsOFPlayer;// 1 игрок 2 карты в руке
        List<String> otherCards1 = other.cardsOFPlayer;// 2 игрок 2 карты в руке

        //System.out.println("Hand 1p: " + thisCardsP1);
        //System.out.println("Hand 2p: " + otherCards1);

        List<Integer> thisHighCards1 = new ArrayList<>(); // 1 игрок ранги карт
        List<Integer> otherHighCards2= new ArrayList<>(); // 2 игрок ранги карт

        for (int i = 0; i < thisCardsP1.size();i++) {
            thisHighCards1.add(getRank(thisCardsP1.get(i)));
        }// Добавляем ранги

        for (int i = 0; i < otherCards1.size();i++) {
            otherHighCards2.add(getRank(otherCards1.get(i)));
        }// Добавляем ранги
        //System.out.println("Hand 1p Integer: " + thisHighCards1);
        //System.out.println("Hand 2p Integer: " + otherHighCards2);


        //System.out.println("Cards: " + boardHighCards);

        if (thisRank == boardRank && otherRank == boardRank) {
            System.out.println(boardHighCards.getFirst() + "---------" + thisHighCards1.getFirst());
            if (boardHighCards.getFirst() < thisHighCards1.getFirst()
                    && boardHighCards.getFirst() > otherHighCards2.getFirst() ) {
                //System.out.println("---1---");
                return 1;
            }
            if (boardHighCards.getFirst() > thisHighCards1.getFirst()
                    && boardHighCards.getFirst() < otherHighCards2.getFirst()) {
                //System.out.println("---2---");
                return -1;
            }
            if (boardHighCards.getFirst() > thisHighCards1.getFirst()
                    && boardHighCards.getFirst() > otherHighCards2.getFirst()) {
                System.out.println("---3---");
                if (boardRank == HandRank.ONE_PAIR) {
                    return Integer.compare(thisHighCards1.getFirst(), otherHighCards2.getFirst());
                }

                return 0;
            }
            if (boardHighCards.getFirst() < thisHighCards1.getFirst()
                    && boardHighCards.getFirst() < otherHighCards2.getFirst()) {

                //System.out.println("---4---");
                return Integer.compare(thisHighCards1.getFirst(), otherHighCards2.getFirst());
            }
        } // Сравнение когда комбинация на игровом столе

        if (thisRank == HandRank.ONE_PAIR && otherRank == HandRank.ONE_PAIR) {
            int thisPairValue = -1; // Инициализируем переменную для значения пары
            int otherPairValue = -1; // Инициализируем переменную для значения пары

            // Находим значение пары для этой руки
            for (int cardValue : thisHighCards) {
                if (Collections.frequency(thisHighCards, cardValue) == 2) {
                    thisPairValue = cardValue;
                    break; // Выходим из цикла, как только нашли пару
                }
            }

            // Находим значение пары для другой руки
            for (int cardValue : otherHighCards) {
                if (Collections.frequency(otherHighCards, cardValue) == 2) {
                    otherPairValue = cardValue;
                    break; // Выходим из цикла, как только нашли пару
                }
            }

            // Проверяем, нашли ли мы пары
            if (thisPairValue != -1 && otherPairValue != -1) {
                // Сравниваем пары
                if (thisPairValue != otherPairValue) {
                    return Integer.compare(thisPairValue, otherPairValue);
                }
            }
            // Если пары равны, сравниваем кикеры
            List<Integer> thisKickers = thisHighCards.subList(1, thisHighCards.size());
            List<Integer> otherKickers = otherHighCards.subList(1, otherHighCards.size());

            for (int i = 0; i < Math.min(thisKickers.size(), otherKickers.size()); i++) {
                int kickerComparison = Integer.compare(thisKickers.get(i), otherKickers.get(i));
                if (kickerComparison != 0) {
                    return kickerComparison; // Возвращаем результат сравнения кикеров
                }
            }
        }


        // Сравниваем старшие карты, если не пара
        for (int i = 0; i < Math.min(thisHighCards.size(), otherHighCards.size()); i++) {
            int comparison = Integer.compare(thisHighCards.get(i), otherHighCards.get(i));
            if (comparison != 0) {
                return comparison; // Возвращаем результат сравнения старших карт
            }
        }

        return 0; // Если все старшие карты равны, руки равны
    }


    private List<Integer> getHighCards() {
        Set<String> rankCounts = new HashSet<>();
        for (String card : cards) {
            // Извлекаем ранг карты
            String rank = card.substring(0, card.length() - 1);

            // Добавляем ранг в множество
            rankCounts.add(rank);
        }

        // Сортируем ранги по убыванию
        List<String> sortedRanks = new ArrayList<>(cards);
        sortedRanks.sort((a, b) -> getRank(b).compareTo(getRank(a)));

        // Преобразуем отсортированные ранги в список их числовых значений
        List<Integer> highCards = new ArrayList<>();
        for (String rank : sortedRanks) {
            highCards.add(getRank(rank));
        }

        // Проверка для Full House (если есть тройка и пара)
        if (highCards.size() >= 2) {
            boolean hasThreeOfAKind = false;
            boolean hasPair = false;

            for (String rank : rankCounts) {
                long count = 0;
                for (String card : cards) {
                    if (card.startsWith(rank)) {
                        count++;
                    }
                }

                if (count >= 3) {
                    hasThreeOfAKind = true;
                } else if (count >= 2) {
                    hasPair = true;
                }
            }

            // Если есть тройка и пара
            if (hasThreeOfAKind && hasPair) {
                // Добавляем тройку
                highCards.add(highCards.get(0)); // Старший ранг как тройка
                // Добавляем пару
                if (highCards.size() > 1) {
                    highCards.add(highCards.get(1)); // Следующий старший ранг как пара
                }
            }
        }
        return highCards;
    }

}