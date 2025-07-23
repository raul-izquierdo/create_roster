# Requirements

## Objetivo

Este programa lee un excel generado por SIES (el sistema de información de estudiantes de la universidad de Oviedo) que contiene todos los alumnos de una asignatura y genera un fichero de texto que contendrá el student_id generado para cada alumno. Este fichero, que contendrá únicamente un student_id por línea, se usará como roster de una Github Classroom.

Opcionalmente, puede indicarse un fichero con los grupos del profesor, de tal manera que sólo se generen los student_id de los alumnos que estén en esos grupos.

## Uso

La forma de ejecutar el programa es la siguiente:

```bash
Usage: java -jar roster.jar [<sies.xls>] [-o <output_file>] [-g <groups_file>]

Options:
    <sies.xls>        The Excel file from SIES with the students in the subject.
                        (default = "alumnosMatriculados.xls").
    -o <output.txt>   The output file for the generated roster.
                        (default = "roster.txt").
    -g <groups_file>  The file containing my groups. Only the students in these
                        groups will be included in the roster. If not specified,
                        all students will be included.
                        (default = "groups.csv").
    -h, --help        Show this help message.

About the groups csv file:
- It should contain the ids of the groups of the teacher, one line per group.
    Example:
    01
    02
    i02
    08
- It is recommended to use the same "groups.csv" as in "show_solutions".
    Although that file is a csv file with more columns, only the first column will
    be used (the group_id).
```

## Inputs

### Fichero Excel

El formato está determinado por SIES.

### Fichero de Grupos

En su versión más simple, debe contener únicamente los identificadores de los grupos del profesor, uno por línea. Por ejemplo:

```txt
01
02
i02
08
```

Sin embargo, se puede reutilizar el fichero `groups.csv` que tambien se usa como input en el programa `show_solutions`. En este caso, aunque el fichero sea un csv con más columnas, sólo se usará la primera.

Por ejemplo:
```csv
01, monday, 12:00
02, tuesday, 14:00
i02, wednesday, 16:00
08, wednesday, 12:00
```

Nótese que el fichero `groups.csv` nunca tendrá línea de cabecera, por lo que la primera línea siempre será ya un grupo.

## Output

## roster.txt

Simplemente tendrá un "student_id" por línea.

```txt
01-Izquierdo Raúl
02-Gonzalez Manolo
i02-Perez Juan
08-Sanchez Ana
```
El student_id se genera concatenando el grupo de prácticas de laboratorio y el nombre del alumno, separados por un guión.

## Pasos para implementar el programa

### Implementar el método `readStudentsFromExcel` de la clase `ExcelReader`

Debe:
- Abrir el fichero Excel indicado.
- Buscar las columnas donde están el nombre y el laboratorio de cada alumno. Se deben buscar los valores "Alumno" y "Prácticas de Laboratorio".
- Una vez localizadas las celdas de ambas cabeceras, se asegurará que el formato del fichero no ha cambiado verificando:
    - Que las dos celdas anteriores existen y se encuentran en la misma fila.
    - Que la cabecera a la izquierda de "Alumno" se llama "DNI"
    - Si alguna de estas condiciones falla, el programa debe lanzar una excepción indicando que el formato del fichero no es correcto.
    - Antes de lanzar la excepción, se debe indicar por qué el formato no es correcto:
        - No se encuentra las cabeceras.
        - No están en el misma linea
        - No se ha encontrado la cabecera "DNI" a la izquierda de "Alumno".
- Leer todas las filas de alumnos. Empiezan en la fila siguiente a la de las cabeceras y finaliza cuando se encuentre una fila vacía en la columna "Alumno".
- Para cada fila, se debe crear un objeto `Student` con el nombre y el laboratorio del alumno. El nombre del alumno es el valor de la celda de la columna "Alumno" y el laboratorio es el valor de la celda de la columna "Prácticas de Laboratorio". Si la celda de "Prácticas de Laboratorio" está vacía,
  lo cual es un caso válido, simplemente se ignora este alumno y no se crea el objeto `Student`. Este método devolverá una lista con los estudiantes que tienen valor en ambas columnas.
- Finalmente, devolverá una lista con todos los objetos `Student` creados.

El método, por tanto, devolverá:
- Si el fichero no se ha encontrado o ha habido un error de I/O, lanzará una excepción (sugerir cual).
- Si el fichero se puede leer, pero no tiene el formato correcto, lanzará una excepción indicando que el formato del fichero no es correcto.
- Si el fichero se puede leer y tiene el formato correcto, devolverá una lista con los objetos `Student` creados (los que tienen valor en ambas columnas).


### Record Student

#### Añadir un método `getGroupId` al record `Student`

El `laboratory` del alumno puede tener dos formatos al ser extraido del Excel:
- Prácticas de Laboratorio-01
- Prácticas de Laboratorio-Inglés-01

Es decir, siempre tiene el prefijo "Prácticas de Laboratorio-". Luego, puede tener, para los grupos de inglés, el sufijo adicional "-Inglés".

El método `getGroupId` debe devolver:
- Si es un grupo en inglés, poner el sufijo "i" delante del número del grupo separado por un guión.
- Si es un grupo en español, simplemente dejar el número del grupo.

- Prácticas de Laboratorio-01 -> 01
- Prácticas de Laboratorio-Inglés-01 -> i01


#### Añadir un método `getStudentId` al record `Student`

El método `getStudentId` debe devolver el identificador del estudiante en el formato "groupId-name", donde "grupo" es el identificador del grupo obtenido mediante el método `getGroupId`.

"Izquierdo Castanedo, Raúl", "Prácticas de Laboratorio-01" -> "01-Izquierdo Castanedo, Raúl"

### Implementar el método `loadGroups` en la clase `GroupLoader`

Este método debe leer un fichero de grupos y devolver una lista con los identificadores de los grupos leídos. El fichero debe tener el formato indicado en la sección "Fichero de Grupos", es decir, puede ser un fichero de texto únicamente con los identificadores de los grupos, uno por línea, o un
fichero CSV con más columnas pero que sólo se usará la primera columna.

Si el fichero no se encuentra, no se lanzará una excepción. Se hará lo siguiente:
- Se escribirá un mensaje al usuario indicando que no se ha encontrado el fichero de grupos (indicar el nombre) y que por tanto se generarán todos los student_id de todos los alumnos.
- Se invocará al método printGroupsFormat() de la clase `CommandLine` para mostrar el formato correcto del fichero de grupos.
- Se imprimirá un mensaje indicando que se generarán todos los student_id de todos los alumnos.
- El método devolverá una lista vacía.

Si el fichero se puede leer, pero no tiene el formato correcto, lanzará una excepción indicando que el formato del fichero no es correcto. Se indicará en qué no es correcto.

Si hay algún error durante la lectura del fichero, lanzará una excepción indicando qué ha pasado. En realidad, supongo que bastará con dejar pasar la excepción que se produzca.


### Implementar el método `createFilter`

Aquí hay las siguiente situaciones:
- Si loadGroups devuelve una lista, se devolverá un "StudentsFilter" que sólo acepta alumnos que estén en esa lista.
- Si loadGroups devuelve una lista vacía, se devolverá un "StudentsFilter" que aceptará todos los alumnos.

### Implementar el método `writeRoster` en la clase `RosterWriter`

Este método debe recibir una lista de objetos `Student` y un fichero de salida. Debe escribir en el fichero de salida el student_id de cada alumno, uno por línea, usando el filtro creado en el paso anterior. Es decir, si el filtro acepta un alumno, se escribirá su student_id en el fichero de salida.


### Complete the `main` method in 'Main.java'

Finish the implementation of the `main` method in the `Main` class.

## Other Requirements

Write the "Readme.md" file for the Github repository explaining the usage of the jar and the format of the input files. Include the following sections:
- Objective
- Usage
- Inputs
  - Excel file
  - Groups file
- Output
  - Roster file
- Other that you consider relevant in a Readme.md file.
  - The project uses Java 21.
  - To build the project, use the command `mvn clean package`.
