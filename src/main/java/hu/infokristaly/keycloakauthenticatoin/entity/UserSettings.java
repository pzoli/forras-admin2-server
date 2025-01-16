package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public class UserSettings implements Serializable {

    public UserSettings() {
        
    }
            
    public UserSettings(Long userid, Object k, Object v) {
        id = new UserSettingsId(userid, k);
        this.value = (String)v;
    }

    private static final long serialVersionUID = -2872224372261912199L;

    @EmbeddedId
    private UserSettingsId id;
    
    @Basic
    private String value;

    public UserSettingsId getId() {
        return id;
    }
    
    public void setId(UserSettingsId id) {
        this.id = id;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    
}
