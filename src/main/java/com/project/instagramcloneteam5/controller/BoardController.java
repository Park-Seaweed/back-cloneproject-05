package com.project.instagramcloneteam5.controller;

import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.ExceptionResponseDto;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.dto.BoardGetResponseDto;
import com.project.instagramcloneteam5.model.dto.BoardRequestDto;
import com.project.instagramcloneteam5.model.dto.BoardUpdateResponseDto;
import com.project.instagramcloneteam5.service.BoardService;
import com.project.instagramcloneteam5.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final S3Service s3Service;

    // 게시글 전체 조회
    @GetMapping("/boards")
    public Map<String, List<BoardGetResponseDto>> getAllBoard(){ return boardService.getAllBoard();}

    // 메인 페이지 무한 스크롤
    @GetMapping("/boardScroll")
    public Map<String, List<BoardGetResponseDto>> getBoardSlice(
            @RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer size,
            @RequestParam(required=false) String sortBy ,
            @RequestParam(required=false) Boolean isAsc
    ) {
        if (isNotNullParam(page, size, sortBy, isAsc)) {
            page -= 1;
            return boardService.getAllBoardSlice(page, size, sortBy, isAsc);
        } else {
            throw new PrivateException(Code.PAGING_ERROR);
        }
    }

    private boolean isNotNullParam(Integer page, Integer size, String sortBy, Boolean isAsc) {
        return (page != null) && (size != null) && (sortBy != null) && (isAsc != null);
    }

    // 게시글 상세 조회
    @GetMapping("/boards/details/{boardId}")
    public ExceptionResponseDto getBoard(@PathVariable Long boardId) {
        BoardGetResponseDto boardGetResponseDto = boardService.getBoardOne(boardId);
        return new ExceptionResponseDto(Code.OK, boardGetResponseDto);
    }

    // 게시글 작성
    @PostMapping("/board/write")
    public ExceptionResponseDto uploadBoard(@RequestPart("content") BoardRequestDto boardRequestDto,
                                           @RequestPart("imgUrl") List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) {
            throw new PrivateException(Code.WRONG_INPUT_CONTENT);
        }
        List<String> imgPaths = s3Service.upload(multipartFiles);
        System.out.println("IMG 경로들 : " + imgPaths);
        boardService.uploadBoard(boardRequestDto, imgPaths);
        return new ExceptionResponseDto(Code.OK);

    }

    // 게시글 수정
    @PutMapping("/boards/details/{boardId}")
    public ExceptionResponseDto updateBoard(@PathVariable Long boardId,@RequestPart("content") BoardRequestDto boardRequestDto) {
        BoardUpdateResponseDto boardUpdateResponseDto = boardService.updateBoard(boardId, boardRequestDto);
        return new ExceptionResponseDto(Code.OK, boardUpdateResponseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/boards/details/{boardId}")
    public ExceptionResponseDto deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return new ExceptionResponseDto(Code.OK);
    }
}