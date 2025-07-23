package es.uniovi.raul.roster.groups;

import java.io.*;
import java.util.*;

public class GroupLoader {
    public static List<String> loadGroupsIds(String filePath) throws IOException, NoGroupsFileFound {

        File file = new File(filePath);
        if (!file.exists())
            throw new NoGroupsFileFound(filePath);

        try (FileReader reader = new FileReader(file)) {
            return loadGroupsIds(reader);
        }
    }

    private static List<String> loadGroupsIds(FileReader reader) throws IOException {

        List<String> groups = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.isBlank())
                    continue;

                String groupId = line.split(",")[0].trim(); // Assuming the first column contains the group ID
                groups.add(groupId);
            }
        }
        return groups;
    }
}
