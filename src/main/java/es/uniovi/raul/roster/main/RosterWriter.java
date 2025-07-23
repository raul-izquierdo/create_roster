package es.uniovi.raul.roster.main;

import java.io.*;
import java.util.List;

import es.uniovi.raul.roster.xls.Student;

public class RosterWriter {
    public static void writeRoster(List<Student> students, String outputFile) throws IOException {

        try (var writer = new FileWriter(outputFile)) {
            writeRoster(students, writer);
        }

    }

    public static void writeRoster(List<Student> students, Writer writer) throws IOException {

        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (var student : students)
                bufferedWriter.write(student.getStudentId() + System.lineSeparator());
        }
    }
}
