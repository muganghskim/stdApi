package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.*;
import com.stdApi.pacificOcean.repository.NoticeRepository;
import com.stdApi.pacificOcean.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final UserRepository userRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addNotice(String content, String noticeType, String title, String userEmail){
        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);

        if (!memberOpt.isPresent()) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        Notice notice = Notice.builder()
                .member(memberOpt.get())
                .content(content)
                .title(title)
                .noticeType(noticeType)
                .build();

        noticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public Page<NoticeDTO> findAll(Pageable pageable) {
        Page<Notice> page = noticeRepository.findAll(pageable);
        return page.map(entity -> {
            NoticeDTO dto = new NoticeDTO();
            dto.setNoticeId(entity.getNoticeId());
            dto.setContent(entity.getContent());
            dto.setNoticeType(entity.getNoticeType());
            dto.setTitle(entity.getTitle());
            dto.setUserName(entity.getMember().getUserName());
            dto.setUserImg(entity.getMember().getUserImg());
            dto.setCreatedAt(entity.getCreatedAt());
            return dto;
        });
    }
}
