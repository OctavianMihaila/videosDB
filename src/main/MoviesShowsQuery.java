package main;

import fileio.*;

import java.util.ArrayList;
import java.util.List;

public class MoviesShowsQuery {
    public static ArrayList<String> CriteriaProcessingMovies(ActionInputData request,
                                                             List<MovieInputData> movies, List<UserInputData> users) {
        ArrayList<String> MovieNames = new ArrayList<String>();
        List<MovieInputData> SortedMovies;

        switch (request.getCriteria()) {
            case "ratings":
                // Select movies that verify genre, year and rating != 0
                // Sort ASC / DESC
                // Get title for first N of these movies.
                List<MovieInputData> SelectedMovies = MovieInputData.SelectMovies(
                        movies, request.getFilters().get(0).get(0), request.getFilters().get(1).get(0), false);
                SortedMovies = MovieInputData.SortMovies(SelectedMovies, request.getSortType());
                MovieNames = MovieInputData.getNames(SortedMovies, request.getNumber());

                // NOT TESTED but looks good

                break;

            case "favorite":
                if (movies.size() != 0) {
                    MovieInputData instance = movies.get(0);
                    movies = MovieInputData.SelectMovies(movies, request.getFilters().get(0).get(0),
                            request.getFilters().get(1).get(0), true);
                    System.out.println("-----------");
                    System.out.println(request.getActionId());
                    for (int i = 0; i < movies.size(); i++) {
                        System.out.println(movies.get(i).getTitle());
                    }
                    instance.CalculateNrappearances(movies, users);
                    instance.SortByNrAppearances(movies, request.getSortType());

                    MovieNames = MovieInputData.getNames(movies, request.getNumber());
                }

                // NOT TESTED
                break;

            case "longest":
                movies = MovieInputData.SelectMovies(movies, request.getFilters().get(0).get(0),
                        request.getFilters().get(1).get(0), true);
                MovieInputData.SortByDuration(movies, request.getSortType());
                MovieNames = MovieInputData.getNames(movies, request.getNumber());
                break;

            case "most_viewed":
                break;

            default:
                throw new IllegalArgumentException("Invalid request");
        }

        return MovieNames;
    }

    public static ArrayList<String> CriteriaProcessingShows(ActionInputData request,
                                                            List<SerialInputData> series, List<UserInputData> users) {
        ArrayList<String> SerialNames = new ArrayList<String>();
        List<SerialInputData> SortedSeries;

        switch (request.getCriteria()) {
            case "ratings":
                // Select movies that verify genre, year and rating != 0
                // Sort ASC / DESC
                // Get title for first N of these movies.
                List<SerialInputData> SelectedSeries = SerialInputData.SelectSeries(series,
                        request.getFilters().get(0).get(0), request.getFilters().get(1).get(0), false);
                SortedSeries = SerialInputData.SortSeries(SelectedSeries, request.getSortType());
                SerialNames = SerialInputData.getNames(SortedSeries, request.getNumber());
                // NOT TESTED but looks good

                break;

            case "favorite":
                if (series.size() != 0) {
                    SerialInputData instance = series.get(0);
                    series = SerialInputData.SelectSeries(series, request.getFilters().get(0).get(0),
                            request.getFilters().get(1).get(0), true);
                    instance.CalculateNrappearances(series, users);
                    instance.SortByNrAppearances(series, request.getSortType());
                    SerialNames = SerialInputData.getNames(series, request.getNumber());
                }
                // NOT TESTED

                break;

            case "longest":
                series = SerialInputData.SelectSeries(series, request.getFilters().get(0).get(0),
                        request.getFilters().get(1).get(0), true);
                SerialInputData.CalculateDuration(series);
                SerialInputData.SortByDuration(series, request.getSortType());
                SerialNames = SerialInputData.getNames(series, request.getNumber());
                break;

            case "most_viewed":
                break;

            default:
                throw new IllegalArgumentException("Invalid request");
        }

        return SerialNames;
    }

}
