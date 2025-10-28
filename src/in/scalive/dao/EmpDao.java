package in.scalive.dao;

import in.scalive.dbutil.DBConnection;
import in.scalive.pojo.EmpPojo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpDao {
    public static void initSchema() throws SQLException {
        try (Connection conn = DBConnection.getDBConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS EMP (" +
                    "emp_no INT PRIMARY KEY, " +
                    "emp_name VARCHAR(100) NOT NULL, " +
                    "emp_sal DOUBLE, " +
                    "emp_comm DOUBLE, " +
                    "dept_no INT)");
        }
    }

    public static boolean upsert(EmpPojo emp) throws SQLException {
        String sql = "MERGE INTO EMP KEY(emp_no) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, emp.getEmpNo());
            ps.setString(2, emp.getEmpName());
            ps.setDouble(3, emp.getEmpSal());
            ps.setDouble(4, emp.getEmpComm());
            ps.setInt(5, emp.getDeptNo());
            return ps.executeUpdate() == 1;
        }
    }

    public static int importAll(List<EmpPojo> list) throws SQLException {
        if (list == null || list.isEmpty()) return 0;
        initSchema();
        int count = 0;
        for (EmpPojo e : list) {
            if (upsert(e)) count++;
        }
        return count;
    }

    public static boolean addEmp(EmpPojo emp) throws SQLException {
        String sql = "INSERT INTO EMP (emp_no, emp_name, emp_sal, emp_comm, dept_no) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, emp.getEmpNo());
            ps.setString(2, emp.getEmpName());
            ps.setDouble(3, emp.getEmpSal());
            ps.setDouble(4, emp.getEmpComm());
            ps.setInt(5, emp.getDeptNo());
            return ps.executeUpdate() == 1;
        }
    }

    public static void seedDemo() throws SQLException {
        initSchema();
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement("MERGE INTO EMP KEY(emp_no) VALUES (?,?,?,?,?)")) {
            ps.setInt(1, 201); ps.setString(2, "Alice"); ps.setDouble(3, 50000); ps.setDouble(4, 3000); ps.setInt(5, 10); ps.executeUpdate();
            ps.setInt(1, 202); ps.setString(2, "Bob");   ps.setDouble(3, 55000); ps.setDouble(4, 2500); ps.setInt(5, 20); ps.executeUpdate();
            ps.setInt(1, 203); ps.setString(2, "Carol"); ps.setDouble(3, 60000); ps.setDouble(4, 2000); ps.setInt(5, 10); ps.executeUpdate();
        }
    }

    public static List<EmpPojo> getAll() throws SQLException {
        String sql = "SELECT emp_no, emp_name, emp_sal, emp_comm, dept_no FROM EMP ORDER BY emp_no";
        List<EmpPojo> list = new ArrayList<>();
        try (Connection conn = DBConnection.getDBConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                EmpPojo e = new EmpPojo();
                e.setEmpNo(rs.getInt("emp_no"));
                e.setEmpName(rs.getString("emp_name"));
                e.setEmpSal(rs.getDouble("emp_sal"));
                e.setEmpComm(rs.getDouble("emp_comm"));
                e.setDeptNo(rs.getInt("dept_no"));
                list.add(e);
            }
        }
        return list;
    }

    public static boolean existsById(int empNo) throws SQLException {
        String sql = "SELECT 1 FROM EMP WHERE emp_no=?";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static List<EmpPojo> listByDept(int deptNo) throws SQLException {
        String sql = "SELECT emp_no, emp_name, emp_sal, emp_comm, dept_no FROM EMP WHERE dept_no=? ORDER BY emp_no";
        List<EmpPojo> list = new ArrayList<>();
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptNo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EmpPojo e = new EmpPojo();
                    e.setEmpNo(rs.getInt("emp_no"));
                    e.setEmpName(rs.getString("emp_name"));
                    e.setEmpSal(rs.getDouble("emp_sal"));
                    e.setEmpComm(rs.getDouble("emp_comm"));
                    e.setDeptNo(rs.getInt("dept_no"));
                    list.add(e);
                }
            }
        }
        return list;
    }

    public static long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM EMP";
        try (Connection conn = DBConnection.getDBConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0L;
    }

    public static EmpPojo findById(int empNo) throws SQLException {
        String sql = "SELECT emp_no, emp_name, emp_sal, emp_comm, dept_no FROM EMP WHERE emp_no=?";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmpPojo e = new EmpPojo();
                    e.setEmpNo(rs.getInt("emp_no"));
                    e.setEmpName(rs.getString("emp_name"));
                    e.setEmpSal(rs.getDouble("emp_sal"));
                    e.setEmpComm(rs.getDouble("emp_comm"));
                    e.setDeptNo(rs.getInt("dept_no"));
                    return e;
                }
            }
        }
        return null;
    }

    public static boolean updateEmp(EmpPojo emp) throws SQLException {
        String sql = "UPDATE EMP SET emp_name=?, emp_sal=?, emp_comm=?, dept_no=? WHERE emp_no=?";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getEmpName());
            ps.setDouble(2, emp.getEmpSal());
            ps.setDouble(3, emp.getEmpComm());
            ps.setInt(4, emp.getDeptNo());
            ps.setInt(5, emp.getEmpNo());
            return ps.executeUpdate() == 1;
        }
    }

    public static boolean deleteEmp(int empNo) throws SQLException {
        String sql = "DELETE FROM EMP WHERE emp_no=?";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empNo);
            return ps.executeUpdate() == 1;
        }
    }
}
