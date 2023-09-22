package com.hrmG3.service;

import com.hrmG3.dto.request.RegisterManagerRequestDto;
import com.hrmG3.dto.request.RegisterVisitorRequestDto;
import com.hrmG3.exception.AuthManagerException;
import com.hrmG3.exception.ErrorType;
import com.hrmG3.manager.IUserProfileManager;
import com.hrmG3.mapper.IAuthMapper;
import com.hrmG3.rabbitmq.producer.RegisterMailHelloProducer;
import com.hrmG3.rabbitmq.producer.RegisterMailProducer;
import com.hrmG3.repository.IAuthRepository;
import com.hrmG3.repository.entity.Auth;
import com.hrmG3.repository.entity.ERole;
import com.hrmG3.repository.entity.EStatus;
import com.hrmG3.utility.JwtTokenProvider;
import com.hrmG3.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.hrmG3.utility.CodeGenerator.generateCode;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserProfileManager userManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegisterMailHelloProducer registerMailHelloProducer;

    private final RegisterMailProducer registerMailProducer;

    public AuthService(IAuthRepository repository, PasswordEncoder passwordEncoder, IUserProfileManager userManager, JwtTokenProvider jwtTokenProvider, RegisterMailHelloProducer registerMailHelloProducer, RegisterMailProducer registerMailProducer) {
        super(repository);
        this.authRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.userManager = userManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.registerMailHelloProducer = registerMailHelloProducer;
        this.registerMailProducer = registerMailProducer;
    }

    public Boolean registerVisitor(RegisterVisitorRequestDto dto){
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(dto.getEmail());
        if(!optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.DUPLICATE_USER);
        Auth auth = IAuthMapper.INSTANCE.fromVisitorsRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.VISITOR));
        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            auth.setStatus(EStatus.ACTIVE);
            save(auth);
            userManager.createVisitorUser(IAuthMapper.INSTANCE.fromAuthNewCreateVisitorUserRequestDto(auth));
            registerMailHelloProducer.sendHello(IAuthMapper.INSTANCE.fromAuthToRegisterMailHelloModel(auth));
            return true;
        }
        throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
    }
/*
passwordEncoder.encode(dto.getPassword()):
Bu parolayı, passwordEncoder adlı bir şifreleme mekanizması veya bileşen kullanarak şifreler.
Şifreleme işlemi, kullanıcının parolasını anlamlı bir metinden (düz metin) anlamsız bir metne (şifrelenmiş metin) dönüştürür.
Bu, kullanıcının parolasının güvende tutulmasına yardımcı olur.
Şifrelenmiş parola, potansiyel olarak zararlı kişilerin veya
sistem içerisindeki yetkisiz erişimlerin kullanıcı parolalarını görmesini veya çözmesini zorlaştırır.
* */

    public boolean registerManager(RegisterManagerRequestDto dto){
        Auth auth = IAuthMapper.INSTANCE.fromManagerRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.MANAGER,ERole.PERSONEL, ERole.FOUNDER));
        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setActivationCode(generateCode()); //maili cuma yaparız.
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            auth.setStatus(EStatus.ACTIVE);
            save(auth);
        }else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        return true;
    }

    public Boolean confirmUserAccount(String confirmationToken) {
        try {
            Long authId = jwtTokenProvider.getIdFromToken(confirmationToken).orElseThrow(() -> {
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            });
            Optional<Auth> auth = authRepository.findOptionalByAuthId(authId);
            if(auth.get().getStatus()==EStatus.INACTIVE)
                throw new AuthManagerException(ErrorType.INVALID_ACTION);
            auth.get().setStatus(EStatus.INACTIVE);
            update(auth.get());
            return true;
        }catch (Exception e){
            return false;
        }
    }



}
