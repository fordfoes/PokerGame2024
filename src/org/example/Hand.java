package org.example;

import java.util.*;

public class Hand {
    private List<String> cards;
    private List<String> cardsOfPlayer;
    private List<String> cardsOnBoard;

    public List<String> getCardsOnBoard() {
        return cardsOnBoard;
    }

    public List<String> getCardsOfPlayer() {
        return cardsOfPlayer;
    }

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
        this.cardsOfPlayer = playerCards;

        int n1 = cardsOfPlayer.size();
        for (int i = 0; i < n1 - 1; i++) {
            for (int j = 0; j < n1 - 1 - i; j++) {
                if (getRank(cardsOfPlayer.get(j)) < getRank(cardsOfPlayer.get(j + 1))) {
                    // Меняем местами карты
                    String temp = cardsOfPlayer.get(j);
                    cardsOfPlayer.set(j, cardsOfPlayer.get(j + 1));
                    cardsOfPlayer.set(j + 1, temp);
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

    }

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

    public Integer getRank(String card) {
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
        Map<String, List<String>> rankCounts = new HashMap<>();
        // Хранит количество карт по мастям
        Map<String, Integer> suitCounts = new HashMap<>();
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
        if (counts.get(0) == 4) return HandRank.FOUR_OF_A_KIND;
        if (counts.get(0) == 3 && counts.size() > 1 && counts.get(1) == 2) return HandRank.FULL_HOUSE;
        if (isFlush) return HandRank.FLUSH;
        if (isStraight) return HandRank.STRAIGHT;
        if (counts.get(0) == 3) return HandRank.THREE_OF_A_KIND;
        if (counts.get(0) == 2 && counts.size() > 1 && counts.get(1) == 2) return HandRank.TWO_PAIR;
        if (counts.get(0) == 2) return HandRank.ONE_PAIR;

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




    public List<Integer> getHighCards() {
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