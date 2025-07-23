package es.uniovi.raul.roster.main;

import static es.uniovi.raul.roster.cli.CommandLine.*;
import static es.uniovi.raul.roster.main.GroupLoader.*;
import static es.uniovi.raul.roster.main.RosterWriter.*;

import java.util.*;

import es.uniovi.raul.roster.model.*;
import es.uniovi.raul.roster.xls.ExcelReader;

public class Main {
    public static void main(String[] args) {
        Map<String, String> cliArguments = getCommandLineArguments(args);
        if (cliArguments.containsKey("-h")) {
            printHelp();
            return;
        }

        String xlsFile = cliArguments.get("xls");
        String outputFile = cliArguments.get("-o");
        String groupsFile = cliArguments.get("-g");

        List<Student> students;
        try {
            students = ExcelReader.readStudentsFromExcel(xlsFile);
        } catch (Exception e) {
            printError("Error leyendo el fichero de alumnos: " + e.getMessage());
            return;
        }

        List<String> allowedGroups;
        try {
            allowedGroups = loadGroups(groupsFile);
        } catch (Exception e) {
            printError("Error leyendo el fichero de grupos: " + e.getMessage());
            return;
        }

        var filter = createFilter(allowedGroups);
        try {
            writeRoster(students, filter, outputFile);
            System.out.println("\nRoster generado en: " + outputFile);
        } catch (Exception e) {
            printError("Error escribiendo el fichero de salida: " + e.getMessage());
        }
    }

    static StudentsFilter createFilter(List<String> allowedGroups) {
        if (allowedGroups == null || allowedGroups.isEmpty())
            return student -> true;
        else
            return student -> allowedGroups.contains(student.getGroupId());
    }

}
