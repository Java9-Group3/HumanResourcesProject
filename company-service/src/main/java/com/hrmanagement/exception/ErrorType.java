package com.hrmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR(5100, "Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4000, "Parametre Hatası", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4600,"Token hatası" ,  HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4400, "Böyle bir kullanıcı bulunamadı", HttpStatus.NOT_FOUND),
    NO_AUTHORIZATION(4900,"Yetkisiz giriş denemesi",HttpStatus.BAD_REQUEST),
    COMPANY_NOT_FOUND(5100, "Böyle bir şirket bulunamadı", HttpStatus.NOT_FOUND),
    COMPANY_ALREADY_EXIST(5200,"Bu şirket veritabanında zaten kayıtlı", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND(5300,"Bu şirkete ait bir yorum bulunamadı.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_PENDING(5400,"Comment Pending durumda değildir.", HttpStatus.BAD_REQUEST),
    AUTHORIZATION_ERROR(4200,"You're not authorized to do this.", HttpStatus.BAD_REQUEST),
    NO_EXPENSE_EXIST(5500,"Bu şirkete ait bir expense bulunmamaktadır.",HttpStatus.NOT_FOUND),
    EXPENSE_NOT_PENDING(5600,"İlgili expense'in durumu pending değildir.",HttpStatus.BAD_REQUEST);






    private int code;
    private String message;
    HttpStatus httpStatus;
}
