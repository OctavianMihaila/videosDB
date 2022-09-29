package fileio;

import entertainment.Season;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Information about a tv show, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput<List<SerialInputData>, List<UserInputData>, String> {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    private int duration;

    public SerialInputData(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        this.duration = 0; // default value.
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public int getDuration() {
        return  duration;
    }

    public void setDuration(int NewDuration) {
        this.duration = NewDuration;
    }

    public static SerialInputData FindSeries(List<SerialInputData> series, String title) {
        for (int i = 0; i < series.size(); i++) {
            if (series.get(i).getTitle().equals(title)) {
                return series.get(i);
            }
        }

        return null;
    }

    /**
     * Updating the grade every time a season from a series receives an evaluation
     */
    public static void UpdateSeriesGrade(SerialInputData series) {
        Double sum = Double.valueOf(0);
        for (int i = 0 ; i < series.getSeasons().size(); i++) {
            sum = Double.sum(sum, series.getSeasons().get(i).getRating());
        }
        double value = sum.doubleValue() / series.numberOfSeasons;
        series.UpdateGrade(value);
    }
    public static void RateSeason(SerialInputData Series, int Nr, Double Grade) {
        Series.getSeasons().get(Nr - 1).setRating(Grade);
    }

    /**
     * Selecting the series that are from same year and have the same genre as the parameters
     * Also, selecting only those series that have been received at least one evaluation.
     */
    public static List<SerialInputData> SelectSeries(List<SerialInputData> series, String year, String genre, boolean Graded) {
        List<SerialInputData> SelectedSeries = new ArrayList<SerialInputData>();
        for (int i = 0; i < series.size(); i++) {
            SerialInputData show = series.get(i);
            if (Graded &&
                    show.getGenres().contains(genre) &&
                    year != null &&
                    show.getYear() == Integer.parseInt(year)) {
                SelectedSeries.add(show);
            }
        }

        return SelectedSeries;
    }

    /**
     * Sorting a list of series based on rating and the required order.
     */

    public static List<SerialInputData> SortSeries(List<SerialInputData> series, String order) {
        Collections.sort(series, Comparator.comparing(SerialInputData::getGrade));

        if (order.equals("desc")) {
            Collections.reverse(series);
        }

        return series;
    }

    /**
     * Building a list with names of first N Series from sorted list received as parameter.
     */
    public static ArrayList<String> getNames(List<SerialInputData> series, Integer N) {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < N && i < series.size(); i++) {
            names.add(series.get(i).getTitle());
        }

        return names;
    }

    public void CalculateNrappearances(List<SerialInputData> series, List<UserInputData> users) {
        for (int i = 0; i < series.size(); i++) {
            Integer Nrappearances = 0;
            SerialInputData serial = series.get(i);
            for (int j = 0; j < users.size(); j++) {
                if (users.get(j).getFavoriteMovies().contains(serial.getTitle())) {
                    Nrappearances++;
                }
            }
            if (Nrappearances != 0) {
                serial.UpdateNrappearances(Nrappearances);
            }
        }
    }

    public void SortByNrAppearances(List<SerialInputData> series, String order) {
        Collections.sort(series, Comparator.comparing(SerialInputData::getNrappearances));

        if (order.equals("desc")) {
            Collections.reverse(series);
        }
    }

    public static void CalculateDuration(List<SerialInputData> series) {
        for (int i = 0; i < series.size(); i++) {
            int duration = 0;
            ArrayList<Season> seasons = series.get(i).getSeasons();
            for (int j = 0; j < seasons.size(); j++) {
                duration += seasons.get(j).getDuration();
            }
            series.get(i).setDuration(duration);
        }
    }

    public static void SortByDuration(List<SerialInputData> series, String order) {
        Collections.sort(series, Comparator.comparing(SerialInputData::getDuration));

        if (order.equals("desc")) {
            Collections.reverse(series);
        }
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
}
