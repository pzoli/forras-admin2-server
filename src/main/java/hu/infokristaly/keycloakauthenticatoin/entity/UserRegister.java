package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Entity
public class UserRegister {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String emailAddress;
	private String userName;
	@ManyToOne
	@JoinColumn(name = "registereduser")
	private SystemUser registeredUser;
	private Date registeredDate;
	private Date activationDate;
	private Date mailSentDate;
	private String ipAddress;
	private String hashId;
	private Byte mailSendTryCounter;
	private String userPassword;
	@Version
	private Long version;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public SystemUser getRegisteredUser() {
		return registeredUser;
	}
	public void setRegisteredUser(SystemUser registeredUser) {
		this.registeredUser = registeredUser;
	}
	public Date getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	public Date getMailSentDate() {
		return mailSentDate;
	}
	public void setMailSentDate(Date mailSentDate) {
		this.mailSentDate = mailSentDate;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Byte getMailSendTryCounter() {
		return mailSendTryCounter;
	}
	public void setMailSendTryCounter(Byte mailSendTryCounter) {
		this.mailSendTryCounter = mailSendTryCounter;
	}
	public String getHashId() {
		return hashId;
	}
	public void setHashId(String hashId) {
		this.hashId = hashId;
	}	
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public Date getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}
	@PrePersist
	private void prePersist() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		this.mailSendTryCounter = 0;
		this.registeredDate = new Date();
		//this.hashId = StringTools.getMD5HashHex(registeredDate.toString() + " :: " + (new BigInteger(String.valueOf(hashCode())).toString(16)));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activationDate == null) ? 0 : activationDate.hashCode());
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result + ((hashId == null) ? 0 : hashId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((mailSendTryCounter == null) ? 0 : mailSendTryCounter.hashCode());
		result = prime * result + ((mailSentDate == null) ? 0 : mailSentDate.hashCode());
		result = prime * result + ((registeredDate == null) ? 0 : registeredDate.hashCode());
		result = prime * result + ((registeredUser == null) ? 0 : registeredUser.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userPassword == null) ? 0 : userPassword.hashCode());
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
		UserRegister other = (UserRegister) obj;
		if (activationDate == null) {
			if (other.activationDate != null)
				return false;
		} else if (!activationDate.equals(other.activationDate))
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (hashId == null) {
			if (other.hashId != null)
				return false;
		} else if (!hashId.equals(other.hashId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (mailSendTryCounter == null) {
			if (other.mailSendTryCounter != null)
				return false;
		} else if (!mailSendTryCounter.equals(other.mailSendTryCounter))
			return false;
		if (mailSentDate == null) {
			if (other.mailSentDate != null)
				return false;
		} else if (!mailSentDate.equals(other.mailSentDate))
			return false;
		if (registeredDate == null) {
			if (other.registeredDate != null)
				return false;
		} else if (!registeredDate.equals(other.registeredDate))
			return false;
		if (registeredUser == null) {
			if (other.registeredUser != null)
				return false;
		} else if (!registeredUser.equals(other.registeredUser))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userPassword == null) {
			if (other.userPassword != null)
				return false;
		} else if (!userPassword.equals(other.userPassword))
			return false;
		return true;
	}
}
