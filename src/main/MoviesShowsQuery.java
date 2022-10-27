package main;

import fileio.*;

import java.util.ArrayList;
import java.util.List;

public class MoviesShowsQuery {
    public static ArrayList<String> CriteriaProcessingMovies(ActionInputData request,
                                                             List<MovieInputData> movies,
                                                             List<UserInputData> users) {
        ArrayList<String> MovieNames = new ArrayList<String>();
        List<MovieInputData> SortedMovies;
        List<MovieInputData> SelectedMovies;


        switch (request.getCriteria()) {
            case "ratings":
                // Select movies that verify genre, year and rating != 0
                // Sort ASC / DESC
                // Get title for first N of these movies.
                SelectedMovies = MovieInputData.SelectMovies(
                        movies, request.getFilters().get(0).get(0), request.getFilters().get(1).get(0), false);
                SortedMovies = MovieInputData.SortMoviesByRating(SelectedMovies, request.getSortType());
                MovieNames = MovieInputData.getNames(SortedMovies, request.getNumber(), false);

                // NOT TESTED but looks good

                break;

            case "favorite":
                if (movies.size() != 0) {
                    MovieInputData instance = movies.get(0);
                    movies = MovieInputData.SelectMovies(movies, request.getFilters().get(0).get(0),
                            request.getFilters().get(1).get(0), true);
                    instance.CalculateNrappearances(movies, users);
                    instance.SortByNrAppearances(movies, request.getSortType());
                    MovieNames = MovieInputData.getNames(movies, request.getNumber(), false);
                }

                // NOT TESTED
                break;

            case "longest":
                movies = MovieInputData.SelectMovies(movies, request.getFilters().get(0).get(0),
                        request.getFilters().get(1).get(0), true);
                MovieInputData.SortByDuration(movies, request.getSortType());
                MovieNames = MovieInputData.getNames(movies, request.getNumber(), false);
                break;

            case "most_viewed":
                SelectedMovies = MovieInputData.SelectMovies(movies, request.getFilters().get(0).get(0),
                        request.getFilters().get(1).get(0), false);

                MovieInputData.CalculateViews(SelectedMovies, users);
                MovieInputData.SortByNrviews(SelectedMovies, request.getSortType());
                MovieNames = MovieInputData.getNames(SelectedMovies, request.getNumber(), true);
                break;

            default:
                throw new IllegalArgumentException("Invalid request");
        }

        return MovieNames;
    }

    public static ArrayList<String> CriteriaProcessingShows(ActionInputData request,
                                                            List<SerialInputData> series,
                                                            List<UserInputData> users) {
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
                SerialNames = SerialInputData.getNames(SortedSeries, request.getNumber(), false);
                // NOT TESTED but looks good

                break;

            case "favorite":
                if (series.size() != 0) {
                    SerialInputData instance = series.get(0);
                    series = SerialInputData.SelectSeries(series, request.getFilters().get(0).get(0),
                            request.getFilters().get(1).get(0), true);
                    instance.CalculateNrappearances(series, users);
                    instance.SortByNrAppearances(series, request.getSortType());
                    SerialNames = SerialInputData.getNames(series, request.getNumber(), false);
                }
                // NOT TESTED

                break;

            case "longest":
                series = SerialInputData.SelectSeries(series, request.getFilters().get(0).get(0),
                        request.getFilters().get(1).get(0), true);
                SerialInputData.CalculateDuration(series);
                SerialInputData.SortByDuration(series, request.getSortType());
                SerialNames = SerialInputData.getNames(series, request.getNumber(), false);
                break;

            case "most_viewed":
                SelectedSeries = SerialInputData.SelectSeries(series,
                        request.getFilters().get(0).get(0), request.getFilters().get(1).get(0), false);
                SerialInputData.CalculateViews(SelectedSeries, users);
                SerialInputData.SortByNrViews(SelectedSeries, request.getSortType());
                SerialNames = SerialInputData.getNames(SelectedSeries, request.getNumber(), true);
                break;

            default:
                throw new IllegalArgumentException("Invalid request");
        }

        return SerialNames;
    }

}
