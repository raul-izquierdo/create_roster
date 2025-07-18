package es.uniovi.eii.ds.main;

import static es.uniovi.eii.ds.cli.CommandLine.getCommandLineArguments;
import static es.uniovi.eii.ds.cli.CommandLine.printHelp;
import static es.uniovi.eii.ds.cli.CommandLine.printError;
import java.util.*;
import static es.uniovi.eii.ds.main.GroupLoader.loadGroups;
import static es.uniovi.eii.ds.main.RosterWriter.writeRoster;
import es.uniovi.eii.ds.model.*;
import es.uniovi.eii.ds.xls.ExcelReader;

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
