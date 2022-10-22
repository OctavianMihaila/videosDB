package main;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.List;

public class AllUsersRecommendation {

    /**
     * Returns the first unseen video from a movies list
     */
    public static String StandardRecommendation(ActionInputData request,
                                                List<MovieInputData> movies,
                                                List<UserInputData> users) {
        String username = request.getUsername();
        UserInputData user = UserInputData.getUser(users, username);

        if (user == null) {
            return null;
        }

        for (MovieInputData movie: movies) {
            if (user.CheckSeen(user.getHistory(), movie.getTitle())) {
                continue;
            }
            else {
                return movie.getTitle();
            }
        }
        return null;
    }

    /**
     * Returns the first unseen video from a movies list that is ordered by ratings
     */
    public static String BestUnseenRecommendation(ActionInputData request,
                                                List<MovieInputData> movies,
                                                List<UserInputData> users) {
        String username = request.getUsername();
        UserInputData user = UserInputData.getUser(users, username);

        if (user == null) {
            return null;
        }

        MovieInputData.SortMoviesByRating(movies, "asc");
        for (MovieInputData movie : movies) {
            if (user.CheckSeen(user.getHistory(), movie.getTitle())) {
                continue;
            } else {
                return movie.getTitle();
            }
        }
        return null;
    }
}
