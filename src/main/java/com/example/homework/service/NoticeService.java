package com.example.homework.service;

import com.example.homework.domain.Notice;
import com.example.homework.dto.NoticeDto;
import com.example.homework.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 공지사항 생성
    @Transactional
    public NoticeDto.Response createNotice(NoticeDto.Request request) {
        Notice notice = request.toEntity();
        return NoticeDto.Response.from(noticeRepository.save(notice));
    }

    // 모든 공지사항 조회 (페이징 처리)
    public Page<NoticeDto.Response> getAllNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable).map(NoticeDto.Response::from);
    }

    // 특정 공지사항 조회 및 조회수 증가
    @Transactional
    public NoticeDto.Response getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notice not found with id: " + id));
        notice.incrementViewCount(); // 조회수 증가
        return NoticeDto.Response.from(notice);
    }

    // 공지사항 업데이트
    @Transactional
    public NoticeDto.Response updateNotice(Long id, NoticeDto.Request request) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notice not found with id: " + id));
        notice.update(request.getTitle(), request.getContent());
        return NoticeDto.Response.from(noticeRepository.save(notice));
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new NoSuchElementException("Notice not found with id: " + id);
        }
        noticeRepository.deleteById(id);
    }
}
