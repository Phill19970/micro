package com.capstone.expenseservice.mapper;

import com.capstone.expenseservice.dto.v1.ExpenseDTO;
import com.capstone.expenseservice.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    Expense toEntity(ExpenseDTO expenseDTO);

    ExpenseDTO toDTO(Expense expense);


}
