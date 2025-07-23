package es.uniovi.raul.roster.groups;

public class NoGroupsFileFound extends Exception {
    public NoGroupsFileFound(String filePath) {
        super(String.format("\"%s\" not found", filePath));
    }
}
