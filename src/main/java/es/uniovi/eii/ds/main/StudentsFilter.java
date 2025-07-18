package es.uniovi.eii.ds.main;

import es.uniovi.eii.ds.model.Student;

@FunctionalInterface
public interface StudentsFilter {
    boolean accept(Student student);
}
