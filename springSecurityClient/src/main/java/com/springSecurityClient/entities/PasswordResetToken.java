package com.springSecurityClient.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {

    //expiration time 10 min
    private static final int EXPIRATION_TIME=10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private User user;

    public PasswordResetToken(User user, String token){
        super();
        this.user = user;
        this.token = token;
        this.expirationTime = CalculateExpirationDate(EXPIRATION_TIME);
    }

    public PasswordResetToken(String token){
        super();
        this.token = token;
        this.expirationTime = CalculateExpirationDate(EXPIRATION_TIME);
    }

    private Date CalculateExpirationDate(int expirationTime) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(new Date().getTime());
        rightNow.add(Calendar.MINUTE, expirationTime);
        return new Date(rightNow.getTime().getTime());
    }
}
