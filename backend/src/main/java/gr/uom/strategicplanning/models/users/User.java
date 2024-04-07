package gr.uom.strategicplanning.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.uom.strategicplanning.models.domain.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;
    private String roles = "";
    private boolean verified = false;
    @ManyToOne
    private Organization organization;

    // User verification related fields
    private long verCodeExpiryTime = 300000; // 5 minutes
    private String verificationCode;
    private Date verificationIssuedDate= new Date();
    private Date verificationExpirationDate = new Date(System.currentTimeMillis() + verCodeExpiryTime);

    // Password reset related fields
    private long passResetExpiryTime = 300000; // 5 minutes
    private String passResetCode;
    private Date passResetIssuedDate = new Date();
    private Date passResetExpirationDate = new Date(System.currentTimeMillis() + passResetExpiryTime);

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean verificationCodeExpired() {
        return new Date().after(verificationExpirationDate);
    }

    public boolean verificationIsValid(String code) {
        return code.equals(verificationCode) && !verificationCodeExpired();
    }

    public boolean passResetCodeExpired() {
        return new Date().after(passResetExpirationDate);
    }

    public boolean passResetCodeIsValid(String code) {
        return code.equals(passResetCode) && !passResetCodeExpired();
    }

    public void setPassResetCode(String passResetCode) {
        this.passResetCode = passResetCode;
        this.passResetIssuedDate = new Date();
        this.passResetExpirationDate = new Date(System.currentTimeMillis() + passResetExpiryTime);
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        this.verificationIssuedDate = new Date();
        this.verificationExpirationDate = new Date(System.currentTimeMillis() + verCodeExpiryTime);
    }
}
