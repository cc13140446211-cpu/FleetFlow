package com.yuhan.fleetflow.mapper;

import com.yuhan.fleetflow.model.Quote;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuoteMapper {

    @Select("""
        SELECT
            quote_id AS quoteId,
            cust_id AS custId,
            prepared_by_emp_id AS preparedByEmpId,
            quote_pickup_location AS quotePickupLocation,
            quote_dropoff_location AS quoteDropoffLocation,
            quote_preferred_pickup_date AS quotePreferredPickupDate,
            quote_price AS quotePrice,
            quote_status AS quoteStatus,
            quote_created_at AS quoteCreatedAt,
            quote_updated_at AS quoteUpdatedAt
        FROM quote
        """)
    List<Quote> findAll();

    @Select("""
        SELECT
            quote_id AS quoteId,
            cust_id AS custId,
            prepared_by_emp_id AS preparedByEmpId,
            quote_pickup_location AS quotePickupLocation,
            quote_dropoff_location AS quoteDropoffLocation,
            quote_preferred_pickup_date AS quotePreferredPickupDate,
            quote_price AS quotePrice,
            quote_status AS quoteStatus,
            quote_created_at AS quoteCreatedAt,
            quote_updated_at AS quoteUpdatedAt
        FROM quote
        WHERE quote_id = #{id}
        """)
    Quote findById(Long id);

    @Insert("""
        INSERT INTO quote (
            cust_id,
            prepared_by_emp_id,
            quote_pickup_location,
            quote_dropoff_location,
            quote_preferred_pickup_date,
            quote_price
        )
        VALUES (
            #{custId},
            #{preparedByEmpId},
            #{quotePickupLocation},
            #{quoteDropoffLocation},
            #{quotePreferredPickupDate},
            #{quotePrice}
        )
        """)
    @Options(
            useGeneratedKeys = true,
            keyProperty = "quoteId"
    )
    int insert(Quote quote);
}