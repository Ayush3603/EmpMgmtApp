package in.scalive.pojo;

public class EmpPojo {
	private int empNo;
	private String empName;
	private double empSal;
	private double empComm;
	private int deptNo;
	

	public EmpPojo() {
		
	}

	public EmpPojo(int empNo, String empName, double empSal, double empComm, int deptNo) {

		this.empNo = empNo;
		this.empName = empName;
		this.empSal = empSal;
		this.empComm = empComm;
		this.deptNo = deptNo;
	}

	public int getEmpNo() {
		return empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public double getEmpSal() {
		return empSal;
	}

	public void setEmpSal(double empSal) {
		this.empSal = empSal;
	}

	public double getEmpComm() {
		return empComm;
	}

	public void setEmpComm(double empComm) {
		this.empComm = empComm;
	}

	public int getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}

	@Override
	public String toString() {
		return "EmpPojo [empNo=" + empNo + ", empName=" + empName + ", empSal=" + empSal + ", empComm=" + empComm
				+ ", deptNo=" + deptNo + "]";
	}
	
}
