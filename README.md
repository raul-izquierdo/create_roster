# Roster

## Objective

This tool generates a roster file for GitHub Classroom assignments by processing an Excel file exported from SIES (Universidad de Oviedo) and, optionally, a groups file.

The content of the generated file is the roster that must be copy and pasted in the Github Classroom roster field.

## Usage

Run the program using the following command:

```
java -jar roster.jar <sies.xls> [-g <groups_file>] [-o <output_file>]
```

- `<sies.xls>`: Excel file exported from SIES with student data.
- `-g <groups_file>` (optional, default: all groups): CSV or TXT file listing the group IDs of the teacher (one per line). If not provided, all students are included.
- `-o <output_file>` (optional, default: roster.txt): Output file name. Defaults to `roster.txt` if not specified.

Example:
```
java -jar roster.jar alumnosMatriculados.xls -g groups.csv -o roster.txt
```

## Input Files Format


### SIES Excel File (`alumnosMatriculados.xls`)
- The file must have the header `Alumno` in cell C10 and `Prácticas de Laboratorio` in cell K10.
- Student data starts at row 11 (Excel row 11, zero-based index 10).
- Each row must have:
  - Column C: Student name
  - Column K: Laboratory group (must start with `Prácticas de Laboratorio-`)


|   | A      | B      | C       | ... | K                           |
|---|--------|--------|---------|-----|-----------------------------|
|10 |        |        | Alumno  | ... | Prácticas de Laboratorio    |
|11 |        |        | Ana Gil | ... | Prácticas de Laboratorio-01 |
|12 |        |        | Luis Paz| ... | Prácticas de Laboratorio-02 |
|13 |        |        |         | ... |                             |



### Groups File (`groups.csv` or `groups.txt`)
- Plain text file, one group ID per line.
- Blank lines are ignored.

```
01
02
i01
```

## Output

The output file (default: `roster.txt`) contains one line per student in the format:

```
<groupId>-<studentName>
```


Example:
```
01-Anacleto Gil
02-Luis Paz
01-Mario Anabel Soto
i01-Jesus Patricia Menendez Soto
```

Only students belonging to the allowed groups (if a groups file is provided) are included. Otherwise, all students are listed.

## License

MIT License

Copyright (c) 2025 Raul Izquierdo Castanedo
