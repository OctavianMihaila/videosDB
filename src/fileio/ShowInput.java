package fileio;

import java.util.ArrayList;

/**
 * General information about show (video), retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public abstract class ShowInput<T, R, S> {
    /**
     * Show's title
     */
    private final String title;
    /**
     * The year the show was released
     */
    private final int year;
    /**
     * Show casting
     */
    private final ArrayList<String> cast;
    /**
     * Show genres
     */
    private final ArrayList<String> genres;

    /**
     * Rating from users
     */
    private Double grade;

    /**
     * Counts the number of appearances in users favorite list for a video
     */
    private Integer Nrappearances;

    /**
     * Counts the number of views for each show
     */
    private Integer NrViews;

    public ShowInput(final String title, final int year,
                     final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.grade = Double.valueOf(0); // default value
        this.Nrappearances = 0; // default value
        this.NrViews = 0; // default value
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final Double getGrade() {
        return grade;
    }

    public final Integer getNrappearances() {
        return Nrappearances;
    }

    public final Integer getNrViews() {
        return NrViews;
    }
    public final void UpdateGrade(Double NewGrade) {
        this.grade = NewGrade;
    }

    public final void UpdateNrappearances(Integer NewNrappearances) {
        this.Nrappearances = NewNrappearances;
    }

    public final void UpdateNrViews(Integer NewNrViews) {
        this.NrViews = NewNrViews;
    }

    /**
     * Calculates number of appearances for a video in favorite lists of users.
     */
    public abstract void CalculateNrappearances(T t, R r);

    /**
     * Sorting a list of videos based on Nrappearances.
     */
    public abstract void SortByNrAppearances(T t, S s);
}
