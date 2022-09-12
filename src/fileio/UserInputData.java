package fileio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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

    public UserInputData(final String username, final String subscriptionType,
                         final Map<String, Integer> history,
                         final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteMovies = favoriteMovies;
        this.history = history;
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

    public void AddToFavorites (ArrayList<String> favorites, String title) {
        favorites.add(title);
    }
    @Override
    public String toString() {
        return "UserInputData{" + "username='"
                + username + '\'' + ", subscriptionType='"
                + subscriptionType + '\'' + ", history="
                + history + ", favoriteMovies="
                + favoriteMovies + '}';
    }
}
