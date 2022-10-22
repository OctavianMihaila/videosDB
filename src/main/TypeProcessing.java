package main;

import fileio.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeProcessing {
    public static JSONObject CommandProcessing(ActionInputData command, List<UserInputData> users,
                                               List<MovieInputData> movies, List<SerialInputData> series) {
        String type = command.getType();
        String username = command.getUsername();
        String title = command.getTitle();
        UserInputData user = Input.FindUser(users, username);
        Map<String, Integer> history = user.getHistory();
        JSONObject confirmation = new JSONObject();
        confirmation.put("id", command.getActionId());

        switch (type) {
            case "favorite":
                if (user.CheckSeen(history, title)
                        && !user.CheckFavoriteExistence(user.getFavoriteMovies(), title)) {
                    user.AddToFavorites(user.getFavoriteMovies(), title);
                    confirmation.put("message", "succes -> " + title + " was added as favourite");

                } else if (user.CheckSeen(history, title)) {
                    confirmation.put("message", "error -> " + title + " is already in favourite list");
                } else {
                    confirmation.put("message", "error -> " + title + " is not seen" );
                }
                break;

            case "view":
                Integer CurrentViews = user.View(history, title);
                confirmation.put("message", "success -> " + title + " was viewed with total views of " + CurrentViews);
                break;

            case "rating":
                if (user.CheckSeen(history, title)) {
                    if (command.getSeasonNumber() == 0) {
                        MovieInputData MovieToRate = MovieInputData.FindMovie(movies, title);
                        MovieInputData.IncrementNrRatings(MovieToRate);
                        MovieInputData.RecalculateGrade(MovieToRate, command.getGrade());
                        user.IncrementNrEvaluation(user);
                    } else {
                        SerialInputData SerialToRate = SerialInputData.FindSeries(series, title);
                        int SeasonNumber = command.getSeasonNumber();
                        SerialInputData.RateSeason(SerialToRate, SeasonNumber, command.getGrade());
                        SerialInputData.UpdateSeriesGrade(SerialToRate);
                        user.IncrementNrEvaluation(user);
                    }

                    confirmation.put("message", "success -> " + title + " was rated with " + command.getGrade() + " by " + username);
                }
                else {
                    confirmation.put("message", "error -> " + title + " is not seen");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid command");
        }

        return confirmation;
    }

    public static JSONObject QueryProcessing(ActionInputData request, List<ActorInputData> actors,
                                             List<MovieInputData> movies, List<SerialInputData> series,
                                             List<UserInputData> users) {
        JSONObject confirmation = new JSONObject();
        confirmation.put("id", request.getActionId());
        ArrayList<String> ActorsNames;

        switch(request.getObjectType()) {
            case "actors":
                ActorsNames = ActorsQuery.CriteriaProcessing(request, actors, movies, series);
                break;

            case "movies":
                ActorsNames = MoviesShowsQuery.CriteriaProcessingMovies(request, movies, users);
                break;

            case "shows":
                ActorsNames = MoviesShowsQuery.CriteriaProcessingShows(request, series, users);
                break;

            case "users":
                ActorsNames = UsersQuery.FindMostActiveUsers(request, users);
                break;

            default:
                throw new IllegalArgumentException("Invalid action type");
        }

        confirmation.put("message", "Query result: " + ActorsNames);
        return confirmation;

    }

    public static JSONObject RecommendationProcessing(ActionInputData request, List<ActorInputData> actors,
                                                      List<MovieInputData> movies, List<SerialInputData> series,
                                                      List<UserInputData> users) {
        JSONObject confirmation = new JSONObject();
        confirmation.put("id", request.getActionId());
        ArrayList<String> VideosTitles;
        String title;

        switch (request.getType()) {
            case "best_unseen":
                title = AllUsersRecommendation.BestUnseenRecommendation(request, movies, users);
                confirmation.put("message", "BestRatedUnseenRecommendation result: " + title);
                break;

            case "favorite":
                VideosTitles = null;
                break;

            case "popular":
                VideosTitles = null;
                break;

            case "search":
                VideosTitles = null;
                break;

            case "standard":
                title = AllUsersRecommendation.StandardRecommendation(request, movies, users);
                confirmation.put("message", "StandardRecommendation result: " + title);
                break;
        }

        return confirmation;
    }
}
