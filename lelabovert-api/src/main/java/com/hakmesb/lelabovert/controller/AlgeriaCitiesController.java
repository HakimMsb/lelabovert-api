package com.hakmesb.lelabovert.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hakmesb.lelabovert.payload.projection.CommuneDto;
import com.hakmesb.lelabovert.payload.projection.WilayaDto;
import com.hakmesb.lelabovert.service.AlgeriaCitiesService;

@RequestMapping("/api/v1")
@RestController
public class AlgeriaCitiesController {
	
	private final AlgeriaCitiesService algeriaCitiesService;
	
	public AlgeriaCitiesController(AlgeriaCitiesService algeriaCitiesService) {
		this.algeriaCitiesService = algeriaCitiesService;
	}
	
	@GetMapping("/public/algeriaCities/wilayas")
	public ResponseEntity<List<WilayaDto>> getWilayas(){
		List<WilayaDto> wilayas = algeriaCitiesService.getWilayas();
		
		return new ResponseEntity<List<WilayaDto>>(wilayas, HttpStatus.OK);
	}
	
	@GetMapping("/public/algeriaCities/wilayas/{wilayaCode}/communes")
	public ResponseEntity<List<CommuneDto>> getCommunes(@PathVariable String wilayaCode){
		List<CommuneDto> communes = algeriaCitiesService.getCommunes(wilayaCode);
		
		return new ResponseEntity<List<CommuneDto>>(communes, HttpStatus.OK);
	}

}
