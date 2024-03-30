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
}
