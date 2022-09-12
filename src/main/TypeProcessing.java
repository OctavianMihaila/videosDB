package main;

import fileio.ActionInputData;
import fileio.Input;
import fileio.UserInputData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class TypeProcessing {
    public static JSONObject CommandProcessing(ActionInputData command, List<UserInputData> users) {
        String type = command.getType();
        String username = command.getUsername();
        JSONObject confirmation = new JSONObject();
        UserInputData user;

        switch (type) {
            case "favorite":
                confirmation.put("ID", command.getActionId());

                user = Input.FindUser(users, username);
                Map<String, Integer> history = user.getHistory();
                if (user.CheckSeen(history, command.getTitle())
                        && !user.CheckFavoriteExistence(user.getFavoriteMovies(), command.getTitle())) {
                    user.AddToFavorites(user.getFavoriteMovies(), command.getTitle());
                    confirmation.put("message", "succes -> " + command.getTitle() + " was added as favourite");

                } else if (user.CheckSeen(history, command.getTitle())) {
                    confirmation.put("message", "error -> " + command.getTitle() + " is already in favourite list");
                } else {
                    confirmation.put("message", "error -> " + command.getTitle() + " is not seen" );
                }

                System.out.println("Favorite case:");
                System.out.println("User: " + username);

               /* for (int i = 0; i < user.getFavoriteMovies().size(); i++) {
                    String MovieName = user.getFavoriteMovies().get(i);
                    System.out.println(MovieName);
                } */

                System.out.println("-----------\n");

            case "view":

            case "rating":
        }

        return confirmation;
    }
}
