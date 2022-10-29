package main;

import entertainment.Genre;
import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import javax.swing.text.View;
import java.util.*;
import java.util.stream.Collectors;

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

    public static ArrayList<String> SearchRecommendation(ActionInputData request,
                                              List<MovieInputData> movies,
                                              List<UserInputData> users,
                                              List<SerialInputData> series) {
        String username = request.getUsername();
        UserInputData user = UserInputData.getUser(users, username);
        String genre = request.getGenre();
        Map<String, Double> Ratings = new HashMap<String, Double>();
        HashMap SortedRatings;
        List<Object> videos;
        ArrayList<String> titles = null;

        if (user == null) {
            return null;
        }

        // Selecting all unseen videos(movies/shows) that are from a specific genre.
        videos = user.SearchUnseen(movies, series, user, request.getGenre());
        // Put the objects in a map that stores their (name, rating) info.
        for (Object video: videos) {
            if (video instanceof MovieInputData) {
                Ratings.put(((MovieInputData) video).getTitle(), ((MovieInputData) video).getGrade());
            }
            else if (video instanceof SerialInputData) {
                Ratings.put(((SerialInputData) video).getTitle(), ((SerialInputData) video).getGrade());
            }
        }
        // Sort: 1st criteria rating, 2nd criteria name.
        Ratings = SortUtils.SortByValue(Ratings);
        titles = SortUtils.SortbyKey(Ratings);

        return titles;
    }
}
