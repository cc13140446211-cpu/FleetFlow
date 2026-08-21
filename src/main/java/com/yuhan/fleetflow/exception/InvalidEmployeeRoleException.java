package com.yuhan.fleetflow.exception;

public class InvalidEmployeeRoleException extends RuntimeException {

    public InvalidEmployeeRoleException(Long id, String requiredRole) {
        super(
                "Employee with id " + id +
                        " must have role: " + requiredRole
        );
    }
}
