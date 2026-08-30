package com.yuhan.fleetflow.mapper;

import com.yuhan.fleetflow.model.Job;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
                job_created_at AS jobCreatedAt,
                job_updated_at AS jobUpdatedAt
            FROM job
            WHERE job_id = #{id}
            """)
    Job findById(Long id);

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
        job_created_at AS jobCreatedAt,
        job_updated_at AS jobUpdatedAt
    FROM job
    """)
    List<Job> findAll();

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

    @Select("""
            SELECT COUNT(*)
            FROM job
            WHERE driver_emp_id = #{driverEmpId}
              AND job_id <> #{jobId}
              AND job_status IN ('SCHEDULED', 'IN_PROGRESS')
              AND job_pickup_datetime < #{newEnd}
              AND job_expected_dropoff_datetime > #{newStart}
            """)
    int countDriverConflictsExcludingJob(
            Long driverEmpId,
            Long jobId,
            LocalDateTime newStart,
            LocalDateTime newEnd
    );

    @Select("""
            SELECT COUNT(*)
            FROM job
            WHERE truck_id = #{truckId}
              AND job_id <> #{jobId}
              AND job_status IN ('SCHEDULED', 'IN_PROGRESS')
              AND job_pickup_datetime < #{newEnd}
              AND job_expected_dropoff_datetime > #{newStart}
            """)
    int countTruckConflictsExcludingJob(
            Long truckId,
            Long jobId,
            LocalDateTime newStart,
            LocalDateTime newEnd
    );

    @Update("""
            UPDATE job
            SET driver_emp_id = #{driverEmpId},
                truck_id = #{truckId},
                job_pickup_datetime = #{jobPickupDatetime},
                job_expected_dropoff_datetime = #{jobExpectedDropoffDatetime},
                job_final_price = #{jobFinalPrice}
            WHERE job_id = #{jobId}
            """)
    int update(Job job);

    @Update("""
            UPDATE job
            SET job_status = #{status}
            WHERE job_id = #{id}
            """)
    int updateStatus(Long id, String status);

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
        job_created_at AS jobCreatedAt,
        job_updated_at AS jobUpdatedAt
    FROM job
    WHERE
        (#{status} IS NULL OR job_status = #{status})
        AND
        (
            #{date} IS NULL
            OR DATE(job_pickup_datetime) = #{date}
        )
    ORDER BY job_pickup_datetime DESC
    """)
    List<Job> findJobs(
            @Param("status") String status,
            @Param("date") LocalDate date
    );

}
