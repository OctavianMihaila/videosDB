package fileio;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;

    /**
     * Number of evaluations from users
     */
    private int NrRatings;

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.NrRatings = 0; // dafault value
    }

    public int getDuration() {
        return duration;
    }

    public static MovieInputData FindMovie(List<MovieInputData> movies, String title) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getTitle().equals(title)) {
                return movies.get(i);
            }
        }

        return null;
    }

    public static void IncrementNrRatings(MovieInputData movie) {
        movie.NrRatings++;
    }

    /**
     * Updating the grade every time the movie receives a new evaluation
     */
    public static void RecalculateGrade(MovieInputData movie, Double NewEvaluation) {
        Double OldGrade = movie.getGrade();
        Double NewGrade = ((movie.NrRatings - 1) * OldGrade + NewEvaluation) / movie.NrRatings;
        movie.UpdateGrade(NewGrade);
    }


    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }
}
