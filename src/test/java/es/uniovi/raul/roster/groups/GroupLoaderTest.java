package es.uniovi.raul.roster.groups;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.List;

import org.junit.jupiter.api.Test;

class GroupLoaderTest {

    @Test
    void testLoadGroupsIds_emptyFile() throws Exception {
        List<String> groups = GroupLoader.loadGroupsIds(new StringReader(""));
        assertTrue(groups.isEmpty());
    }

    @Test
    void testLoadGroupsIds_onlyBlankLines() throws Exception {
        List<String> groups = GroupLoader.loadGroupsIds(new StringReader("\n   \n\t\n"));
        assertTrue(groups.isEmpty());
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource({
            "'  A1  \n B2\nC3   \n', 'A1;B2;C3'",
            "'A1,foo,bar\nB2 , something\nC3\n', 'A1;B2;C3'",
            "'A1\n\nB2\n   \nC3\n', 'A1;B2;C3'"
    })
    void testLoadGroupsIds_variousInputs(String input, String expected) throws Exception {
        List<String> groups = GroupLoader.loadGroupsIds(new StringReader(input));
        List<String> expectedList = java.util.Arrays.asList(expected.split(";"));
        assertEquals(expectedList, groups);
    }

    @Test
    void testLoadGroupsIds_fileNotFound() {
        assertThrows(NoGroupsFileFound.class, () -> GroupLoader.loadGroupsIds("nonexistent.txt"));
    }

    @Test
    void testLoadGroupsIds_csvFormat() throws Exception {
        List<String> groups = GroupLoader.loadGroupsIds(new StringReader("A1,Extra\nB2,Other\n"));
        assertEquals(List.of("A1", "B2"), groups);
    }
}
