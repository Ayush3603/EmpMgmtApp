package in.scalive.util;

import in.scalive.pojo.EmpPojo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    public static List<EmpPojo> readEmployees(String csvPath) throws IOException {
        Path p = Paths.get(csvPath);
        List<EmpPojo> list = new ArrayList<>();
        if (!Files.exists(p)) throw new IOException("File not found: " + csvPath);
        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    // maybe header or invalid line
                    if (first) { first = false; continue; }
                    else continue;
                }
                if (first) {
                    // Skip header if present
                    if (!isInteger(parts[0].trim())) { first = false; continue; }
                    first = false;
                }
                EmpPojo e = new EmpPojo();
                e.setEmpNo(Integer.parseInt(parts[0].trim()));
                e.setEmpName(parts[1].trim());
                e.setEmpSal(parseDouble(parts[2].trim()));
                e.setEmpComm(parseDouble(parts[3].trim()));
                e.setDeptNo(Integer.parseInt(parts[4].trim()));
                list.add(e);
            }
        }
        return list;
    }

    public static void writeEmployees(String csvPath, List<EmpPojo> list) throws IOException {
        Path p = Paths.get(csvPath);
        if (p.getParent() != null) Files.createDirectories(p.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            bw.write("emp_no,emp_name,emp_sal,emp_comm,dept_no");
            bw.newLine();
            if (list != null) {
                for (EmpPojo e : list) {
                    bw.write(String.format("%d,%s,%.2f,%.2f,%d",
                            e.getEmpNo(), safe(e.getEmpName()), e.getEmpSal(), e.getEmpComm(), e.getDeptNo()));
                    bw.newLine();
                }
            }
        }
    }

    private static boolean isInteger(String s) {
        try { Integer.parseInt(s); return true; } catch (NumberFormatException e) { return false; }
    }
    private static double parseDouble(String s) {
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return 0.0; }
    }
    private static String safe(String s) { return s == null ? "" : s; }
}
