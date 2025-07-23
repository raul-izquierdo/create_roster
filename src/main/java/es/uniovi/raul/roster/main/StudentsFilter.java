package es.uniovi.raul.roster.main;

import es.uniovi.raul.roster.model.Student;

@FunctionalInterface
public interface StudentsFilter {
    boolean accept(Student student);
}
