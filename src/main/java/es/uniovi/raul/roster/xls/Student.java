package es.uniovi.raul.roster.xls;

public final class Student {
    private static final String LAB_PREFIX = "Prácticas de Laboratorio-";
    private static final String ENGLISH_PREFIX = "Inglés-";

    private final String name;
    private String groupId;

    public Student(String name, String laboratory) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name must not be null or blank. Value: '" + name + "'");
        this.name = name;

        if (laboratory == null || !laboratory.startsWith(LAB_PREFIX))
            throw new IllegalArgumentException(
                    "Laboratory must start with '" + LAB_PREFIX + "'. Value: '" + laboratory + "'");

        this.groupId = laboratory.substring(LAB_PREFIX.length());
        if (this.groupId.startsWith(ENGLISH_PREFIX))
            this.groupId = "i" + this.groupId.substring(ENGLISH_PREFIX.length());

        if (this.groupId.isBlank())
            throw new IllegalArgumentException(
                    "Laboratory must have a group ID after '" + LAB_PREFIX + "'. Value: '" + this.groupId + "'");
    }

    public String getName() {
        return name;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getStudentId() {
        return getGroupId() + "-" + getName();
    }
}
