package com.hrmanagement.service;

import com.hrmanagement.dto.request.CompanyNameAndWageDateRequestDto;
import com.hrmanagement.dto.request.CompanyUpdateRequestDto;
import com.hrmanagement.dto.request.FindPendingCommentWithCompanyName;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.IUserManager;
import com.hrmanagement.mapper.ICompanyMapper;
import com.hrmanagement.repository.ICompanyRepository;
import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.repository.entity.Company;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyService extends ServiceManager<Company, Long> {
    private final ICompanyRepository companyRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;
    private final CommentService commentService;

    private CompanyService(ICompanyRepository companyRepository,
                           JwtTokenProvider jwtTokenProvider,
                           IUserManager userManager,
                           CommentService commentService) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
        this.commentService = commentService;
    }

    public Long save(SaveCompanyRequestDto dto) {
        if (!companyRepository.existsByCompanyNameIgnoreCase(dto.getCompanyName()) && !companyRepository.existsByTaxNumber(dto.getTaxNumber())) {
            Company company = ICompanyMapper.INSTANCE.fromSaveCompanyResponseDtoToCompany(dto);
//            if (dto.getBase64Logo() != null) {
//                String encodedLogo = Base64.getEncoder().encodeToString(dto.getBase64Logo().getBytes());
//                company.setLogo(encodedLogo);
//                company.setSubscriptionExpirationDate(2023L);
//            }
            return save(company).getCompanyId();
        }
        throw new CompanyManagerException(ErrorType.COMPANY_ALREADY_EXIST);
    }
    public Long updateCompany(CompanyUpdateRequestDto dto) {
        Long companyId = jwtTokenProvider.getCompanyIdFromToken(dto.getToken())
                .orElseThrow(() -> new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND));
        Optional<Company> companyProfile = companyRepository.findById(companyId);
        List<String> roles = jwtTokenProvider.getRoleFromToken(dto.getToken());
        if (roles.isEmpty()) {
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (roles.contains(ERole.MANAGER.toString())||roles.contains(ERole.FOUNDER.toString())) {
            if (companyProfile.isPresent()) {
                Company company = companyProfile.get();
                company.setCompanyMail(dto.getCompanyMail());
                company.setCompanyPhone(dto.getCompanyPhone());
                company.setCompanyCountry(dto.getCompanyCountry());
                company.setCompanyNeighbourhood(dto.getCompanyNeighbourhood());
                company.setCompanyDistrict(dto.getCompanyDistrict());
                company.setCompanyBuildingNumber(dto.getCompanyBuildingNumber());
                company.setCompanyApartmentNumber(dto.getCompanyApartmentNumber());
                company.setSector(dto.getSector());
                company.setCompanyProvince(dto.getCompanyProvince());
                return update(company).getCompanyId();
            } else {
                throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            throw new CompanyManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
    }

    public CompanyInformationResponseDto showCompanyInformation(String token) {
        Company company = findById(jwtTokenProvider.getCompanyIdFromToken(token).get()).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        return ICompanyMapper.INSTANCE.fromCompanyToCompanyInformationResponseDto(company);
    }

    //Tüm companylerin preview bilgileri için metot
    public List<VisitorCompanyInformations> findAllCompanyPreviewInformation() {
        List<Company> companyList = companyRepository.findAll();
        List<VisitorCompanyInformations> companyInformationsList = new ArrayList<>();
        companyList.forEach(company -> {
            if (company.getSubscriptionExpirationDate() != null) {
                Long currentTime = System.currentTimeMillis();
                Date currentDate = new Date(currentTime);
                Date expirationDate = new Date(company.getSubscriptionExpirationDate());
                System.out.println(currentDate);
                System.out.println(expirationDate);
                if (currentDate.after(expirationDate)) {
                    VisitorCompanyInformations dto = ICompanyMapper.INSTANCE.fromCompanyToVisitorCompanyInformations(company);
                    if (company.getLogo() != null) {
                        try {
                            byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                            String decodedLogo = new String(decodedBytes);
                            dto.setLogo(decodedLogo);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    companyInformationsList.add(dto);
                }
            }
        });
        return companyInformationsList;

    }

    //Detaylı company sayfası için metot
    public VisitorDetailedCompanyInformationResponse findCompanyDetailedInformation(Long companyId) {
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        VisitorDetailedCompanyInformationResponse dto = ICompanyMapper.INSTANCE.fromCompanyToVisitorDetailedCompanyInformationResponse(company);
        if (company.getLogo() != null) {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                String decodedLogo = new String(decodedBytes);
                dto.setLogo(decodedLogo);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        List<FindCompanyCommentsResponseDto> dtoCommentList = commentService.findCompanyComments(companyId);
        dto.setCompanyComments(dtoCommentList);
        return dto;
    }


    public PersonnelCompanyInformationResponseDto getPersonnelCompanyInformation(Long companyId) {
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        PersonnelCompanyInformationResponseDto dto = ICompanyMapper.INSTANCE.fromCompanyToPersonnelCompanyInformationResponseDto(company);
        if (company.getLogo() != null) {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                String decodedLogo = new String(decodedBytes);
                dto.setLogo(decodedLogo);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return dto;
    }

    public Boolean doesCompanyIdExist(Long companyId) {
        if (companyRepository.existsByCompanyId(companyId)) {
            return true;
        }
        return false;
    }

//    public Long doesCompanyIdExist(Long companyId) {
//        Optional<Long> companyExistence = companyRepository.existsByCompanyId(companyId);
//        if (companyExistence.isEmpty()){
//            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
//        }
//        return companyExistence.get();
//    }

    // Verilen bir JWT (JSON Web Token) kimlik doğrulama belgesinden kullanıcı rollerini alır.
    public List<FindPendingCommentWithCompanyName> findCommentWithCompanyNameByStatus(String token) {
        List<String> userRoles = jwtTokenProvider.getRoleFromToken(token);
        // Kullanıcı admin rolüne sahipse:
        if (userRoles.contains(ERole.ADMIN.toString())) {
            // Yorum servisi aracılığıyla belirli bir durumda olan yorumları alır.
            List<Comment> commentList = commentService.findCommentByStatus();
            // Her yorum için aşağıdaki işlemleri yapar:
            List<FindPendingCommentWithCompanyName> pendingComment = commentList.stream()
                    .map(comment -> {
                        // Yorumun bağlı olduğu şirketi bulur veya istisna fırlatır.
                        Company company = findById(comment.getCompanyId())
                                .orElseThrow(() -> new RuntimeException("Şirket bulunamadı"));
                        // Yorumun kullanıcısının avatarını alır.
                        String userAvatar = String.valueOf(userManager.getUserAvatarByUserId(comment.getUserId()).getBody());
                        // Yorumu ve ilgili bilgileri içeren bir nesne oluşturur.
                        FindPendingCommentWithCompanyName pending = FindPendingCommentWithCompanyName.builder()
                                .commentId(comment.getCommentId())
                                .avatar(userAvatar)
                                .companyName(company.getCompanyName())
                                .eCommentStatus(comment.getECommentStatus())
                                .comment(comment.getComment())
                                .name(comment.getName())
                                .surname(comment.getSurname())
                                .build();
                        return pending;
                    })
                    .collect(Collectors.toList());
            // İşlenmiş yorumları döndürür.
            return pendingComment;
        }// Kullanıcı admin rolüne sahip değilse, yetkilendirme hatası fırlatılır.
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }       //


    public String getCompanyNameWithCompanyId(Long companyId) {
        Optional<Company> optionalCompany = findById(companyId);
        if (optionalCompany.isEmpty())
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        return optionalCompany.get().getCompanyName();
    }

    public PersonnelDashboardResponseDto getPersonnelDashboardInformation(String token) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        });
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        if (roles.contains(ERole.PERSONEL.toString())) {
            UserProfilePersonnelDashboardResponseDto userDto = userManager.getUserProfilePersonnelDashboardInformation(authId).getBody();
            PersonnelDashboardResponseDto personnelDto = ICompanyMapper.INSTANCE.fromUserProfilePersonnelDashboardResponseDtoToPersonnelDashboardResponseDto(userDto);
            Company company = findById(userDto.getCompanyId()).orElseThrow(() -> {
                throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
            });
            personnelDto.setCompanyName(company.getCompanyName());
            if (company.getLogo() != null) {
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                    String decodedLogo = new String(decodedBytes);
                    personnelDto.setLogo(decodedLogo);
                    //WageDate
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate formattedWageDate = LocalDate.parse(company.getWageDate(), formatter);
                    LocalDate nowDate = LocalDate.now();
                    if (formattedWageDate.isBefore(nowDate)) {
                        LocalDate newDate = formattedWageDate.plusDays(30);
                        String newWageDate = newDate.format(formatter);
                        company.setWageDate(newWageDate);
                        update(company);
                    }
                    personnelDto.setWageDate(company.getWageDate());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            personnelDto.setSector(company.getSector());
            personnelDto.setHolidayDates(company.getHolidayDates());
            return personnelDto;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public ManagerDashboardResponseDto getManagerDashboardInformation(String token) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        });
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        if (roles.contains(ERole.MANAGER.toString())) {
            UserProfileManagerDashboardResponseDto dtoUser = userManager.getUserProfileManagerDashboard(authId).getBody();
            Company company = findById(dtoUser.getCompanyId()).orElseThrow(() -> {
                throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
            });
            ManagerDashboardResponseDto managerDto = ICompanyMapper.INSTANCE.fromCompanyToManagerDashboardResponseDto(company);
            managerDto.setCompanyPersonnelCount(dtoUser.getCompanyPersonnelCount());
            return managerDto;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public AllCompanyInfosForUserProfileResponseDto getAllInfosCompanyWithCompanyId(Long companyId) {
        Optional<Company> companyInfos = findById(companyId);
        if (companyInfos.isEmpty())
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        AllCompanyInfosForUserProfileResponseDto dto = ICompanyMapper.INSTANCE.fromCompanyToAllCompanyInfosForUserProfileResponseDto(companyInfos.get());
        return dto;
    }


    public CompanyNameAndWageDateRequestDto getCompanyNameAndWageDateResponseDto(Long companyId) {
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        try {
            Date wageDate = new SimpleDateFormat("dd-MM-yyyy").parse(company.getWageDate());
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            if (wageDate.before(date)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(wageDate);
                Calendar millisCalendar = Calendar.getInstance();
                millisCalendar.setTimeInMillis(millis);
                calendar.set(Calendar.MONTH, millisCalendar.get(Calendar.MONTH));
                calendar.set(Calendar.YEAR, millisCalendar.get(Calendar.YEAR));
                wageDate = calendar.getTime();
            }
            String formattedDate = dateFormat.format(wageDate);
            if (!formattedDate.equals(company.getWageDate())) {
                company.setWageDate(formattedDate);
                update(company);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return ICompanyMapper.INSTANCE.fromCompanyToCompanyNameAndWageDateRequestDto(company);
    }

    public Boolean subscribeCompany(SubscribeCompanyResponseDto dto) {
        Company company = findById(dto.getCompanyId()).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        LocalDate currentDate = LocalDate.now();
        LocalDate newDate = currentDate.plusDays(dto.getCompanySubscribeDay());
        Date date = Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Long subscriptionExpirationDate = date.getTime();
        company.setSubscriptionExpirationDate(subscriptionExpirationDate);
        update(company);
        return true;
    }


    public Boolean doesCompanySubscriptionExist(Long companyId) {
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        if (company.getSubscriptionExpirationDate() == null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean updateCompanyWageDate(UpdateCompanyWageDateResponseDto dto) {
        Company company = findById(dto.getCompanyId()).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        company.setWageDate(dto.getWageDate());
        update(company);
        return true;
    }

    public Company findCompanyByCompanyName(String companyName) {
        Optional<Company> optionalCompany = companyRepository.findByCompanyName(companyName);
        if (optionalCompany.isEmpty()) {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        }
        return optionalCompany.get();
    }

    @PostConstruct
    public void defaultCompany(){
        save(Company.builder()
                .companyName("A Şirketi")
                .companyCountry("Türkiye")
                .companyProvince("Ankara")
                .companyDistrict("Çankaya")
                .companyNeighbourhood("Çukurambar")
                .companyMail("asirketi@asirketi.com")
                .companyPhone("000-0-000")
                .companyPostalCode(6)
                .taxNumber("000-000-001")
                .companyBalanceStatus(500000000D)
                .companyApartmentNumber(17)
                .companyBuildingNumber(32)
                .sector("Construction")
                .build());
        save(Company.builder()
                .companyName("B Şirketi")
                .companyCountry("Türkiye")
                .companyProvince("Antalya")
                .companyDistrict("Muratpaşa")
                .companyNeighbourhood("Teomanpaşa")
                .companyMail("bsirketi@bsirketi.com")
                .companyPhone("000-0-000")
                .companyPostalCode(6)
                .taxNumber("000-000-002")
                .companyBalanceStatus(377000000D)
                .companyApartmentNumber(57)
                .companyBuildingNumber(9)
                .sector("Software")
                .build());
    }

    public Optional<Company> getHolidayDatesByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }
}
