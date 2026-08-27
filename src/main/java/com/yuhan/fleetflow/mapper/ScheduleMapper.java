package com.yuhan.fleetflow.mapper;

import com.yuhan.fleetflow.dto.response.DriverAvailabilityResponse;
import com.yuhan.fleetflow.dto.response.ScheduleJobResponse;
import com.yuhan.fleetflow.dto.response.TruckAvailabilityResponse;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    @Select("""
        SELECT
            j.job_id AS jobId,

            q.quote_pickup_location AS pickupLocation,
            q.quote_dropoff_location AS dropoffLocation,

            j.job_pickup_datetime AS pickupDatetime,
            j.job_expected_dropoff_datetime AS expectedDropoffDatetime,

            j.job_status AS jobStatus,

            d.emp_id AS driverId,
            d.emp_name AS driverName,

            t.truck_id AS truckId,
            t.truck_registration_number AS truckRegistrationNumber,
            t.truck_model AS truckModel

        FROM job j

        JOIN quote q
            ON j.quote_id = q.quote_id

        JOIN employee d
            ON j.driver_emp_id = d.emp_id

        JOIN truck t
            ON j.truck_id = t.truck_id

        WHERE DATE(j.job_pickup_datetime) = #{date}

        AND j.job_status <> 'CANCELLED'

        ORDER BY j.job_pickup_datetime
        """)
    List<ScheduleJobResponse> findJobsByDate(
            @Param("date") LocalDate date
    );


    @Select("""
        SELECT
            e.emp_id AS driverId,
            e.emp_name AS driverName,

            CASE
                WHEN j.job_id IS NULL THEN TRUE
                ELSE FALSE
            END AS available,

            j.job_id AS conflictingJobId,
            j.job_pickup_datetime AS conflictStart,
            j.job_expected_dropoff_datetime AS conflictEnd,

            CASE
                WHEN j.job_id IS NULL THEN NULL
                ELSE 'SCHEDULE_CONFLICT'
            END AS reason

        FROM employee e

        LEFT JOIN job j
            ON j.driver_emp_id = e.emp_id

            AND j.job_status <> 'CANCELLED'

            AND j.job_pickup_datetime < #{dropoff}

            AND j.job_expected_dropoff_datetime > #{pickup}

        WHERE e.emp_role = 'DRIVER'
          AND e.emp_status = 'ACTIVE'

        ORDER BY e.emp_name
        """)
    List<DriverAvailabilityResponse> findDriverAvailability(
            @Param("pickup") LocalDateTime pickup,
            @Param("dropoff") LocalDateTime dropoff
    );


    @Select("""
        SELECT
            t.truck_id AS truckId,
            t.truck_registration_number AS registrationNumber,
            t.truck_model AS model,
            t.truck_status AS status,

            CASE
                WHEN t.truck_status <> 'ACTIVE' THEN FALSE
                WHEN j.job_id IS NOT NULL THEN FALSE
                ELSE TRUE
            END AS available,

            j.job_id AS conflictingJobId,
            j.job_pickup_datetime AS conflictStart,
            j.job_expected_dropoff_datetime AS conflictEnd,

            CASE
                WHEN t.truck_status = 'MAINTENANCE'
                    THEN 'MAINTENANCE'

                WHEN t.truck_status = 'INACTIVE'
                    THEN 'INACTIVE'

                WHEN j.job_id IS NOT NULL
                    THEN 'SCHEDULE_CONFLICT'

                ELSE NULL
            END AS reason

        FROM truck t

        LEFT JOIN job j
            ON j.truck_id = t.truck_id

            AND j.job_status <> 'CANCELLED'

            AND j.job_pickup_datetime < #{dropoff}

            AND j.job_expected_dropoff_datetime > #{pickup}

        ORDER BY t.truck_registration_number
        """)
    List<TruckAvailabilityResponse> findTruckAvailability(
            @Param("pickup") LocalDateTime pickup,
            @Param("dropoff") LocalDateTime dropoff
    );

    @Select("""
    SELECT
        j.job_id AS jobId,

        q.quote_pickup_location AS pickupLocation,
        q.quote_dropoff_location AS dropoffLocation,

        j.job_pickup_datetime AS pickupDatetime,
        j.job_expected_dropoff_datetime AS expectedDropoffDatetime,

        j.job_status AS jobStatus,

        d.emp_id AS driverId,
        d.emp_name AS driverName,

        t.truck_id AS truckId,
        t.truck_registration_number AS truckRegistrationNumber,
        t.truck_model AS truckModel

    FROM job j

    JOIN quote q
        ON j.quote_id = q.quote_id

    JOIN employee d
        ON j.driver_emp_id = d.emp_id

    JOIN truck t
        ON j.truck_id = t.truck_id

    WHERE j.driver_emp_id = #{driverId}

      AND DATE(j.job_pickup_datetime) = #{date}

      AND j.job_status <> 'CANCELLED'

    ORDER BY j.job_pickup_datetime
    """)
    List<ScheduleJobResponse> findDriverScheduleByDate(
            @Param("driverId") Long driverId,
            @Param("date") LocalDate date
    );

    @Select("""
    SELECT
        j.job_id AS jobId,

        q.quote_pickup_location AS pickupLocation,
        q.quote_dropoff_location AS dropoffLocation,

        j.job_pickup_datetime AS pickupDatetime,
        j.job_expected_dropoff_datetime AS expectedDropoffDatetime,

        j.job_status AS jobStatus,

        d.emp_id AS driverId,
        d.emp_name AS driverName,

        t.truck_id AS truckId,
        t.truck_registration_number AS truckRegistrationNumber,
        t.truck_model AS truckModel

    FROM job j

    JOIN quote q
        ON j.quote_id = q.quote_id

    JOIN employee d
        ON j.driver_emp_id = d.emp_id

    JOIN truck t
        ON j.truck_id = t.truck_id

    WHERE j.truck_id = #{truckId}

      AND DATE(j.job_pickup_datetime) = #{date}

      AND j.job_status <> 'CANCELLED'

    ORDER BY j.job_pickup_datetime
    """)
    List<ScheduleJobResponse> findTruckScheduleByDate(
            @Param("truckId") Long truckId,
            @Param("date") LocalDate date
    );
}