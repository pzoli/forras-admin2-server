package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity implementation class for Entity: RFIDCardSystemUser
 *
 */
@Entity(name="RFIDCardUser")
public class RFIDCardUser implements Serializable {

    private static final long serialVersionUID = -7661898665102382566L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id;
    

    private Date periodStart;
    private Date periodEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfcardid")
	private RFIDCard rfidCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientid")
	private Client client;

	public RFIDCardUser() {
		super();
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   

	public RFIDCard getRfidCard() {
        return this.rfidCard;
    }

    public void setRfidCard(RFIDCard rfidCard) {
        this.rfidCard = rfidCard;
    }   
	public Date getPeriodStart() {
		return this.periodStart;
	}

	public void setPeriodStart(Date periodStart) {
		this.periodStart = periodStart;
	}   
	public Date getPeriodEnd() {
		return this.periodEnd;
	}

	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}   
	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((periodEnd == null) ? 0 : periodEnd.hashCode());
        result = prime * result + ((periodStart == null) ? 0 : periodStart.hashCode());
        result = prime * result + ((rfidCard == null) ? 0 : rfidCard.hashCode());
        result = prime * result + ((client == null) ? 0 : client.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof RFIDCardUser))
            return false;
        RFIDCardUser other = (RFIDCardUser) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (periodEnd == null) {
            if (other.periodEnd != null)
                return false;
        } else if (!periodEnd.equals(other.periodEnd))
            return false;
        if (periodStart == null) {
            if (other.periodStart != null)
                return false;
        } else if (!periodStart.equals(other.periodStart))
            return false;
        if (rfidCard == null) {
            if (other.rfidCard != null)
                return false;
        } else if (!rfidCard.equals(other.rfidCard))
            return false;
        if (client == null) {
            if (other.client != null)
                return false;
        } else if (!client.equals(other.client))
            return false;
        return true;
    }

    public String getUserName() {
        return client != null ? client.getNeve() : null;
    }
}
