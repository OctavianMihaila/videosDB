package main;
import checker.Checkstyle;
import checker.Checker;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Constants;
import fileio.*;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        //Writer fileWriter = new Writer(filePath2);
        // Creating a default JSON obj that will be updated later for every action.
        JSONArray arrayResult = new JSONArray();

        // ENTRY POINT to a PROCESS CLASS

        //System.out.println("TEST");

        List<ActionInputData> commands = input.getCommands();
        List<UserInputData> users = input.getUsers();

        for (int i = 0 ; i < commands.size(); i++) {

            ActionInputData command = commands.get(i);

            switch (command.getActionType()) {
                case "command":
                JSONObject NewConfirmation = TypeProcessing.CommandProcessing(command, users);
                arrayResult.add(NewConfirmation);
                case "query":
                    // to do

                case "recommendation":
                    // to do

                default:
                    // Some error print
            }
            //System.out.println(command.getActionId());

        }

        try (FileWriter file = new FileWriter(filePath2)) {
            file.write(arrayResult.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO add here the entry point to your implementation

        //fileWriter.closeJSON(arrayResult);
    }
}
