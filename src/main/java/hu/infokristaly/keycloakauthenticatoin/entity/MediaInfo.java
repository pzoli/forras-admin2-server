package hu.infokristaly.keycloakauthenticatoin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "media_info", indexes={@Index(columnList="systemUserSub", name = "idx_systemuser_sub")})
@NamedQuery(name = "MediaInfo.findAll", query = "SELECT m FROM MediaInfo m")
public class MediaInfo implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull
    private String fileName;

    @Temporal(TemporalType.TIMESTAMP)
    @Basic
    private Date closeDate;

    @Basic
    private String systemUserSub;

    public MediaInfo() {
    }

    public MediaInfo(String fileName, String systemUserSub) {
        this.fileName = fileName;
        this.systemUserSub = systemUserSub;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getSystemUserSub() {
        return systemUserSub;
    }

    public void setSystemUserSub(String systemUserSub) {
        this.systemUserSub = systemUserSub;
    }
}
