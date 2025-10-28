package in.scalive.app;

import in.scalive.dbutil.DBConnection;
import in.scalive.dao.EmpDao;
import in.scalive.pojo.EmpPojo;
import in.scalive.util.TableUtil;
import in.scalive.util.CsvUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("EmpMgmtApp (H2) starting...");
        try (Connection conn = DBConnection.getDBConnection(); Scanner sc = new Scanner(System.in)) {
            System.out.println("DB connection obtained: " + conn);
            EmpDao.initSchema();

            while (true) {
                System.out.println();
                System.out.println("1) Add Emp  2) List All  3) Find By Id  4) Update Emp  5) Delete Emp  6) List By Dept  7) Count  8) Import CSV  10) Export CSV  9) Seed Demo  0) Exit");
                System.out.print("Choose: ");
                String choice = sc.nextLine().trim();
                try {
                    switch (choice) {
                        case "1": {
                            EmpPojo emp = new EmpPojo();
                            emp.setEmpNo(readInt(sc, "Emp No"));
                            if (EmpDao.existsById(emp.getEmpNo())) {
                                System.out.println("Emp No already exists. Choose a different Emp No.");
                                break;
                            }
                            emp.setEmpName(readNonEmpty(sc, "Name"));
                            emp.setEmpSal(readNonNegativeDouble(sc, "Salary"));
                            emp.setEmpComm(readNonNegativeDouble(sc, "Commission"));
                            emp.setDeptNo(readInt(sc, "Dept No"));
                            boolean ok = EmpDao.addEmp(emp);
                            System.out.println(ok ? "Inserted" : "Insert failed");
                            break;
                        }
                        case "2": {
                            List<EmpPojo> list = EmpDao.getAll();
                            TableUtil.printEmployees(list);
                            break;
                        }
                        case "3": {
                            int eno = readInt(sc, "Emp No");
                            EmpPojo e = EmpDao.findById(eno);
                            System.out.println(e != null ? e : "Not found");
                            break;
                        }
                        case "4": {
                            int eno = readInt(sc, "Emp No to update");
                            EmpPojo e = EmpDao.findById(eno);
                            if (e == null) { System.out.println("Not found"); break; }
                            System.out.print("Name ["+e.getEmpName()+"]: "); String name = sc.nextLine().trim();
                            if (!name.isEmpty()) e.setEmpName(name);
                            System.out.print("Salary ["+e.getEmpSal()+"]: "); String sal = sc.nextLine().trim();
                            if (!sal.isEmpty()) {
                                double v = Double.parseDouble(sal);
                                if (v < 0) { System.out.println("Salary cannot be negative"); break; }
                                e.setEmpSal(v);
                            }
                            System.out.print("Commission ["+e.getEmpComm()+"]: "); String comm = sc.nextLine().trim();
                            if (!comm.isEmpty()) {
                                double v = Double.parseDouble(comm);
                                if (v < 0) { System.out.println("Commission cannot be negative"); break; }
                                e.setEmpComm(v);
                            }
                            System.out.print("Dept No ["+e.getDeptNo()+"]: "); String dept = sc.nextLine().trim();
                            if (!dept.isEmpty()) e.setDeptNo(Integer.parseInt(dept));
                            boolean ok = EmpDao.updateEmp(e);
                            System.out.println(ok ? "Updated" : "Update failed");
                            break;
                        }
                        case "5": {
                            int eno = readInt(sc, "Emp No to delete");
                            boolean ok = EmpDao.deleteEmp(eno);
                            System.out.println(ok ? "Deleted" : "Delete failed");
                            break;
                        }
                        case "6": {
                            int dept = readInt(sc, "Dept No");
                            List<EmpPojo> list = EmpDao.listByDept(dept);
                            if (list.isEmpty()) System.out.println("No employees in dept " + dept);
                            else TableUtil.printEmployees(list);
                            break;
                        }
                        case "7": {
                            long c = EmpDao.count();
                            System.out.println("Total employees: " + c);
                            break;
                        }
                        case "8": {
                            System.out.print("CSV path to import: ");
                            String path = sc.nextLine().trim();
                            try {
                                List<EmpPojo> list = CsvUtil.readEmployees(path);
                                int n = EmpDao.importAll(list);
                                System.out.println("Imported/Upserted: " + n);
                            } catch (Exception ex) {
                                System.out.println("Import failed: " + ex.getMessage());
                            }
                            break;
                        }
                        case "10": {
                            System.out.print("CSV path to export: ");
                            String path = sc.nextLine().trim();
                            try {
                                List<EmpPojo> list = EmpDao.getAll();
                                CsvUtil.writeEmployees(path, list);
                                System.out.println("Exported " + list.size() + " employees to " + path);
                            } catch (Exception ex) {
                                System.out.println("Export failed: " + ex.getMessage());
                            }
                            break;
                        }
                        case "9": {
                            EmpDao.seedDemo();
                            System.out.println("Seeded. Current employees:");
                            TableUtil.printEmployees(EmpDao.getAll());
                            break;
                        }
                        case "0":
                            System.out.println("Bye");
                            return;
                        default:
                            System.out.println("Invalid choice");
                    }
                } catch (SQLException ex) {
                    System.out.println("DB error: " + ex.getMessage());
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid number");
                }
            }
        } catch (SQLException e) {
            System.out.println("DB operation failed: " + e.getMessage());
        }
    }

    private static String readNonEmpty(Scanner sc, String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Value cannot be empty");
        }
    }

    private static int readInt(Scanner sc, String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid integer"); }
        }
    }

    private static double readDouble(Scanner sc, String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            try { return Double.parseDouble(s); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid number"); }
        }
    }

    private static double readNonNegativeDouble(Scanner sc, String label) {
        while (true) {
            double v = readDouble(sc, label);
            if (v < 0) {
                System.out.println(label + " cannot be negative");
            } else {
                return v;
            }
        }
    }
}
