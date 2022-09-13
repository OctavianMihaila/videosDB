package fileio;

import entertainment.Season;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a tv show, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    public SerialInputData(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
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
