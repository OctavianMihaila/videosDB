package main;

import fileio.ActionInputData;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.List;

public class UsersQuery {
    public static ArrayList<String> FindMostActiveUsers(ActionInputData request, List<UserInputData> users) {
        UserInputData.SortByNrEvaluation(users, request.getSortType());
        return UserInputData.getNames(users, request.getNumber());

    }
}
