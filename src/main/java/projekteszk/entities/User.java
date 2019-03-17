package projekteszk.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /*@JsonIgnore
    @OneToOne(mappedBy = "user")
    private Mohosz mohosz;*/
    
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Column(unique=true)
    @NotNull
    private String name;
        
    @Column(unique=true)
    @NotNull
    private String email;
    
    @Column
    @NotNull
    private String pass;
    
    @Column
    @NotNull
    private String phone;
    
    @Column
    @NotNull
    private String address;
    
    public enum Role {
        ROLE_QUEST, ROLE_USER, ROLE_ADMIN
    }
}
