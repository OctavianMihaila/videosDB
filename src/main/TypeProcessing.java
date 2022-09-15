package main;

import fileio.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
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
                break;

            default:
                throw new IllegalArgumentException("Invalid command");
        }

        return confirmation;
    }

    public static JSONObject QueryProcessing(ActionInputData request, List<ActorInputData> actors,
                                             List<MovieInputData> movies, List<SerialInputData> series) {
        JSONObject confirmation = new JSONObject();
        confirmation.put("id", request.getActionId());

        switch(request.getObjectType()) {
            case "actors":
                ArrayList<String> ActorsNames = ActorsQuery.CriteriaProcessing(request, actors, movies, series);
                confirmation.put("message", "Query result: " + ActorsNames);
                break;

            case "movies":
                break;

            case "shows":
                break;

            case "users":
                break;

            default:
                throw new IllegalArgumentException("Invalid action type");
        }

        return confirmation;

    }
}
