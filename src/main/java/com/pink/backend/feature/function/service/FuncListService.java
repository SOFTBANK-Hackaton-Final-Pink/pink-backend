package com.pink.backend.feature.function.service;

import com.pink.backend.feature.function.dao.FunctionRepository;
import com.pink.backend.feature.function.dto.FuncDetailRes;
import com.pink.backend.feature.function.dto.FuncListItemDto;
import com.pink.backend.feature.function.entity.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuncListService {

    private static final int PAGE_SIZE = 10;

    private final FunctionRepository functionRepository;

    public List<FuncListItemDto> getFunctionList(LocalDateTime cursor) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        List<Function> functions;

        if (cursor == null) {
            functions = functionRepository.findAllOrderByUpdatedAtDesc(pageable);
        } else {
            functions = functionRepository.findByCursorBefore(cursor, pageable);
        }

        return functions.stream()
                .map(FuncListItemDto::from)
                .collect(Collectors.toList());
    }

    public List<FuncDetailRes> getFunctionDetail(LocalDateTime cursor) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        
    }
}
