package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity implementation class for Entity: RFIDLogEntry
 *
 */
@Entity

public class RFIDLogEntry implements Serializable {

    private static final long serialVersionUID = -5987903563774980633L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
	private Long id;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfcarduserid")
    private RFIDCardUser rfidCardUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfcardid")
	private RFIDCardReader rfidCardReader;
	
	private Date logDate;
	

	public RFIDLogEntry() {
		super();
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
	public RFIDCardUser getRfidCardUser() {
		return this.rfidCardUser;
	}

	public void setRfidCardUser(RFIDCardUser rfidCardUser) {
		this.rfidCardUser = rfidCardUser;
	}   
	public RFIDCardReader getRfidCardReader() {
		return this.rfidCardReader;
	}

	public void setRfidCardReader(RFIDCardReader rfidCardReader) {
		this.rfidCardReader = rfidCardReader;
	}   
	public Date getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
   
}
