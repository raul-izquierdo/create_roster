package es.uniovi.eii.ds.main;

import es.uniovi.eii.ds.model.*;
import java.util.*;
import java.io.*;

public class RosterWriter {
    public static void writeRoster(List<Student> students, StudentsFilter filter, String outputFile)
            throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (var student : students)
                if (filter.accept(student))
                    writer.write(student.getStudentId() + System.lineSeparator());
        }
    }
}
