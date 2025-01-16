/**
 * 
 */
package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.io.Serializable;

@Entity
@Table(name="user_join_group")
public class UserJoinGroup implements Serializable {

    private static final long serialVersionUID = -448692172698838315L;

    @EmbeddedId
    private UserJoinGroupId userJoinGroupId;
	@Version
	private Long version;

    public UserJoinGroupId getUserJoinGroupId() {
        return userJoinGroupId;
    }

    public void setUserJoinGroupId(UserJoinGroupId userJoinGroup) {
        this.userJoinGroupId = userJoinGroup;
    }

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
