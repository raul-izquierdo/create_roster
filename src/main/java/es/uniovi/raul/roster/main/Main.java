package es.uniovi.raul.roster.main;

import static es.uniovi.raul.roster.cli.CommandLine.*;
import static es.uniovi.raul.roster.groups.GroupLoader.*;
import static es.uniovi.raul.roster.main.RosterWriter.*;

import java.io.IOException;
import java.util.*;

import es.uniovi.raul.roster.groups.NoGroupsFileFound;
import es.uniovi.raul.roster.xls.*;

public class Main {
    public static void main(String[] args) {

        Map<String, String> cliArguments = getCommandLineArguments(args);
        if (cliArguments.containsKey("-h")) {
            printHelp();
            return;
        }

        String xlsFile = cliArguments.get(EXCEL_FILE);
        List<Student> students;
        try {
            students = ExcelReader.readStudentsFromExcel(xlsFile);
        } catch (Exception e) {
            printError("Error reading the Excel file: " + e.getMessage());
            return;
        }

        String groupsFile = cliArguments.get(GROUP_FILE_FLAG);
        try {
            var allowedGroupsIds = loadGroupsIds(groupsFile);
            students = students.stream()
                    .filter(student -> allowedGroupsIds.contains(student.getGroupId()))
                    .toList();

        } catch (IOException e) {
            printError("Error reading the groups file: " + e.getMessage());
            return;
        } catch (NoGroupsFileFound e) {
            System.out.println(e.getMessage());
            printGroupsFormat();
            System.out.println(
                    "Continuing without group restrictions, so all students will be included in the output.");
        }

        String outputFile = cliArguments.get(OUTPUT_FILE_FLAG);
        try {
            writeRoster(students, outputFile);
            System.out.println("\nRoster generated at: " + outputFile);
            printCredits();
        } catch (Exception e) {
            printError("Error writing the output file: " + e.getMessage());
        }
    }

}
