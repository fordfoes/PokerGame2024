package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandComparison {

    private Hand hand1;
    private Hand hand2;
    private Hand cardsOnBoard;

    public HandComparison(Hand hand1, Hand hand2, Hand cardsOnBoard) {
        this.hand1 = hand1;
        this.hand2 = hand2;
        this.cardsOnBoard = cardsOnBoard;
    }

    public int comparison() {
        HandRank thisRank = hand1.evaluateHand();
        HandRank otherRank = hand2.evaluateHand();
        HandRank boardRank = cardsOnBoard.evaluateHand();

        int rankComparison = thisRank.compareTo(otherRank);

        if (rankComparison != 0) { // Если руки не равны, то возвращаем значение
            return rankComparison;
        }
        // Если у нас одинаковая комбинация, сравниваем старшие карты
        List<Integer> thisHighCards = hand1.getHighCards();// 1 игрок
        List<Integer> otherHighCards = hand2.getHighCards();// 2 игрок
        List<Integer> boardHighCards = cardsOnBoard.getHighCards(); // Старшая карта игрового стола

        List<String> thisCardsP1 = hand1.getCardsOfPlayer();// 1 игрок 2 карты в руке
        List<String> otherCards1 = hand2.getCardsOfPlayer();// 2 игрок 2 карты в руке

        List<Integer> thisCardsRankP1 = new ArrayList<>(); // 1 игрок ранги карт
        List<Integer> otherCardsRankP2= new ArrayList<>(); // 2 игрок ранги карт

        for (int i = 0; i < thisCardsP1.size();i++) {
            thisCardsRankP1.add(hand1.getRank(thisCardsP1.get(i)));
        }// Добавляем ранги

        for (int i = 0; i < otherCards1.size();i++) {
            otherCardsRankP2.add(hand1.getRank(otherCards1.get(i)));
        }// Добавляем ранги

        List<Integer> allHighCards1 = new ArrayList<>();
        allHighCards1.addAll(thisCardsRankP1);
        allHighCards1.addAll(otherCardsRankP2);
        int n = allHighCards1.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (allHighCards1.get(j) < allHighCards1.get(j + 1)) {
                    // Меняем местами карты
                    int temp = allHighCards1.get(j);
                    allHighCards1.set(j, allHighCards1.get(j + 1));
                    allHighCards1.set(j + 1, temp);
                }
            }
        } // Сортировка по убыванию

        if (thisRank == boardRank && otherRank == boardRank) {
            if (boardHighCards.get(0) < thisCardsRankP1.get(0)
                    && boardHighCards.get(0) > otherCardsRankP2.get(0) ) {
                return 1;
            }
            if (boardHighCards.get(0) > thisCardsRankP1.get(0)
                    && boardHighCards.get(0) < otherCardsRankP2.get(0)) {
                return -1;
            }

            if (boardHighCards.get(0) < thisCardsRankP1.get(0)
                    && boardHighCards.get(0) < otherCardsRankP2.get(0)) {
                if (0 == Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0))) {
                    if (0 == Integer.compare(thisCardsRankP1.get(1), otherCardsRankP2.get(1))) {
                        return 0;
                    } else {
                        return Integer.compare(thisCardsRankP1.get(1), otherCardsRankP2.get(1));
                    }
                } else {
                    return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
                }
            }

            if (boardHighCards.get(0) > thisCardsRankP1.get(0)
                    && boardHighCards.get(0)> otherCardsRankP2.get(0)) {
                if (boardRank == HandRank.HIGH_CARD) {
                    if (boardHighCards.get(boardHighCards.size() - 1) < allHighCards1.get(0)) {
                        return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
                    }
                }
                if (boardRank == HandRank.ONE_PAIR) {
                    if (boardHighCards.get(boardHighCards.size() - 1) < allHighCards1.get(0)) {
                        return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
                    }
                }
                if (boardRank == HandRank.TWO_PAIR) {
                    if (boardHighCards.get(boardHighCards.size() - 1) < allHighCards1.get(0)) {
                        return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
                    }
                }
                if (boardRank == HandRank.THREE_OF_A_KIND) {
                    if (boardHighCards.get(boardHighCards.size() - 1) < allHighCards1.get(0)) {
                        return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
                    }
                }
                if (boardRank == HandRank.STRAIGHT) {
                    if (boardHighCards.get(boardHighCards.size() - 1) < allHighCards1.get(0)) {
                        return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
                    }
                }
                if (boardRank == HandRank.FOUR_OF_A_KIND) {
                    if (boardHighCards.get(boardHighCards.size() - 1) < allHighCards1.get(0)) {
                        return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
                    }
                }

                return 0;
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

        if (thisRank == HandRank.THREE_OF_A_KIND && otherRank == HandRank.THREE_OF_A_KIND) {
            if (boardHighCards.get(0) < allHighCards1.get(0)) {
                return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
            } else if (thisCardsRankP1.get(0)!=otherCardsRankP2.get(0)&&thisCardsRankP1.get(1)!=otherCardsRankP2.get(1)) {
                return Integer.compare(thisCardsRankP1.get(0), otherCardsRankP2.get(0));
            } else {
                return 0;
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
}
