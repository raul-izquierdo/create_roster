package es.uniovi.eii.ds.cli;

import java.util.*;

public class CommandLine {
    public static Map<String, String> getCommandLineArguments(String[] args) {

        Map<String, String> map = new HashMap<>();
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("-o") && i + 1 < args.length)
                map.put("-o", args[++i]);
            else if (args[i].equals("-g") && i + 1 < args.length)
                map.put("-g", args[++i]);
            else if (args[i].endsWith(".xls"))
                map.put("xls", args[i]);
            else if (args[i].equals("-h") || args[i].equals("--help"))
                map.put("-h", "");
            i++;
        }

        if (!map.containsKey("xls"))
            map.put("xls", "alumnosMatriculados.xls");

        if (!map.containsKey("-o"))
            map.put("-o", "roster.txt");

        if (!map.containsKey("-g"))
            map.put("-g", "groups.csv");

        return map;
    }

    public static void printError(String message) {
        System.err.println("\n\n --> Error!!! " + message + "\n");
    }

    public static void printError(String message, Object... args) {
        printError(String.format(message, args));
    }

    public static void printHelp() {
        System.out.println(
                """
                        Usage: java -jar create_roster.jar [<sies.xls>] [-o <output_file>] [-g <groups_file>]

                        Options:
                          <sies.xls>        The Excel file from SIES with the students in the subject.
                                            (default = "alumnosMatriculados.xls").
                          -o <output.txt>   The output file for the generated roster.
                                            (default = "roster.txt").
                          -g <groups_file>  The file containing my groups. Only the students in these
                                            groups will be included in the roster. If not specified,
                                            all students will be included.
                                            (default = "groups.csv").
                          -h, --help        Show this help message.

                        """);

        printGroupsFormat();
    }

    public static void printGroupsFormat() {
        System.out.println(
                """
                        About the "groups.csv" file:
                        - It should contain the ids of the groups assigned to the teacher, one line per group.
                          Example:
                            01
                            02
                            i02
                            08
                        - It is recommended to use the same "groups.csv" as in "show_solutions".
                          Although that file is a csv file with more columns, only the first column will
                          be used (the group_id).
                        """);
    }

}
