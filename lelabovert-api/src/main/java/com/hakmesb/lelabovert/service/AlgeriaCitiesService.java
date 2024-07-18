package com.hakmesb.lelabovert.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.payload.projection.CommuneDto;
import com.hakmesb.lelabovert.payload.projection.WilayaDto;
import com.hakmesb.lelabovert.repository.AlgeriaCitiesRepository;

@Service
public class AlgeriaCitiesService {
	
	private final AlgeriaCitiesRepository algeriaCitiesRepository;
	
	public AlgeriaCitiesService(AlgeriaCitiesRepository algeriaCitiesRepository) {
		this.algeriaCitiesRepository = algeriaCitiesRepository;
	}

	public List<WilayaDto> getWilayas() {
		return algeriaCitiesRepository.findAllWilayas();
	}
	
	public List<CommuneDto> getCommunes(String wilayaCode){
		return algeriaCitiesRepository.findAllCommunesByWilayaCode(wilayaCode);
	}
	
}
