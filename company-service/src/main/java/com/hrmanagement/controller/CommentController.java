package com.hrmanagement.controller;

import com.hrmanagement.dto.request.ChangeCommentStatusRequestDto;
import com.hrmanagement.dto.request.PersonelCommentRequestDto;
import com.hrmanagement.dto.response.PersonnelActiveCompanyCommentsResponseDto;
import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.service.CommentService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hrmanagement.constants.EndPoints.COMMENT;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(COMMENT)
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/personel-make-comment/{token}")
    public ResponseEntity<Boolean> personnelMakeComment(@PathVariable String token, @RequestBody PersonelCommentRequestDto dto){
        return ResponseEntity.ok(commentService.personelMakeComment(token,dto));
    }

    @PutMapping("/change-comment-status/{token}")
    public ResponseEntity<Boolean> changeCommentStatus(@PathVariable String token, @RequestBody ChangeCommentStatusRequestDto dto){
        return ResponseEntity.ok(commentService.changeCommentStatus(token,dto));
    }
    @Hidden
    @GetMapping("find-all-active-company-comments/{token}")
    public ResponseEntity<List<PersonnelActiveCompanyCommentsResponseDto>> findAllActiveCompanyComments(@PathVariable String token){
        return ResponseEntity.ok(commentService.findAllActiveCompanyComments(token));
    }

    @GetMapping("/pending-comments")
    public List<Comment> getPendingComments() {
        return commentService.findCommentByStatus();
    }

}
