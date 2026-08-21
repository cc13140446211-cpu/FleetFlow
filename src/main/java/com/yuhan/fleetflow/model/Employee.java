package com.yuhan.fleetflow.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Employee {

    private Long empId;
    private String empName;
    private String empPhone;
    private String empRole;
    private String empLicenseNumber;
    private LocalDate empLicenseExpiryDate;
    private String empStatus;
    private LocalDateTime empCreatedAt;

    public Employee() {
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpRole() {
        return empRole;
    }

    public void setEmpRole(String empRole) {
        this.empRole = empRole;
    }

    public String getEmpLicenseNumber() {
        return empLicenseNumber;
    }

    public void setEmpLicenseNumber(String empLicenseNumber) {
        this.empLicenseNumber = empLicenseNumber;
    }

    public LocalDate getEmpLicenseExpiryDate() {
        return empLicenseExpiryDate;
    }

    public void setEmpLicenseExpiryDate(LocalDate empLicenseExpiryDate) {
        this.empLicenseExpiryDate = empLicenseExpiryDate;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public LocalDateTime getEmpCreatedAt() {
        return empCreatedAt;
    }

    public void setEmpCreatedAt(LocalDateTime empCreatedAt) {
        this.empCreatedAt = empCreatedAt;
    }
}