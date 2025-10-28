# EmpMgmtApp

A simple Java employee management console app with a JDBC DAO layer.
- H2 database by default (file-based, persists in `./data/empmgmt`).
- Configurable via `config.properties` (can switch to Oracle later).
- CSV import/export and pretty table output.
- Maven build configured; JUnit tests for DAO.

## Project Layout
- `src/` Java sources (kept as provided; not Maven-standard layout).
- `test/` JUnit tests.
- `lib/` third-party jars (e.g., `h2.jar` when running without Maven).
- `data/` H2 database files (auto-created on first run).
- `pom.xml` Maven configuration.

## Features
- CRUD on employees (`EmpDao`) with schema initialization.
- Validated CLI menu (`Main`):
  - 1 Add Emp
  - 2 List All
  - 3 Find By Id
  - 4 Update Emp
  - 5 Delete Emp
  - 6 List By Dept
  - 7 Count
  - 8 Import CSV
  - 9 Seed Demo
  - 10 Export CSV
  - 0 Exit
- CSV utilities: `CsvUtil.readEmployees`, `CsvUtil.writeEmployees`.
- Pretty table printer: `TableUtil.printEmployees`.

## Prerequisites
- Java 11+ (adjust `pom.xml` if using a different version).
- For non-Maven runs: place `h2.jar` in `lib/` (already added by setup steps above).

## Run (without Maven)
Compile:
```
javac -cp 'lib/h2.jar' -d bin -sourcepath src \
  src/in/scalive/util/TableUtil.java src/in/scalive/util/CsvUtil.java \
  src/in/scalive/config/AppConfig.java src/in/scalive/dbutil/DBConnection.java \
  src/in/scalive/dao/EmpDao.java src/in/scalive/app/Main.java
```
Run:
```
java -cp 'bin;lib/h2.jar' in.scalive.app.Main
```

## Run (with Maven)
Build:
```
mvn -q package
```
Run:
```
mvn -q exec:java
```
Test:
```
mvn -q test
```

## Configuration
`DBConnection` loads settings from `in.scalive.config.AppConfig`, which reads `config.properties` if present in the project root.
Defaults:
```
db.url=jdbc:h2:file:./data/empmgmt;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1
db.user=sa
db.password=
```
To use Oracle instead (requires `ojdbc` on classpath or added via Maven):
```
db.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
db.user=your_user
db.password=your_password
```

## CSV Format
Header row expected/written:
```
emp_no,emp_name,emp_sal,emp_comm,dept_no
```
Example row:
```
301,Jane,72000,3500,30
```

## Notes
- The project keeps the original `src`/`test` layout; `pom.xml` is configured accordingly.
- For Windows PowerShell, single quotes around classpaths like `'bin;lib/h2.jar'` avoid `;` parsing issues.
- To change DB location, adjust `db.url` in `config.properties`.
# EmpMgmtApp
