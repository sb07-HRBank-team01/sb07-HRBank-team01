package com.codeit_team01.sb07_hrbank_team01.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaFileRepository extends JpaRepository<MetaFile, Long> {

}
