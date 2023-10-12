package com.hrmanagement.service;

import com.hrmanagement.dto.request.ChangeCommentStatusRequestDto;
import com.hrmanagement.dto.request.PersonelCommentRequestDto;
import com.hrmanagement.dto.response.FindCompanyCommentsResponseDto;
import com.hrmanagement.dto.response.PersonnelActiveCompanyCommentsResponseDto;
import com.hrmanagement.dto.response.PersonnelDashboardCommentResponseDto;
import com.hrmanagement.dto.response.UserProfileCommentResponseDto;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.IUserManager;
import com.hrmanagement.mapper.ICommentMapper;
import com.hrmanagement.repository.ICommentRepository;
import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.repository.entity.enums.ECommentStatus;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService extends ServiceManager<Comment, Long> {
    private final ICommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;
    private final PasswordEncoder passwordEncoder;

    public CommentService(ICommentRepository commentRepository, JwtTokenProvider jwtTokenProvider, IUserManager userManager, PasswordEncoder passwordEncoder) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean personelMakeComment(String token, PersonelCommentRequestDto dto) {
        Long authId = jwtTokenProvider.getAuthIdFromToken(token).orElseThrow(() -> new CompanyManagerException(ErrorType.USER_NOT_FOUND));
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.BAD_REQUEST);
        if (roles.contains(ERole.PERSONEL.toString())) {
            UserProfileCommentResponseDto userProfileCommentResponseDto = userManager.getUserProfileCommentInformation(authId).getBody();
            Comment comment = ICommentMapper.INSTANCE.fromUserProfileCommentResponseDtoToComment(userProfileCommentResponseDto);
            comment.setComment(dto.getComment());
            save(comment);
            return true;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public List<FindCompanyCommentsResponseDto> findCompanyComments(Long companyId) {
        List<Comment> commentList = commentRepository.findByCompanyId(companyId);
        List<FindCompanyCommentsResponseDto> companyComments = commentList.stream().filter(y ->
                y.getECommentStatus() == ECommentStatus.ACTIVE).map(x ->
                ICommentMapper.INSTANCE.fromCompanyToFindCompanyCommentsResponseDto(x)).collect(Collectors.toList());
        return companyComments;
    }

    public Boolean changeCommentStatus(String token, ChangeCommentStatusRequestDto dto) {
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        if (roles.contains(ERole.ADMIN.toString())) {
            Comment commentId = findById(dto.getCommentId()).orElseThrow(() -> {
                throw new CompanyManagerException(ErrorType.COMMENT_NOT_FOUND);
            });
            if (commentId.getECommentStatus() == ECommentStatus.PENDING) {
                if (dto.getAction()) {
                    commentId.setECommentStatus(ECommentStatus.ACTIVE);
                } else {
                    commentId.setECommentStatus(ECommentStatus.DELETED);
                }
                update(commentId);
                return true;
            }
            throw new CompanyManagerException(ErrorType.COMMENT_NOT_PENDING);
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public List<Comment> findCommentByStatus() {
        List<Comment> commentList = commentRepository.findAll();
        List<Comment> pendingComment = new ArrayList<>();
        commentList.forEach(x -> {
            if (x.getECommentStatus() == ECommentStatus.PENDING) {
                pendingComment.add(x);
            }
        });
        return pendingComment;
    }

    public List<PersonnelActiveCompanyCommentsResponseDto> findAllActiveCompanyComments(String token){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        if(roles.contains(ERole.PERSONEL.toString())) {
            PersonnelDashboardCommentResponseDto dto = userManager.findAllActiveCompanyComments(authId).getBody();
            List<Comment> commentList = commentRepository.findByCompanyId(dto.getCompanyId());
            System.out.println(commentList);
            List<Comment> filteredComments= new ArrayList<>();
                    commentList.stream().forEach(x -> {
                if(x.getECommentStatus() == ECommentStatus.ACTIVE)
                    filteredComments.add(x);
            });
            List<PersonnelActiveCompanyCommentsResponseDto> activeCompanyCommentsResponseDtos =
                    filteredComments.stream().map(comment -> {
                        PersonnelActiveCompanyCommentsResponseDto dto1 = ICommentMapper.INSTANCE.fromCommentToPersonnelActiveCompanyCommentsResponseDto(comment);
                        String avatar = String.valueOf(userManager.getUserAvatarByUserId(comment.getUserId()).getBody());
                        dto1.setAvatar(avatar);
                        return dto1;
                    }).collect(Collectors.toList());
            System.out.println(activeCompanyCommentsResponseDtos);
            return activeCompanyCommentsResponseDtos;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }


}
