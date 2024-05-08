    package com.jomariabejo.motorph.entity;

    import com.jomariabejo.motorph.enums.EmployeeStatus;

    import java.math.BigDecimal;
    import java.sql.Date;

    public class Employee {
        private int employeeId;
        private String firstName;
        private String lastName;
        private Date birthday;
        private String address;
        private String contactNumber;
        private EmployeeStatus status;
        private Date dateHired;
        private int positionId;
        private String supervisor;
        private int deptId;
        private String sss;
        private String philhealth;
        private String pagibig;
        private String tin;
        private BigDecimal basicSalary;
        private BigDecimal grossSemiMonthlyRate;
        private BigDecimal hourlyRate;

        // Constructor
        public Employee() {}

        public Employee(int employeeId, String firstName, String lastName, Date birthday, String address, String contactNumber, EmployeeStatus status, Date dateHired, int positionId, String supervisor, int deptId, String sss, String philhealth, String pagibig, String tin, BigDecimal basicSalary, BigDecimal grossSemiMonthlyRate, BigDecimal hourlyRate) {
            this.employeeId = employeeId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthday = birthday;
            this.address = address;
            this.contactNumber = contactNumber;
            this.status = status;
            this.dateHired = dateHired;
            this.positionId = positionId;
            this.supervisor = supervisor;
            this.deptId = deptId;
            this.sss = sss;
            this.philhealth = philhealth;
            this.pagibig = pagibig;
            this.tin = tin;
            this.basicSalary = basicSalary;
            this.grossSemiMonthlyRate = grossSemiMonthlyRate;
            this.hourlyRate = hourlyRate;
        }

        public Employee(int employeeId, String firstName, String lastName, Date birthday, String address, String contactNumber, EmployeeStatus status, String supervisor, String sss, String philhealth, String pagibig, String tin, BigDecimal basicSalary, BigDecimal grossSemiMonthlyRate, BigDecimal hourlyRate) {
            this.employeeId = employeeId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthday = birthday;
            this.address = address;
            this.contactNumber = contactNumber;
            this.status = status;
            this.supervisor = supervisor;
            this.sss = sss;
            this.philhealth = philhealth;
            this.pagibig = pagibig;
            this.tin = tin;
            this.basicSalary = basicSalary;
            this.grossSemiMonthlyRate = grossSemiMonthlyRate;
            this.hourlyRate = hourlyRate;
        }

        // Getters and setters
        public int getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public EmployeeStatus getStatus() {
            return status;
        }

        public void setStatus(EmployeeStatus status) {
            this.status = status;
        }

        public Date getDateHired() {
            return dateHired;
        }

        public void setDateHired(Date dateHired) {
            this.dateHired = dateHired;
        }

        public int getPositionId() {
            return positionId;
        }

        public void setPositionId(int positionId) {
            this.positionId = positionId;
        }

        public String getSupervisor() {
            return supervisor;
        }

        public void setSupervisor(String supervisor) {
            this.supervisor = supervisor;
        }

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public String getSss() {
            return sss;
        }

        public void setSss(String sss) {
            this.sss = sss;
        }

        public String getPhilhealth() {
            return philhealth;
        }

        public void setPhilhealth(String philhealth) {
            this.philhealth = philhealth;
        }

        public String getPagibig() {
            return pagibig;
        }

        public void setPagibig(String pagibig) {
            this.pagibig = pagibig;
        }

        public String getTin() {
            return tin;
        }

        public void setTin(String tin) {
            this.tin = tin;
        }

        public BigDecimal getBasicSalary() {
            return basicSalary;
        }

        public void setBasicSalary(BigDecimal basicSalary) {
            this.basicSalary = basicSalary;
        }

        public BigDecimal getGrossSemiMonthlyRate() {
            return grossSemiMonthlyRate;
        }

        public void setGrossSemiMonthlyRate(BigDecimal grossSemiMonthlyRate) {
            this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        }

        public BigDecimal getHourlyRate() {
            return hourlyRate;
        }

        public void setHourlyRate(BigDecimal hourlyRate) {
            this.hourlyRate = hourlyRate;
        }
    }
