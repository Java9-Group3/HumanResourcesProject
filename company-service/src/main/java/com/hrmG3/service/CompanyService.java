package com.hrmG3.service;

import com.hrmG3.dto.request.AddCompanyRequestDto;
import com.hrmG3.exception.CompanyManagerException;
import com.hrmG3.exception.ErrorType;
import com.hrmG3.mapper.ICompanyMapper;
import com.hrmG3.repository.ICompanyRepository;
import com.hrmG3.repository.entity.Company;
import com.hrmG3.repository.enums.ERole;
import com.hrmG3.utility.JwtTokenManager;
import com.hrmG3.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class CompanyService extends ServiceManager<Company,Long> {
    private final ICompanyRepository companyRepository;
    private final JwtTokenManager jwtTokenManager;

    public CompanyService(ICompanyRepository companyRepository, JwtTokenManager jwtTokenManager) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public Boolean addCompany(String token,AddCompanyRequestDto dto) {
        List<String> roles = jwtTokenManager.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        if (roles.contains(ERole.ADMIN.toString())) {
            if (!companyRepository.existsByCompanyNameIgnoreCase(dto.getCompanyName())) {
                Company company = ICompanyMapper.INSTANCE.fromAddCompanyResponseDtoToCompany(dto);
                if(dto.getBase64Logo()!=null){
                    String encodedLogo = Base64.getEncoder().encodeToString(dto.getBase64Logo().getBytes());
                    company.setLogo(encodedLogo);
                }
                save(company);
                return true;
            }
            throw new CompanyManagerException(ErrorType.COMPANY_ALREADY_EXIST);
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }
}
