package in.scalive.dao;

import in.scalive.dbutil.DBConnection;
import in.scalive.pojo.EmpPojo;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmpDaoTest {

    @BeforeAll
    static void setupDb() throws SQLException {
        // Ensure DB is reachable and schema exists
        try (Connection conn = DBConnection.getDBConnection()) {
            assertNotNull(conn);
        }
        EmpDao.initSchema();
    }

    @Test
    void testSeedAndList() throws SQLException {
        EmpDao.seedDemo();
        List<EmpPojo> list = EmpDao.getAll();
        assertTrue(list.size() >= 3, "Expected at least 3 seeded employees");
    }

    @Test
    void testFindUpdateDelete() throws SQLException {
        EmpDao.seedDemo();
        EmpPojo e = EmpDao.findById(202);
        assertNotNull(e, "Employee 202 should exist after seeding");

        double oldSal = e.getEmpSal();
        e.setEmpSal(oldSal + 123.45);
        assertTrue(EmpDao.updateEmp(e));

        EmpPojo e2 = EmpDao.findById(202);
        assertNotNull(e2);
        assertEquals(oldSal + 123.45, e2.getEmpSal(), 0.0001);

        assertTrue(EmpDao.deleteEmp(203));
        assertNull(EmpDao.findById(203));
    }
}
