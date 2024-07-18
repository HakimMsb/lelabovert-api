package com.hakmesb.lelabovert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hakmesb.lelabovert.model.AlgeriaCity;
import com.hakmesb.lelabovert.payload.projection.CommuneDto;
import com.hakmesb.lelabovert.payload.projection.WilayaDto;

public interface AlgeriaCitiesRepository extends JpaRepository<AlgeriaCity, Integer>{
	
	@Query("""
            SELECT DISTINCT NEW com.hakmesb.lelabovert.payload.projection.WilayaDto(ac.wilayaNameAscii, ac.wilayaCode)\
             FROM AlgeriaCity ac ORDER BY ac.wilayaCode ASC\
            """)
	List<WilayaDto> findAllWilayas();
	
	@Query("""
            SELECT DISTINCT NEW com.hakmesb.lelabovert.payload.projection.CommuneDto(ac.id, ac.communeNameAscii)\
             FROM AlgeriaCity ac WHERE ac.wilayaCode = :wilayaCode ORDER BY ac.communeNameAscii ASC\
            """)
	List<CommuneDto> findAllCommunesByWilayaCode(String wilayaCode);

}
