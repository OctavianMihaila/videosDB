package main;

import java.util.*;

public class SortUtils {
    public static <K, V extends Comparable<? super V>> Map<K, V> SortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Sorting a Map by a 2nd criteria (key), while first criteria remains value.
     */
    public static ArrayList<String> SortbyKey(Map<String, Double> Ratings) {
        HashMap<Double, ArrayList<String>> hashmap = new HashMap<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> OrderedTitles = new ArrayList<>();
        boolean FirstIteration = true;
        Double TargetRating = 0d, CurrentRating;

        for (Map.Entry<String, Double> entry: Ratings.entrySet()) {
            CurrentRating = entry.getValue();
            if (FirstIteration) {
                TargetRating = CurrentRating;
                FirstIteration = false;
            }
            if (CurrentRating.doubleValue() != TargetRating.doubleValue()) {
                hashmap.put(TargetRating, titles);
                titles = new ArrayList<>();
                TargetRating = CurrentRating;
            }
            titles.add(entry.getKey());
        }
        hashmap.put(TargetRating, titles);

        for (Map.Entry<Double, ArrayList<String>> entry: hashmap.entrySet()) {
            ArrayList<String> temp = entry.getValue();
            Collections.sort(temp);
            OrderedTitles.addAll(temp);
        }
        if (titles.isEmpty()) {
            return null;
        } else {
            return OrderedTitles;
        }
    }
}
