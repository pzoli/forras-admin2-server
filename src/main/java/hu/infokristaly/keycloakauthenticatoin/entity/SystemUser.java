package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "systemuser", schema = "public")
public class SystemUser implements java.io.Serializable {

	private static final long serialVersionUID = 4103231713043595259L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "osusername", length = 128)
	private String osUserName;

	@ManyToOne
	@JoinColumn(name = "organizationunit")
	private Organizationunit organizationunit;

	@Basic
	@Column(name = "username")
	private String userName;

	@Column(name = "vcard")
	@Lob
	private String vCard;

	@Column(name = "photo")
	@Lob
	private byte[] photo;

	@Column(name = "sqlserverloginname", length = 50, unique = true)
	private String sqlserverloginname;

	@ManyToOne
	@JoinColumn(name = "usergroup")
	private Usergroup usergroup;

	@Column(name = "osuserpassword")
	private String osUserPassword;

	@Column(name = "enabled")
	private Boolean enabled;

	private String comment;

	@ManyToOne
	@JoinColumn(name = "language")
	private Language language;

	@Basic
    @Column(name="emailaddress")
    private String emailAddress;
    @Column(name="casemanager")
    @Basic
    private Boolean caseManager = false;
    @Basic
    @Column(name="adminuser")
    private boolean adminUser = false;
    @Basic
    @Column(name="pincode")
    private String pinCode;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deleteddate")
    private Date deletedDate;
	@Version
	private Long version;

	public SystemUser() {
	}

	public SystemUser(Long id) {
		this.id = id;
	}

	public SystemUser(Long id, String userName, String osUserName, Organizationunit organizationunit, String vCard, byte[] photo, String sqlserverloginname, Usergroup usergroup) {
		this.id = id;
		this.userName = userName;
		this.osUserName = osUserName;
		this.organizationunit = organizationunit;
		this.vCard = vCard;
		this.photo = photo;
		this.sqlserverloginname = sqlserverloginname;
		this.usergroup = usergroup;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOsUserName() {
		return this.osUserName;
	}

	public void setOsUserName(String osusername) {
		this.osUserName = osusername;
	}

	public Organizationunit getOrganizationunit() {
		return this.organizationunit;
	}

	public void setOrganizationunit(Organizationunit organizationunit) {
		this.organizationunit = organizationunit;
	}

	public String getVCard() {
		return this.vCard;
	}

	public void setVCard(String vCard) {
		this.vCard = vCard;
	}

	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getSqlserverloginname() {
		return this.sqlserverloginname;
	}

	public void setSqlserverloginname(String sqlserverloginname) {
		this.sqlserverloginname = sqlserverloginname;
	}

	public Usergroup getUsergroup() {
		return this.usergroup;
	}

	public void setUsergroup(Usergroup usergroup) {
		this.usergroup = usergroup;
	}

	public String getOsUserPassword() {
		return osUserPassword;
	}

	public void setOsUserPassword(String osUserPassword) {
		this.osUserPassword = osUserPassword;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Boolean getCaseManager() {
		return caseManager;
	}

	public void setCaseManager(Boolean caseManager) {
		this.caseManager = caseManager;
	}

	public boolean getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((organizationunit == null) ? 0 : organizationunit.hashCode());
		result = prime * result + ((osUserName == null) ? 0 : osUserName.hashCode());
		result = prime * result + ((osUserPassword == null) ? 0 : osUserPassword.hashCode());
		result = prime * result + Arrays.hashCode(photo);
		result = prime * result + ((sqlserverloginname == null) ? 0 : sqlserverloginname.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((usergroup == null) ? 0 : usergroup.hashCode());
		result = prime * result + ((vCard == null) ? 0 : vCard.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemUser other = (SystemUser) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (organizationunit == null) {
			if (other.organizationunit != null)
				return false;
		} else if (!organizationunit.equals(other.organizationunit))
			return false;
		if (osUserName == null) {
			if (other.osUserName != null)
				return false;
		} else if (!osUserName.equals(other.osUserName))
			return false;
		if (osUserPassword == null) {
			if (other.osUserPassword != null)
				return false;
		} else if (!osUserPassword.equals(other.osUserPassword))
			return false;
		if (!Arrays.equals(photo, other.photo))
			return false;
		if (sqlserverloginname == null) {
			if (other.sqlserverloginname != null)
				return false;
		} else if (!sqlserverloginname.equals(other.sqlserverloginname))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (usergroup == null) {
			if (other.usergroup != null)
				return false;
		} else if (!usergroup.equals(other.usergroup))
			return false;
		if (vCard == null) {
			if (other.vCard != null)
				return false;
		} else if (!vCard.equals(other.vCard))
			return false;
		return true;
	}

}
