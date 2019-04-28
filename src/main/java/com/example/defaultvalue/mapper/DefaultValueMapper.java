package com.example.defaultvalue.mapper;

import com.example.defaultvalue.model.DefaultValue;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface DefaultValueMapper {

    @Select("select * from default_value where service = #{service} and clazz = #{clazz}")
    @MapKey("field")
    Map<String, DefaultValue> findByServiceAndClazz(@Param("service") String service, @Param("clazz") String clazz);
}
