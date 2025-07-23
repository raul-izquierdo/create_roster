package es.uniovi.raul.roster.xls;

import java.util.*;
import java.io.*;

import static org.apache.poi.ss.usermodel.WorkbookFactory.create;
import static java.lang.String.*;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.*;

public class ExcelReader {

    // Excel cell positions (0-based)
    private static final int HEADERS_ROW = 9; // Aquí deberían estar los encabezados (fila 10 en Excel)

    private static final int NAME_COLUMN = 2; // Aquí debería estar la columna "Alumno" - Column C
    private static final String NAME_HEADER = "Alumno";

    private static final int LABORATORY_COLUMN = 10; // Aquí debería estar la columna "Prácticas de Laboratorio" - Column K
    private static final String LABORATORY_HEADER = "Prácticas de Laboratorio";

    public static List<Student> readStudentsFromExcel(String filePath) throws IOException, ExcelContentException {
        List<Student> students = new ArrayList<>();

        try (Workbook workbook = create(new FileInputStream(filePath))) {

            Sheet sheet = workbook.getSheetAt(0);
            validateExcelFileFormat(sheet);

            // Data starts at row 11 (index 10)
            for (int rowIndex = HEADERS_ROW + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null)
                    continue;

                String name = getValue(row, NAME_COLUMN);
                if (name.isEmpty())
                    break; // No more students in this file

                String laboratory = getValue(row, LABORATORY_COLUMN);
                if (laboratory.isEmpty())
                    continue;

                try {
                    students.add(new Student(name, laboratory));
                } catch (IllegalArgumentException e) {
                    throw new ExcelContentException(format("Error in row %d. %s", rowIndex + 1, e.getMessage()));
                }
            }
        }

        return students;
    }

    private static String getValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        return (cell != null && cell.getCellType() == STRING) ? cell.getStringCellValue().trim() : "";
    }

    // Validate that "Alumno" is at C10 and "Prácticas de Laboratorio" is at K10
    private static void validateExcelFileFormat(Sheet sheet) throws ExcelContentException {
        Row headerRow = sheet.getRow(HEADERS_ROW); // Row 10 (0-based index)
        if (headerRow == null)
            throw new ExcelContentException("Header row 10 not found in the sheet.");

        Cell alumnoCell = headerRow.getCell(NAME_COLUMN); // Column C (0-based index)
        Cell laboratoryCell = headerRow.getCell(LABORATORY_COLUMN); // Column K (0-based index)

        String alumnoValue = (alumnoCell != null && alumnoCell.getCellType() == STRING)
                ? alumnoCell.getStringCellValue().trim()
                : "";
        String laboratoryValue = (laboratoryCell != null && laboratoryCell.getCellType() == STRING)
                ? laboratoryCell.getStringCellValue().trim()
                : "";

        if (!NAME_HEADER.equalsIgnoreCase(alumnoValue) || !LABORATORY_HEADER.equalsIgnoreCase(laboratoryValue)) {
            throw new ExcelContentException(format(
                    "Expected headers not found at C10 and K10. Expected: C10='%s', K10='%s'", NAME_HEADER,
                    LABORATORY_HEADER));
        }
    }
}
