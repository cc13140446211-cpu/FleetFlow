package com.yuhan.fleetflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TruckMapper {

    @Select("""
        SELECT COUNT(*)
        FROM truck
        WHERE truck_id = #{id}
        """)
    int existsById(Long id);
}