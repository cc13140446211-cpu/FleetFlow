package com.yuhan.fleetflow.mapper;

import com.yuhan.fleetflow.model.Truck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TruckMapper {

    @Select("""
        SELECT COUNT(*)
        FROM truck
        WHERE truck_id = #{id}
        """)
    int existsById(Long id);

    @Select("""
    SELECT
        truck_id AS truckId,
        truck_vin AS truckVin,
        truck_registration_number AS truckRegistrationNumber,
        truck_model AS truckModel,
        truck_capacity_kg AS truckCapacityKg,
        truck_status AS truckStatus,
        truck_created_at AS truckCreatedAt
    FROM truck
    ORDER BY truck_registration_number
    """)
    List<Truck> findAll();

    @Select("""
    SELECT
        truck_id AS truckId,
        truck_vin AS truckVin,
        truck_registration_number AS truckRegistrationNumber,
        truck_model AS truckModel,
        truck_capacity_kg AS truckCapacityKg,
        truck_status AS truckStatus,
        truck_created_at AS truckCreatedAt
    FROM truck
    WHERE truck_id = #{id}
    """)
    Truck findById(@Param("id") Long id);

}