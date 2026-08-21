package com.yuhan.fleetflow.mapper;

import com.yuhan.fleetflow.model.Job;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface JobMapper {

    @Insert("""
        INSERT INTO job (
            quote_id,
            driver_emp_id,
            scheduled_by_emp_id,
            truck_id,
            job_pickup_datetime,
            job_expected_dropoff_datetime,
            job_final_price
        )
        VALUES (
            #{quoteId},
            #{driverEmpId},
            #{scheduledByEmpId},
            #{truckId},
            #{jobPickupDatetime},
            #{jobExpectedDropoffDatetime},
            #{jobFinalPrice}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "jobId")
    int insert(Job job);

    @Select("""
        SELECT
            job_id AS jobId,
            quote_id AS quoteId,
            driver_emp_id AS driverEmpId,
            scheduled_by_emp_id AS scheduledByEmpId,
            truck_id AS truckId,
            job_pickup_datetime AS jobPickupDatetime,
            job_expected_dropoff_datetime AS jobExpectedDropoffDatetime,
            job_final_price AS jobFinalPrice,
            job_status AS jobStatus,
            job_payment_status AS jobPaymentStatus,
            job_created_at AS jobCreatedAt,
            job_updated_at AS jobUpdatedAt
        FROM job
        WHERE job_id = #{id}
        """)
    Job findById(Long id);

    @Select("""
        SELECT COUNT(*)
        FROM job
        WHERE quote_id = #{quoteId}
        """)
    int countByQuoteId(Long quoteId);

    @Select("""
    SELECT COUNT(*)
    FROM job
    WHERE driver_emp_id = #{driverEmpId}
      AND job_status IN ('SCHEDULED', 'IN_PROGRESS')
      AND job_pickup_datetime < #{newEnd}
      AND job_expected_dropoff_datetime > #{newStart}
    """)
    int countDriverConflicts(
            Long driverEmpId,
            LocalDateTime newStart,
            LocalDateTime newEnd
    );

    @Select("""
    SELECT COUNT(*)
    FROM job
    WHERE truck_id = #{truckId}
      AND job_status IN ('SCHEDULED', 'IN_PROGRESS')
      AND job_pickup_datetime < #{newEnd}
      AND job_expected_dropoff_datetime > #{newStart}
    """)
    int countTruckConflicts(
            Long truckId,
            LocalDateTime newStart,
            LocalDateTime newEnd
    );
}