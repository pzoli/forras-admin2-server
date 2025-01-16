package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ForeignKey;

import java.io.Serializable;


/**
 * The persistent class for the qrtz_cron_triggers database table.
 * 
 */
@Entity
@Table(name="qrtz_cron_triggers")
@NamedQuery(name="QrtzCronTrigger.findAll", query="SELECT q FROM QrtzCronTrigger q")
public class QrtzCronTrigger implements Serializable {
	private static final long serialVersionUID = 1L;
	private QrtzCronTriggerPK id;
	private String cronExpression;
	private String timeZoneId;
	private QrtzTrigger qrtzTrigger;

	public QrtzCronTrigger() {
	}


	@EmbeddedId
	public QrtzCronTriggerPK getId() {
		return this.id;
	}

	public void setId(QrtzCronTriggerPK id) {
		this.id = id;
	}


	@Column(name="cron_expression", nullable=false, length=120)
	public String getCronExpression() {
		return this.cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}


	@Column(name="time_zone_id", length=80)
	public String getTimeZoneId() {
		return this.timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}


	//bi-directional many-to-one association to QrtzTrigger
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="sched_name", referencedColumnName="sched_name", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="trigger_group", referencedColumnName="trigger_group", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="trigger_name", referencedColumnName="trigger_name", nullable=false, insertable=false, updatable=false)
		})
	@ForeignKey(name = "qrtz_cron_triggers_sched_name_fkey")
	public QrtzTrigger getQrtzTrigger() {
		return this.qrtzTrigger;
	}

	public void setQrtzTrigger(QrtzTrigger qrtzTrigger) {
		this.qrtzTrigger = qrtzTrigger;
	}

}