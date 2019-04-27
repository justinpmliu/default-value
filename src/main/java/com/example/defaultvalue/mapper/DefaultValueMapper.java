package com.example.defaultvalue.mapper;

import com.example.defaultvalue.model.DefaultValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DefaultValueMapper {

    @Select("select * from default_value")
    List<DefaultValue> findAll();
}
