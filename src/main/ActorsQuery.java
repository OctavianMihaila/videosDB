package main;

import fileio.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActorsQuery {
    public static ArrayList<String> CriteriaProcessing(ActionInputData request, List<ActorInputData> actors,
                                                       List<MovieInputData> movies, List<SerialInputData> series) {
        ArrayList<String> ActorsNames = new ArrayList<String>();
        List<ActorInputData> SortedActors;

        switch(request.getCriteria()) {
            case "average":
                Input.UpdateActorsRatings(actors, movies, series);
                SortedActors = ActorInputData.SortByRatingAverage(actors, request.getSortType());
                ActorsNames = ActorInputData.getFirstN(SortedActors, request.getNumber());
                break;

            case "awards":
                List<ActorInputData> AvailableActors =
                        ActorInputData.SelectAvailable(actors, request.getFilters().get(3));
                ActorInputData.CalculateNrAwards(AvailableActors);
                SortedActors = ActorInputData.SortByNrOfAwards(AvailableActors, request.getSortType());
                ActorsNames = ActorInputData.getNames(SortedActors);
                break;

            case "filter_description":
                List<ActorInputData> SelectedActors =
                        ActorInputData.SelectRequiredActors(actors, request.getFilters().get(2));
                ActorsNames = ActorInputData.ColectNames(SelectedActors);
                Collections.sort(ActorsNames);
                break;

            default:
                throw new IllegalArgumentException("Invalid request");

        }

        return ActorsNames;
    }
}
