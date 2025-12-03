package com.codeit_team01.sb07_hrbank_team01.department.controller;

import com.codeit_team01.sb07_hrbank_team01.common.api.ApiResponseDto;
import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentSearchRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentUpdateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.response.DepartmentResponseDto;
import com.codeit_team01.sb07_hrbank_team01.department.service.DepartmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;



    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(@RequestBody @Valid DepartmentCreateRequestDto request){

        DepartmentResponseDto department = departmentService.createDepartment(request);

        return   ResponseEntity.status(HttpStatus.CREATED).body(department);
    }

    @GetMapping(value = "/{departmentId}")
    public ResponseEntity<DepartmentResponseDto> getDepartment(@PathVariable Long departmentId){

        DepartmentResponseDto department = departmentService.getDepartment(departmentId);

        return ResponseEntity.status(HttpStatus.OK).body(department);
    }

    @PatchMapping(value = "/{departmentId}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable Long departmentId,
            @RequestBody @Valid  DepartmentUpdateRequestDto request){

        DepartmentResponseDto department = departmentService.updateDepartment(departmentId, request);

        return ResponseEntity.status(HttpStatus.OK).body(department);
    }

   @DeleteMapping(value = "/{departmentId}")
   public ResponseEntity<Void> deleteDepartment(
           @PathVariable
           Long departmentId){

      departmentService.deleteDepartment(departmentId);

      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

   }

    @GetMapping
    public ResponseEntity<PageResponseDto<Department>> searchDepartment(@Valid @ModelAttribute DepartmentSearchRequestDto request){

        PageResponseDto<Department> departmentPageResponseDto = departmentService.searchDepartment(request);

        return ResponseEntity.status(HttpStatus.OK).body(departmentPageResponseDto);
    }

}
