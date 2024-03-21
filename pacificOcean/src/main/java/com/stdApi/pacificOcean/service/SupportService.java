package com.stdApi.pacificOcean.service;

import com.stdApi.pacificOcean.model.InvenDTO;
import com.stdApi.pacificOcean.model.Member;
import com.stdApi.pacificOcean.model.Support;
import com.stdApi.pacificOcean.model.SupportDTO;
import com.stdApi.pacificOcean.repository.SupportRepository;
import com.stdApi.pacificOcean.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SupportService {

    private final SupportRepository supportRepository;

    private final UserRepository userRepository;

    @Autowired
    public SupportService(SupportRepository supportRepository, UserRepository userRepository) {
        this.supportRepository = supportRepository;
        this.userRepository = userRepository;
    }

    // C
    public void createSupport(String userEmail, String question){

        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);

        if(!memberOpt.isPresent()){
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        Support support = Support.builder()
                .member(memberOpt.get())
                .question(question)
                .stat("대기")
                .build();

        supportRepository.save(support);

    }

    // R
    @Transactional(readOnly = true)
    public Page<SupportDTO> getSupportAll(Pageable pageable){

        Page<Support> supports = supportRepository.findAll(pageable);

            return supports.map(entity -> {
                SupportDTO dto = new SupportDTO();
                dto.setSupportId(entity.getSupportId());
                dto.setQuestion(entity.getQuestion());
                dto.setAnswer(entity.getAnswer());
                dto.setUserEmail(entity.getMember().getUserEmail());
                dto.setStat(entity.getStat());
                dto.setCreatedAt(entity.getCreatedAt());
                dto.setUpdatedAt(entity.getUpdatedAt());
                return dto;
            });
    }

    @Transactional(readOnly = true)
    public List<SupportDTO> getSupportUser(String userEmail){

        Optional<Member> memberOpt = userRepository.findByUserEmail(userEmail);

        if(!memberOpt.isPresent()){
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }

        List<Support> supports = supportRepository.findByMember(memberOpt.get());

        return supports.stream().map(entity -> {
            SupportDTO dto = new SupportDTO();
            dto.setSupportId(entity.getSupportId());
            dto.setQuestion(entity.getQuestion());
            dto.setAnswer(entity.getAnswer());
            dto.setStat(entity.getStat());
            dto.setUpdatedAt(entity.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());

    }

    // U
    @Transactional
    public void updateSupport(Long supportId, String answer){
        Optional<Support> supportOpt = supportRepository.findById(supportId);

        if(!supportOpt.isPresent()){
            throw new RuntimeException("문의가 없습니다.");
        }

        Support support = supportOpt.get();

        support.setAnswer(answer);
        support.setStat("답변완료");

        supportRepository.save(support);

    }

    // D
}
