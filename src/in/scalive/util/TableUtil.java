package in.scalive.util;

import in.scalive.pojo.EmpPojo;
import java.util.List;

public class TableUtil {
    public static void printEmployees(List<EmpPojo> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No employees");
            return;
        }
        int wNo = Math.max(6, list.stream().mapToInt(e -> String.valueOf(e.getEmpNo()).length()).max().orElse(6));
        int wName = Math.max(8, list.stream().mapToInt(e -> e.getEmpName() != null ? e.getEmpName().length() : 0).max().orElse(8));
        int wSal = 10, wComm = 10, wDept = 7;
        String header = String.format("%-"+wNo+"s  %-"+wName+"s  %"+wSal+"s  %"+wComm+"s  %"+wDept+"s",
                "EMP_NO", "EMP_NAME", "EMP_SAL", "EMP_COMM", "DEPT_NO");
        String sep = repeat('-', header.length());
        System.out.println(header);
        System.out.println(sep);
        for (EmpPojo e : list) {
            System.out.println(String.format("%-"+wNo+"d  %-"+wName+"s  %"+wSal+".2f  %"+wComm+".2f  %"+wDept+"d",
                    e.getEmpNo(), nullSafe(e.getEmpName()), e.getEmpSal(), e.getEmpComm(), e.getDeptNo()));
        }
    }

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }

    private static String nullSafe(String s) { return s == null ? "" : s; }
}
