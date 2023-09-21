package com.hrmG3.service;

import com.hrmG3.exception.ErrorType;
import com.hrmG3.rabbitmq.producer.MailProducer;
import com.hrmG3.utility.ServiceManager;
import com.hrmG3.dto.request.RegisterVisitorRequestDto;
import com.hrmG3.exception.AuthManagerException;
import com.hrmG3.manager.IUserProfileManager;
import com.hrmG3.mapper.IAuthMapper;
import com.hrmG3.repository.IAuthRepository;
import com.hrmG3.repository.entity.Auth;
import com.hrmG3.repository.enums.EStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserProfileManager userManager;
    private final MailProducer mailProducer;


    public AuthService(IAuthRepository repository, PasswordEncoder passwordEncoder, IUserProfileManager userManager, MailProducer mailProducer) {
        super(repository);
        this.authRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.userManager = userManager;
        this.mailProducer = mailProducer;
    }

    public Boolean registerVisitor(RegisterVisitorRequestDto dto){
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(dto.getEmail());
        if(!optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.DUPLICATE_USER);

        Auth auth = IAuthMapper.INSTANCE.fromVisitorsRequestDtoToAuth(dto);
        mailProducer.sendMail(IAuthMapper.INSTANCE.toMailModel(auth));
        if (dto.getPassword().equals(dto.getRepassword())){ //emre123= asdasdasgbdkjh123
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            //mail işlemi onay
            auth.setStatus(EStatus.ACTIVE);
            save(auth);
            userManager.createVisitorUser(IAuthMapper.INSTANCE.fromAuthNewCreateVisitorUserRequestDto(auth));
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

}
