package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.SupportDTO;
import com.stdApi.pacificOcean.service.SupportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Api(value = "Support Controller", description = "문의 관련된 API")
public class SupportController {

    private final SupportService supportService;

    @Autowired
    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }
    @GetMapping("/admin/supportAll")
    @ApiOperation(value = "관리자 문의 전체 조회", notes = "문의를 관리자가 전체조회합니다.")
    public Page<SupportDTO> getSupportAll(Pageable pageable) {
        return supportService.getSupportAll(pageable);
    }

    @GetMapping("/support/all")
    @ApiOperation(value = "유저 문의 전체 조회", notes = "유저가 문의를 전체조회합니다.")
    public List<SupportDTO> getUserSupportAll(@PathVariable String userEmail){
        return supportService.getSupportUser(userEmail);
    }

    @PostMapping("/support/add")
    @ApiOperation(value = "유저 문의 등록", notes = "유저가 문의를 등록합니다.")
    public ResponseEntity<?> createSupport(@RequestBody SupportDTO req){
        try {
            supportService.createSupport(req.getUserEmail(), req.getQuestion());
            return ResponseEntity.ok("200");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/admin/supportUpdate")
    @ApiOperation(value = "관리자 문의 답변", notes = "관리자가 문의를 답변합니다.")
    public ResponseEntity<?> updateSupport(@RequestBody SupportDTO req){
        try {
            supportService.updateSupport(req.getSupportId(), req.getAnswer());
            return ResponseEntity.ok("200");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
