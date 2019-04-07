package projekteszk.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Mohosz implements Serializable {
    
    @Id
    private Integer card_id;
    
    @Column(nullable = false)
    private Integer club_id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    @Temporal(TemporalType.DATE)
    private Date card_issue;
    
    @Column
    @Temporal(TemporalType.DATE)
    private Date card_expiration;
            
    @Column
    private Integer fishing_license_numb;
    
    @Column
    private Boolean is_disabled;
}
