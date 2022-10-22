package fileio;

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

    public Integer getNrEvaluations() { return NrEvaluations; }

    /**
     * Returns a user with a specific username.
     */
    public static UserInputData getUser(List<UserInputData> users, String username) {
        for (UserInputData user: users) {
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
    public boolean CheckSeen (Map<String, Integer> history, String title) {
        Iterator<Map.Entry<String, Integer>> iterator = history.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            if (entry.getKey().equals(title)) {
                return true;
            }
        }

        return false;

    }

    public boolean CheckFavoriteExistence (ArrayList<String> favorites, String title) {
        for (int i = 0; i < favorites.size(); i++) {
            String ItemToCheck = favorites.get(i);
            if (ItemToCheck.equals(title)) {
                return true;
            }
        }

        return false;
    }

    public static void SortByNrEvaluation(List<UserInputData> users, String order) {
        Collections.sort(users, Comparator.comparing(UserInputData::getNrEvaluations));

        if (order.equals("desc")) {
            Collections.reverse(users);
        }
    }

    public void AddToFavorites (ArrayList<String> favorites, String title) {
        favorites.add(title);
    }

    public Integer View (Map<String, Integer> history, String title) {
        history.merge(title, 1, Integer::sum);
        return history.get(title);
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
