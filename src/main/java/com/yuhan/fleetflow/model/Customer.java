package com.yuhan.fleetflow.model;
import java.time.LocalDateTime;

public class Customer {

    private Long custId;
    private String custName;
    private String custCompanyName;
    private String custPhone;
    private String custEmail;
    private String custAddress;
    private LocalDateTime custCreatedAt;

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustCompanyName() {
        return custCompanyName;
    }

    public void setCustCompanyName(String custCompanyName) {
        this.custCompanyName = custCompanyName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public LocalDateTime getCustCreatedAt() {
        return custCreatedAt;
    }

    public void setCustCreatedAt(LocalDateTime custCreatedAt) {
        this.custCreatedAt = custCreatedAt;
    }
}
