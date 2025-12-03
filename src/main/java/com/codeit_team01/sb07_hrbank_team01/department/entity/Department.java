package com.codeit_team01.sb07_hrbank_team01.department.entity;


import com.codeit_team01.sb07_hrbank_team01.base.BaseUpdateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "departments")
public class Department extends BaseUpdateEntity {

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "established_date", nullable = false)
    private LocalDate establishedDate;




    public static Department of(String name, String description, LocalDate establishDate) {
        return new Department(name, description, establishDate);
    }


    public void update(String newName, String newDescription, LocalDate newEstablishDate){
         if(newName != null && !newName.equals(this.name)){
             this.name = newName;
         }
         if(newDescription != null && !newDescription.equals(this.description)){
             this.description = newDescription;
         }
         if(newEstablishDate != null && !newEstablishDate.equals(this.establishedDate)){
             this.establishedDate = newEstablishDate;
         }


    }



}
