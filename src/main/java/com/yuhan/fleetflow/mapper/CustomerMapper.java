package com.yuhan.fleetflow.mapper;

import com.yuhan.fleetflow.model.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper {

    @Select("""
        SELECT
            cust_id AS custId,
            cust_name AS custName,
            cust_company_name AS custCompanyName,
            cust_phone AS custPhone,
            cust_email AS custEmail,
            cust_address AS custAddress,
            cust_created_at AS custCreatedAt
        FROM customer
        """)
    List<Customer> findAll();
}