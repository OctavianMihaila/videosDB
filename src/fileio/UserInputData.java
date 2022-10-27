package fileio;

import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
import entertainment.Genre;

import java.util.*;

/**
 * Information about an user, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class UserInputData {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;

    private Integer NrEvaluations;

    public UserInputData(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
        this.NrEvaluations = 0; // default value;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public Integer getNrEvaluations() {
        return NrEvaluations;
    }

    /**
     * Returns a user with a specific username.
     */
    public static UserInputData getUser(List<UserInputData> users, String username) {
        for (UserInputData user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public static ArrayList<String> getNames(List<UserInputData> users, Integer N) {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < N && i < users.size(); i++) {
            if (users.get(i).getNrEvaluations() != 0) {
                names.add(users.get(i).getUsername());
            }
        }

        return names;

    }

    public boolean CheckSeen(Map<String, Integer> history, String title) {
        Iterator<Map.Entry<String, Integer>> iterator = history.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            if (entry.getKey().equals(title)) {
                return true;
            }
        }

        return false;

    }

    public boolean CheckFavoriteExistence(ArrayList<String> favorites, String title) {
        for (int i = 0; i < favorites.size(); i++) {
            String ItemToCheck = favorites.get(i);
            if (ItemToCheck.equals(title)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a user has PREMIUM subscription
     */
    public String CheckInvalidUser(ActionInputData request, List<UserInputData> users) {
        UserInputData user = UserInputData.getUser(users, request.getUsername());
        if (user.getSubscriptionType().equals("BASIC")) {
            if (request.getType().equals("favorite")) {
                return "Favorite";
            }
            else if (request.getType().equals("popular")) {
                return  "Popular";
            }
            else if (request.getType().equals("search")) {
                return  "Search";
            }
        }

        return null;
    }

    public static void SortByNrEvaluation(List<UserInputData> users, String order) {
        Collections.sort(users, Comparator.comparing(UserInputData::getNrEvaluations));

        if (order.equals("desc")) {
            Collections.reverse(users);
        }
    }

    public void AddToFavorites(ArrayList<String> favorites, String title) {
        favorites.add(title);
    }

    public Integer View(Map<String, Integer> history, String title) {
        history.merge(title, 1, Integer::sum);
        return history.get(title);
    }

    /**
     * Generates a map with the total of views for every movie genre.
     */
    public static Map<String, Integer> TrackViews(List<UserInputData> users,
                                                  List<MovieInputData> movies,
                                                  List<SerialInputData> series) {
        Map<String, Integer> ViewsTracker = new LinkedHashMap<>();
        ArrayList<String> genres = null;
        String title = null;
        // For every user we search its history and add the
        // number of views to the genres that a film has.
        for (UserInputData user : users) {
            Map<String, Integer> history = user.getHistory();
            Iterator<Map.Entry<String, Integer>> iterator = history.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                // Looking for the video in movies list
                MovieInputData movie = movies.get(0).getVideo(entry.getKey(), movies);
                if (movie != null) {
                    genres = movie.getGenres();
                    title = movie.getTitle();
                    // Increment genres with the number of views for the current processed movie.
                    user.UpdateViewsTracker(genres, ViewsTracker, title);
                }
                else { // Looking for the video in shows list
                    SerialInputData show = series.get(0).getVideo(entry.getKey(), series);
                    genres = show.getGenres();
                    title = show.getTitle();
                    // Increment genres with the number of views for the current processed show.
                    user.UpdateViewsTracker(genres, ViewsTracker, title);
                }
            }
        }
        return ViewsTracker;
    }

    /**
     * Increments genres with the number of views for the current processed movie.
     */
    public void UpdateViewsTracker(ArrayList<String> genres, Map<String, Integer> ViewsTracker, String title) {
        for (String genre : genres) {
            for (Genre EnumGenres: Genre.values()) {
                if (genre.toLowerCase().equals(EnumGenres.name().toLowerCase())) {
                    if (ViewsTracker.containsKey(genre)) {
                        ViewsTracker.put(genre, ViewsTracker.get(genre)
                                + history.get(title));
                    } else {
                        ViewsTracker.put(genre, history.get(title));
                    }
                }
            }
        }
    }
    /**
     * Generates a map with the total number of favorite appearances
     * in users's favorite lists for every movie genre.
     */
    public static Map<String, Integer> TrackFavorites(List<UserInputData> users,
                                               List<MovieInputData> movies,
                                               List<SerialInputData> series) {

        Map<String, Integer> FavoritesTracker = new LinkedHashMap<>();
        ArrayList<String> favorites = null;

        // Mapping video names into ViewsTracker
        MovieInputData.MoviesMapping(FavoritesTracker, movies);
        SerialInputData.MoviesMapping(FavoritesTracker, series);

        // For every user we search its favorites list and add
        // increment in ViewsTracker for every video found.
        for (UserInputData user: users) {
            if (user.getSubscriptionType().equals("PREMIUM")) {
                favorites = user.getFavoriteMovies();
                for (String title : favorites) {
                    FavoritesTracker.put(title, FavoritesTracker.get(title) + 1);
                }
            }
        }

        return FavoritesTracker;

    }
    @Override
    public String toString() {
        return "UserInputData{" + "username='"
                + username + '\'' + ", subscriptionType='"
                + subscriptionType + '\'' + ", history="
                + history + ", favoriteMovies="
                + favoriteMovies + '}';
    }

    public void IncrementNrEvaluation(UserInputData user) {
        user.NrEvaluations++;
    }
}
