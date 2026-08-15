package com.yuhan.fleetflow.mapper;

import com.yuhan.fleetflow.model.Customer;
import org.apache.ibatis.annotations.*;

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
    WHERE cust_id = #{id}
    """)
    Customer findById(Long id);

    @Insert("""
    INSERT INTO customer (
        cust_name,
        cust_company_name,
        cust_phone,
        cust_email,
        cust_address
    )
    VALUES (
        #{custName},
        #{custCompanyName},
        #{custPhone},
        #{custEmail},
        #{custAddress}
    )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "custId")
    int insert(Customer customer);
}