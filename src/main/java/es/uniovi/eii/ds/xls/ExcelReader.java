package es.uniovi.eii.ds.xls;

import java.util.*;
import java.io.*;
import es.uniovi.eii.ds.model.Student;
import static org.apache.poi.ss.usermodel.WorkbookFactory.create;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.*;

public class ExcelReader {

    public static final String HEADER_DNI = "DNI";
    public static final String HEADER_ALUMNO = "Alumno";
    public static final String HEADER_LABORATORY = "Prácticas de Laboratorio";
    public static final String LAB_PREFIX = "Prácticas de Laboratorio-";

    public static class ExcelFormatException extends Exception {
        public ExcelFormatException(String message) {
            super(message);
        }

        public ExcelFormatException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public record HeaderInfo(int rowIndex, int alumnoCol, int laboratoryCol) {
    }

    private static HeaderInfo findHeader(Sheet sheet) throws ExcelFormatException {
        for (Row row : sheet) {
            Integer dniCol = null, alumnoCol = null, laboratoryCol = null; // Reseted in each iteration
            for (Cell cell : row) {
                if (cell.getCellType() == STRING) {
                    String value = cell.getStringCellValue().trim();
                    if (value.equalsIgnoreCase(HEADER_DNI))
                        dniCol = cell.getColumnIndex();
                    if (value.equalsIgnoreCase(HEADER_ALUMNO))
                        alumnoCol = cell.getColumnIndex();
                    if (value.equalsIgnoreCase(HEADER_LABORATORY))
                        laboratoryCol = cell.getColumnIndex();
                }
            }

            // Only return if all headers are found in the same row
            if (dniCol != null && alumnoCol != null && laboratoryCol != null) {
                if (!(dniCol < alumnoCol && alumnoCol < laboratoryCol))
                    throw new ExcelFormatException(
                            "Las cabeceras no están en el orden esperado en la fila " + (row.getRowNum() + 1) + ": '"
                                    + HEADER_DNI + "', '" + HEADER_ALUMNO + "', '" + HEADER_LABORATORY + "'");

                return new HeaderInfo(row.getRowNum(), alumnoCol, laboratoryCol);
            }
        }
        throw new ExcelFormatException("No se encuentran las cabeceras en la misma fila: '" + HEADER_DNI + "', '"
                + HEADER_ALUMNO + "', '" + HEADER_LABORATORY + "'");
    }

    private static String getValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        return (cell != null && cell.getCellType() == STRING) ? cell.getStringCellValue().trim() : "";
    }

    public static List<Student> readStudentsFromExcel(String filePath) throws IOException, ExcelFormatException {
        List<Student> students = new ArrayList<>();

        try (Workbook workbook = create(new FileInputStream(filePath))) {

            Sheet sheet = workbook.getSheetAt(0);

            HeaderInfo header = findHeader(sheet);

            for (int rowIndex = header.rowIndex + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                Row row = sheet.getRow(rowIndex);
                if (row == null)
                    continue;

                String name = getValue(row, header.alumnoCol);
                if (name.isEmpty())
                    break;

                String laboratory = getValue(row, header.laboratoryCol);
                if (laboratory.isEmpty())
                    continue;
                if (!laboratory.startsWith(LAB_PREFIX))
                    throw new ExcelFormatException("El valor de la celda '" + HEADER_LABORATORY + "' ('" + laboratory
                            + "') no empieza por el prefijo esperado: '" + LAB_PREFIX + "'. Fila: " + (rowIndex + 1));

                students.add(new Student(name, laboratory));
            }
        }
        return students;
    }
}
