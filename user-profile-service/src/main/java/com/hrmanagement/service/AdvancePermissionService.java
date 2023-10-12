package com.hrmanagement.service;

import com.hrmanagement.dto.request.ChangeAdvanceStatusRequestDto;
import com.hrmanagement.dto.request.CreateAdvancePermissionRequestDto;
import com.hrmanagement.dto.response.CreateAdvancePermissionResponseDto;
import com.hrmanagement.dto.response.ListAdvanceResponseDto;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.exception.UserProfileManagerException;
import com.hrmanagement.mapper.IAdvanceMapper;
import com.hrmanagement.repository.IAdvancePermissionRepository;
import com.hrmanagement.repository.entity.AdvancePermission;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.repository.entity.enums.ApprovalStatus;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdvancePermissionService extends ServiceManager<AdvancePermission,Long> {

    private final IAdvancePermissionRepository repository;
    private final UserProfileService  userProfileService;

    private final JwtTokenProvider jwtTokenProvider;

    public AdvancePermissionService(IAdvancePermissionRepository repository, UserProfileService userProfileService, JwtTokenProvider jwtTokenProvider) {
        super(repository);
        this.repository = repository;
        this.userProfileService = userProfileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Double wageCheck(Long authid){
        Optional<Double> miktar=repository.findOptionalSumAmountByAuthid(authid);
        if (miktar.isEmpty()) {
            return 0.0;
        }
        return miktar.get();
    }
    public CreateAdvancePermissionResponseDto createAdvance (String token, CreateAdvancePermissionRequestDto dto) {
        AdvancePermission permission= IAdvanceMapper.INSTANCE.toAdvance(dto);
        Optional<Long> authIdOptional = jwtTokenProvider.getAuthIdFromToken(token);
        if (authIdOptional.isPresent()) {
            permission.setAuthid(authIdOptional.get());
        } else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        UserProfile userProfile=userProfileService.findByAuthId(permission.getAuthid());
        permission.setCompanyId(userProfile.getCompanyId());
        System.out.println("girilen miktar"+permission.getAmount());
        System.out.println("kişinin maaşı"+userProfile.getWage());
        if (wageCheck(permission.getAuthid())+permission.getAmount()>userProfile.getWage()) {
            System.out.println(wageCheck(permission.getAuthid())+permission.getAmount());
            throw new UserProfileManagerException(ErrorType.ADVANCEREQUEST_BIGGER_THAN_WAGE);
        }
        permission.setName(userProfile.getName());
        save(permission);
        return new CreateAdvancePermissionResponseDto("Avans talebiniz başarıyla kaydedildi");
    }

    public List<ListAdvanceResponseDto> getAllPendingAdvanceByCompanyId(String token) {
        Optional<List<AdvancePermission>> advences=repository.findByCompanyId(jwtTokenProvider.getCompanyIdFromToken(token).get());
        List<ListAdvanceResponseDto> dtoAdvances = new ArrayList<>();
        for (AdvancePermission advance : advences.get()) {
            if (advance.getApprovalStatus().equals(ApprovalStatus.PENDING)){
                ListAdvanceResponseDto dto = IAdvanceMapper.INSTANCE.toDto(advance);
                dtoAdvances.add(dto);
            }
        }
        return dtoAdvances;
    }


    public List<ListAdvanceResponseDto> getAllAdvancesByCompanyId(String token) {
        Optional<List<AdvancePermission>> advences=repository.findByCompanyId(jwtTokenProvider.getCompanyIdFromToken(token).get());
        List<ListAdvanceResponseDto> dtoAdvances = new ArrayList<>();
        for (AdvancePermission advance : advences.get()) {
            ListAdvanceResponseDto dto = IAdvanceMapper.INSTANCE.toDto(advance);
            dtoAdvances.add(dto);
        }
        return dtoAdvances;
    }



    public Boolean changeAdvanceStatus(String token, ChangeAdvanceStatusRequestDto dto) {
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);

        if (roles.contains(ERole.MANAGER.toString())) {
            AdvancePermission advenceid=findById(dto.getAdvanceId()).orElseThrow(() -> {
                throw new UserProfileManagerException(ErrorType.ADVANCE_NOT_FOUND);
            });
            if (advenceid.getApprovalStatus()== ApprovalStatus.PENDING){
                if (dto.getAction()){
                    advenceid.setApprovalStatus(ApprovalStatus.APPROVED);
                }else {
                    advenceid.setApprovalStatus(ApprovalStatus.REJECTED);
                }
                update(advenceid);
                return true;
            }
        }
        throw new UserProfileManagerException(ErrorType.NOT_MANAGER);
    }

    public List<ListAdvanceResponseDto> getAllAdvance(){
        List<AdvancePermission> advences=repository.findAll();
        List<ListAdvanceResponseDto> dtoAdvances = new ArrayList<>();

        for (AdvancePermission advance : advences) {
            ListAdvanceResponseDto dto = IAdvanceMapper.INSTANCE.toDto(advance);
            dtoAdvances.add(dto);
        }
        return dtoAdvances;
    }





}
