package es.uniovi.raul.roster.groups;

public class NoGroupsFileFound extends Exception {
    public NoGroupsFileFound(String filePath) {
        super("Groups file not found: \"" + filePath + "\"");
    }
}
