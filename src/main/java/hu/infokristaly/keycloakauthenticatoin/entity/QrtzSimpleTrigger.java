package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ForeignKey;

import java.io.Serializable;


/**
 * The persistent class for the qrtz_simple_triggers database table.
 * 
 */
@Entity
@Table(name="qrtz_simple_triggers")
@NamedQuery(name="QrtzSimpleTrigger.findAll", query="SELECT q FROM QrtzSimpleTrigger q")
public class QrtzSimpleTrigger implements Serializable {
	private static final long serialVersionUID = 1L;
	private QrtzSimpleTriggerPK id;
	private Long repeatCount;
	private Long repeatInterval;
	private Long timesTriggered;
	private QrtzTrigger qrtzTrigger;

	public QrtzSimpleTrigger() {
	}


	@EmbeddedId
	public QrtzSimpleTriggerPK getId() {
		return this.id;
	}

	public void setId(QrtzSimpleTriggerPK id) {
		this.id = id;
	}


	@Column(name="repeat_count", nullable=false)
	public Long getRepeatCount() {
		return this.repeatCount;
	}

	public void setRepeatCount(Long repeatCount) {
		this.repeatCount = repeatCount;
	}


	@Column(name="repeat_interval", nullable=false)
	public Long getRepeatInterval() {
		return this.repeatInterval;
	}

	public void setRepeatInterval(Long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}


	@Column(name="times_triggered", nullable=false)
	public Long getTimesTriggered() {
		return this.timesTriggered;
	}

	public void setTimesTriggered(Long timesTriggered) {
		this.timesTriggered = timesTriggered;
	}


	//bi-directional many-to-one association to QrtzTrigger
	@ManyToOne      
	@JoinColumns({
		@JoinColumn(name="sched_name", referencedColumnName="sched_name", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="trigger_group", referencedColumnName="trigger_group", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="trigger_name", referencedColumnName="trigger_name", nullable=false, insertable=false, updatable=false)
		})
	@ForeignKey(name = "qrtz_simple_triggers_sched_name_fkey")	
	public QrtzTrigger getQrtzTrigger() {
		return this.qrtzTrigger;
	}

	public void setQrtzTrigger(QrtzTrigger qrtzTrigger) {
		this.qrtzTrigger = qrtzTrigger;
	}

}