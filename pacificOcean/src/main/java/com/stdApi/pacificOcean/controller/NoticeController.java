package com.stdApi.pacificOcean.controller;

import com.stdApi.pacificOcean.model.NoticeDTO;
import com.stdApi.pacificOcean.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@Api(value = "Notice Controller", description = "공지사항 관련된 API")
public class NoticeController {

    @Data
    public static class noticeReq {
        private String userEmail;
        private String title;
        private String content;
        private String noticeType;
    }

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }
    @GetMapping("/notice/all")
    @ApiOperation(value = "공지사항 전체 조회", notes = "공지사항을 전체조회합니다.")
    public Page<NoticeDTO> getAllNotice(Pageable pageable) {
        return noticeService.findAll(pageable);
    }

    @PostMapping("/admin/noticeAdd")
    @ApiOperation(value = "공지사항 추가", notes = "공지사항을 추가합니다.")
    public ResponseEntity<?> addNotice(@RequestBody noticeReq req) {
        try {
            noticeService.addNotice(req.getContent(), req.getNoticeType(), req.getTitle(), req.getUserEmail());
            return ResponseEntity.ok("200");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
