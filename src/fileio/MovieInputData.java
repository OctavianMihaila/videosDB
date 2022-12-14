package fileio;

import java.util.*;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput<List<MovieInputData>, List<UserInputData>,
                                                    String, MovieInputData, UserInputData> {
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

    /**
     * Searching for a movie with a specific title.
     */
    public MovieInputData getMovie(String title, List<MovieInputData> movies) {
        for (MovieInputData movie: movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }

        return null;
    }
    @Override
    public MovieInputData getVideo(String title, List<MovieInputData> movies) {
        for (MovieInputData movie: movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }

        return null;
    }

    /**
     * Checking if a movie has genre in its list of genres
     */
    public boolean CheckGenre(MovieInputData movie, String genre) {
        ArrayList<String> MovieGenres = movie.getGenres();
        for (String MovieGenre: MovieGenres) {
            if (MovieGenre.equals(genre)) {
                return true;
            }
        }

        return false;
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

    /**
     * Selecting the movies that are from same year and have the same genre as the parameters
     * Also, selecting only those movies that have been received at least one evaluation.
     */
    public static List<MovieInputData> SelectMovies(List<MovieInputData> movies, String year, String genre, boolean Graded) {
        List<MovieInputData> SelectedMoviesGraded = new ArrayList<MovieInputData>();
        List<MovieInputData> SelectedMoviesNotGraded = new ArrayList<MovieInputData>();

        for (int i = 0; i < movies.size(); i++) {
            MovieInputData movie = movies.get(i);
            if (Graded && year != null &&
            movie.getGenres().contains(genre) &&
            movie.getYear() == Integer.parseInt(year)) {
                SelectedMoviesGraded.add(movie);
            }
            if (year != null && movie.getGenres().contains(genre) &&
                    movie.getYear() == Integer.parseInt(year)) {
                SelectedMoviesNotGraded.add(movie);

            }
        }
        if (Graded) {
            return SelectedMoviesGraded;
        }
        else {
            return SelectedMoviesNotGraded;
        }
    }

    /**
     * Sorting a list of movies based on rating and the required order.
     */
    public static List<MovieInputData> SortMoviesByRating(List<MovieInputData> movies, String order) {
        Collections.sort(movies, Comparator.comparing(MovieInputData::getGrade));

        if (order.equals("desc")) {
            Collections.reverse(movies);
        }

        return movies;
    }

    /**
     * Building a list with names of first N Movie from sorted list received as parameter.
     * views -> Corner case: don't want to select movies with 0 views for most viewd query.
     */
    public static ArrayList<String> getNames(List<MovieInputData> movies, Integer N, boolean views) {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < N && i < movies.size() ; i++) {
            if (views && movies.get(i).getNrViews() == 0) {
                continue;
            }
            else { // Possible problem, check if NrApperances == 0.
                names.add(movies.get(i).getTitle());
            }
        }

        return names;
    }
    @Override
    public void CalculateNrappearances(List<MovieInputData> movies, List<UserInputData> users) {
        for (int i = 0; i < movies.size(); i++) {
            Integer Nrappearances = 0;
            MovieInputData movie = movies.get(i);
            for (int j = 0; j < users.size(); j++) {
                if (users.get(j).getFavoriteMovies().contains(movie.getTitle())) {
                    Nrappearances++;
                }
            }
            if (Nrappearances != 0) {
                movie.UpdateNrappearances(Nrappearances);
            }
        }
    }

    /**
     * Calculating total number of views for a movie list.
     */
    public static void CalculateViews(List<MovieInputData> movies, List<UserInputData> users) {
        for (int i = 0; i < movies.size(); i++) {
            Integer NrViews = 0;
            MovieInputData movie = movies.get(i);
            for (int j = 0; j < users.size(); j++) {
                Map<String, Integer> history = users.get(j).getHistory();
                for (Map.Entry<String, Integer> entry : history.entrySet()) {
                    if (entry.getKey().equals(movie.getTitle())) {
                        NrViews += entry.getValue();
                    }
                }
            }
            movie.UpdateNrViews(NrViews);
        }
    }
    @Override
    public void SortByNrAppearances(List<MovieInputData> movies, String order) {
        Collections.sort(movies, Comparator.comparing(MovieInputData::getNrappearances));

        if (order.equals("desc")) {
            Collections.reverse(movies);
        }
    }

    public static void SortByDuration(List<MovieInputData> movies, String order) {
        Collections.sort(movies, Comparator.comparing(MovieInputData::getDuration));

        if (order.equals("desc")) {
            Collections.reverse(movies);
        }
    }

    public static void SortByNrviews(List<MovieInputData> movies, String order) {
        Collections.sort(movies, Comparator.comparing(MovieInputData::getNrViews));

        if (order.equals("desc")) {
            Collections.reverse(movies);
        }
    }

    /**
     * Looking for the first movie that has the Popular genre
     * but does not appear in the user's history.
     */
    @Override
    public String FindFirstUnseen(List<MovieInputData> movies, UserInputData user, String PopularGenre) {
        String title = null;
        for (MovieInputData movie: movies) {
            if (movie.CheckGenre(movie, PopularGenre)) {
                if (user.CheckSeen(user.getHistory(), movie.getTitle())) {
                    continue;
                }
                else if (movie != null) {
                    title = movie.getTitle();
                    return title;
                }
            }
        }

        return title;
    }

    /**
     * Returns the first unseen movie/show from the most popular genre.
     * This is done recurisively by checking every genre from SortedViewsTracker
     * If cannot find a movie/show from the most popular genre it goes on to the 2nd most popular.
     */
    public static String FindMostPopular(ActionInputData request, List<UserInputData> users,
                                         List<MovieInputData> movies, List<SerialInputData> shows,
                                         LinkedHashMap<String, Integer> SortedViewsTracker) {
        String title = null;
        String username = request.getUsername();
        UserInputData user = UserInputData.getUser(users, username);

        if (SortedViewsTracker.isEmpty()) {
            return null;
        }
        String PopularGenre = SortedViewsTracker.entrySet().iterator().next().getKey();

        /* Looking for a movie/show that has the Popular
        genre but does not appear in the user's history.*/
        title = movies.get(0).FindFirstUnseen(movies, user, PopularGenre);
        if (title == null) { // if cannot find in movies then search through shows.
            title = shows.get(0).FindFirstUnseen(shows, user, PopularGenre);
        }
        if (title != null) {
            return title;
        }
        SortedViewsTracker.remove(PopularGenre);
        title = FindMostPopular(request, users, movies, shows, SortedViewsTracker);

        return title;
    }

    /**
     * Mapping movies into FavoriteTracker with default value 0.
     */
    public static void MoviesMapping(Map<String, Integer> FavoriteTracker,
                                     List<MovieInputData> movies) {
        for (MovieInputData movie: movies) {
            FavoriteTracker.put(movie.getTitle(), 0);
        }
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
