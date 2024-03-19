package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.InvenDTO;
import com.stdApi.pacificOcean.model.ProfitandLossDTO;
import com.stdApi.pacificOcean.service.InvenService;
import com.stdApi.pacificOcean.service.ProfitAndLossService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
@Api(value = "Inven Controller", description = "재고 관련된 API")
public class InvenController {

    private final InvenService invenService;

    @Autowired
    public InvenController(InvenService invenService) {
        this.invenService = invenService;
    }
    @GetMapping("/inven/all")
    @ApiOperation(value = "재고 전체 조회", notes = "재고를 전체조회합니다.")
    public Page<InvenDTO> getAllInven(Pageable pageable) {
        return invenService.findAll(pageable);
    }
}
