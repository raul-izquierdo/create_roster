package es.uniovi.raul.roster.model;

public record Student(String name, String laboratory) {

    private static final String LAB_PREFIX = "Prácticas de Laboratorio-";
    private static final String ENGLISH_PREFIX = "Inglés-";

    public Student {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name must not be null or blank. Value: '" + name + "'");

        if (laboratory == null || !laboratory.startsWith(LAB_PREFIX))
            throw new IllegalArgumentException(
                    "Laboratory must start with '" + LAB_PREFIX + "'. Value: '" + laboratory + "'");
    }

    public String getGroupId() {

        String groupId = laboratory.substring(LAB_PREFIX.length());

        if (groupId.startsWith(ENGLISH_PREFIX))
            return "i" + groupId.substring(ENGLISH_PREFIX.length());

        return groupId;
    }

    public String getStudentId() {
        String groupId = getGroupId();
        return groupId.isEmpty() ? name : groupId + "-" + name;
    }
}
