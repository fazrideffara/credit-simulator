package com.bcadigital.creditsimulator.model;

import java.math.BigDecimal;

public class LoanRequest {

    private String rawVehicleType;
    private String rawVehicleCondition;
    private int vehicleYear;
    private BigDecimal totalLoanAmount;
    private int loanTenure;
    private BigDecimal downPayment;

    public LoanRequest() {
    }

    public LoanRequest(String rawVehicleType, String rawVehicleCondition, int vehicleYear,
                       BigDecimal totalLoanAmount, int loanTenure, BigDecimal downPayment) {
        this.rawVehicleType = rawVehicleType;
        this.rawVehicleCondition = rawVehicleCondition;
        this.vehicleYear = vehicleYear;
        this.totalLoanAmount = totalLoanAmount;
        this.loanTenure = loanTenure;
        this.downPayment = downPayment;
    }

    public String getRawVehicleType() {
        return rawVehicleType;
    }

    public void setRawVehicleType(String rawVehicleType) {
        this.rawVehicleType = rawVehicleType;
    }

    public String getRawVehicleCondition() {
        return rawVehicleCondition;
    }

    public void setRawVehicleCondition(String rawVehicleCondition) {
        this.rawVehicleCondition = rawVehicleCondition;
    }

    public VehicleType getVehicleType() {
        return VehicleType.fromString(rawVehicleType);
    }

    public VehicleCondition getVehicleCondition() {
        return VehicleCondition.fromString(rawVehicleCondition);
    }

    public int getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(int vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public BigDecimal getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(BigDecimal totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public int getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(int loanTenure) {
        this.loanTenure = loanTenure;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }
}
