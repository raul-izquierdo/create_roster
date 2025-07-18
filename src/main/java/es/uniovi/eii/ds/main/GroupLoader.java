package es.uniovi.eii.ds.main;

import java.util.*;
import java.io.*;
import es.uniovi.eii.ds.cli.CommandLine;

public class GroupLoader {
    public static List<String> loadGroups(String filePath) throws IOException {

        List<String> groups = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.printf("%nGroups file not found: \"%s\"%n", filePath);
            CommandLine.printGroupsFormat();
            System.out
                    .println("Continuing without group restrictions, so all students will be included in the output.");
            return groups;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String groupId = line.split(",")[0].trim();
                    groups.add(groupId);
                }
            }
        }

        return groups;
    }
}
