package com.hrmG3.Service;

import com.hrmG3.rabbitmq.producer.MailProducer;

public class AuthService {

    private final MailProducer mailProducer;

    public AuthService(MailProducer mailProducer) {
        this.mailProducer = mailProducer;
    }

    public RegisterResponseDto registerWithRabbitmq(RegisterRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Auth auth= IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.genarateCode());

        try {
            save(auth);
            // rabbit mq uzrerinden veri aktarcagız
            try {

            }catch (Exception e){
                e.printStackTrace();
            }

            mailProducer.sendMail(IAuthMapper.INSTANCE.toMailModel(auth));

            registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));

            return  IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        } catch (DataIntegrityViolationException e){
            throw  new AuthManagerException(ErrorType.USERNAME_EXIST);
        }catch (Exception ex){
            //   delete(auth);
            ex.printStackTrace();
            throw  new AuthManagerException(ErrorType.USER_NOT_CREATED);
        }
    }
}
