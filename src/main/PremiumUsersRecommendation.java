package main;

import entertainment.Genre;
import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import javax.swing.text.View;
import java.util.*;

public class PremiumUsersRecommendation {
    public static String PopularRecommendation(ActionInputData request,
                                               List<MovieInputData> movies,
                                               List<UserInputData> users,
                                               List<SerialInputData> series) {
        String username = request.getUsername();
        UserInputData user = UserInputData.getUser(users, username);
        Map<String, Integer> ViewsTracker;
        LinkedHashMap<String, Integer> SortedViewsTracker;
        String title = null;

        if (user == null) {
            return null;
        }

        // Calculating views for each genre.
        ViewsTracker = UserInputData.TrackViews(users, movies, series);
        SortedViewsTracker = ActionInputData.SortMap(ViewsTracker);

        return MovieInputData.FindMostPopular(request, users, movies, series, SortedViewsTracker);
    }

    public static String FavoriteRecommendation(ActionInputData request,
                                               List<MovieInputData> movies,
                                               List<UserInputData> users,
                                               List<SerialInputData> series) {
        String username = request.getUsername();
        UserInputData user = UserInputData.getUser(users, username);
        Map<String, Integer> FavoriteTracker;
        String title = null;

        if (user == null) {
            return null;
        }

        FavoriteTracker = UserInputData.TrackFavorites(users, movies, series);
        LinkedHashMap<String, Integer> SortedFavoriteTracker = ActionInputData.SortMap(FavoriteTracker);

        // Selecting the video with most apparences in users's favorite lists.
        String MostFavoriteApparences = SortedFavoriteTracker.entrySet().iterator().next().getKey();
        if (SortedFavoriteTracker.get(MostFavoriteApparences) != 0) {
            title = MostFavoriteApparences;
        }

        return title;
    }
}
