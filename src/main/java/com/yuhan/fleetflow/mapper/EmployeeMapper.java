package com.yuhan.fleetflow.mapper;

import com.yuhan.fleetflow.model.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Select("""
        SELECT
            emp_id AS empId,
            emp_name AS empName,
            emp_phone AS empPhone,
            emp_role AS empRole,
            emp_license_number AS empLicenseNumber,
            emp_license_expiry_date AS empLicenseExpiryDate,
            emp_status AS empStatus,
            emp_created_at AS empCreatedAt
        FROM employee
        WHERE emp_id = #{id}
        """)
    Employee findById(Long id);

    @Select("""
    SELECT
        emp_id AS empId,
        emp_name AS empName,
        emp_phone AS empPhone,
        emp_role AS empRole,
        emp_license_number AS empLicenseNumber,
        emp_license_expiry_date AS empLicenseExpiryDate,
        emp_status AS empStatus,
        emp_created_at AS empCreatedAt
    FROM employee
    WHERE emp_role = 'DRIVER'
    ORDER BY emp_name
    """)
    List<Employee> findAllDrivers();

    @Select("""
    SELECT
        emp_id AS empId,
        emp_name AS empName,
        emp_phone AS empPhone,
        emp_role AS empRole,
        emp_license_number AS empLicenseNumber,
        emp_license_expiry_date AS empLicenseExpiryDate,
        emp_status AS empStatus,
        emp_created_at AS empCreatedAt
    FROM employee
    WHERE emp_id = #{id}
      AND emp_role = 'DRIVER'
    """)
    Employee findDriverById(@Param("id") Long id);

}