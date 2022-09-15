package fileio;

import actor.ActorsAwards;
import main.ActorsQuery;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * Information about an actor, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class ActorInputData {
    /**
     * actor name
     */
    private String name;
    /**
     * description of the actor's career
     */
    private String careerDescription;
    /**
     * videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * awards won by the actor
     */
    private Map<ActorsAwards, Integer> awards;

    /**
     * Average evaluation for movies in which the actor performed
     */

    private Double RatingAverage;

    private Integer NrAwards;

    public ActorInputData(final String name, final String careerDescription,
                          final ArrayList<String> filmography,
                          final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
        this.RatingAverage = Double.valueOf(0); // default value;
        this.NrAwards = 0; // default value;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public Double getRatingAverage() {
        return RatingAverage;
    }

    public Integer getNrAwards() {
        return NrAwards;
    }

    public final void UpdateRatingAverage(Double NewRatingAverage) {
        this.RatingAverage = NewRatingAverage;
    }

    public Double CalculateRatingAverage(ActorInputData actor, List<MovieInputData> movies,
                                         List<SerialInputData> series) {

        Double RatingAverage = Double.valueOf(0);
        int count = 0;
        for (int i = 0; i < actor.getFilmography().size(); i++) {
            String title = actor.getFilmography().get(i);
            // Find the title in the movies/series arrays and get the rating
            for (int j = 0; j < movies.size(); j++) {
                if (movies.get(j).getTitle().equals(title) && (movies.get(j).getGrade() != 0)) {
                    RatingAverage = Double.sum(RatingAverage, movies.get(j).getGrade());
                    count++;
                }
            }
            for (int j = 0; j < series.size(); j++) {
                if (series.get(j).getTitle().equals(title) && (series.get(j).getGrade() != 0)) {
                    RatingAverage = Double.sum(RatingAverage, series.get(j).getGrade());
                    count++;
                }
            }

        }

        RatingAverage = RatingAverage / count;
        return RatingAverage;
    }

    /**
     * Sorting actors ascending or descending by RatingAverage
     * Returns just a sorted Treemap with contains the names and values.
     */
    public static List<ActorInputData> SortByRatingAverage(List<ActorInputData> actors, String order) {
        List<ActorInputData> SortedActors = actors;
        Collections.sort(SortedActors, Comparator.comparingDouble(ActorInputData::getRatingAverage));
        if (order.equals("desc")) {
            Collections.reverse(SortedActors);
        }

        return SortedActors;
    }

    /**
     * Selecting the first N names from the sorted list of Actors
     */
    public static ArrayList<String> getFirstN(List<ActorInputData> SortedActors, int N) {
        ArrayList<String> Names = new ArrayList<String>();
        for (int i = 0; i < N; i++) {
            Names.add(SortedActors.get(i).getName());
        }

        return Names;
    }

    /**
     * Returns a list of strings that represent the names of the actors
     */
    public static ArrayList<String> getNames(List<ActorInputData> actors) {
        ArrayList<String> Names = new ArrayList<String>();
        for (int i = 0; i < actors.size(); i++) {
            Names.add(actors.get(i).getName());
        }

        return Names;
    }

    /**
     * Checks if an actor got all the awards from the list.
     */

    public static boolean CheckAwards(ActorInputData actor, List<String> awards) {
        for (int i = 0; i < actor.getAwards().size(); i++) {
            if (awards.contains(actor.getAwards().get(i))) {
                continue;
            }
            else {
                return false;
            }
        }

        return true;
    }

    /**
     * Selecting the Actors that have all the prizes requested.
     */
    public static List<ActorInputData> SelectAvailable(List<ActorInputData> actors, List<String> awards) {
        List<ActorInputData> AvailableActors = new ArrayList<ActorInputData>();
        for (int i = 0; i < actors.size(); i++) {
            if (actors.get(i).getAwards().size() != 0) {
                if (ActorInputData.CheckAwards(actors.get(i), awards) == true) {
                    AvailableActors.add(actors.get(i));
                }
            }
        }
        return AvailableActors;
    }

    public static void CalculateNrAwards(List<ActorInputData> actors) {
        for (int i = 0; i < actors.size(); i++) {
            int count = 0;
            ActorInputData actor = actors.get(i);
            for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                count += entry.getValue();
            }
            actor.NrAwards = count;
        }
    }

    public static List<ActorInputData> SortByNrOfAwards(List<ActorInputData> actors, String order) {
        List<ActorInputData> SortedActors = actors;
        Collections.sort(SortedActors, Comparator.comparing(ActorInputData::getNrAwards));

        if (order.equals("desc")) {
            Collections.reverse(SortedActors);
        }

        return SortedActors;
    }

    /**
     * Checking if all the words from the list are included in description.
     */
    public boolean CheckWordsInString(String description, List<String> words) {
        for (int i = 0; i < words.size(); i++) {
            if (description.indexOf(words.get(i)) != -1) {
                continue;
            }
            else {
                return false;
            }
        }

        return true;
    }

    /**
     * Building a List that contains all the actors that have
     * all the names given from the input in their description.
     */
    public static List<ActorInputData> SelectRequiredActors(List<ActorInputData> actors, List<String> words) {
        List<ActorInputData> SelectedActors = new ArrayList<ActorInputData>();
        for (int i = 0; i < actors.size(); i++) {
            if (actors.get(i).CheckWordsInString(actors.get(i).getCareerDescription(), words)) {
                SelectedActors.add(actors.get(i));
            }
        }

        return SelectedActors;
    }

    public static ArrayList<String> ColectNames(List<ActorInputData> SelectedActors) {
        ArrayList<String> names = new ArrayList<String>();
        SelectedActors.forEach(actor -> names.add(actor.getName()));

        return names;
    }
    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }
}
