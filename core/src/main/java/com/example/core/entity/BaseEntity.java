package com.example.core.entity;



import com.example.core.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public  class BaseEntity implements Serializable {
    @Id
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "com.example.core.utils.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "snowFlakeIdGenerator")
    @Column(name = "id", length=18)
    private String id;

    @Column(
            name = "Sequence",
            columnDefinition = "int(11) COMMENT '排序号' default 0"
    )
    protected Integer sequence;

    @Column(
            name = "IsValid",
            columnDefinition = "bit(1) COMMENT '是否有效' default 1"
    )
    protected Boolean valid = true;

    @Column(
            name = "IsDeleted",
            columnDefinition = "bit(1) COMMENT '是否有效' default 0"
    )
    protected Boolean deleted = false;

    @Column(
            name = "Remark",
            columnDefinition = "varchar(255) COMMENT '备注'"
    )
    protected String remark;

    @Version
    @Column(
            name = "Version",
            columnDefinition = "int(11) COMMENT '版本' default 0"
    )
    protected Integer version;

    @CreatedBy
    @Column(
            name = "CreatedUser",
            updatable = false,
            columnDefinition = "varchar(36) COMMENT '创建者'"
    )
    protected String createdUser;
    @Column(
            name = "CreatedUserName",
            updatable = false,
            columnDefinition = "varchar(50) COMMENT '创建者姓名'"
    )
    protected String createdUserName;

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @CreatedDate
    @Column(
            name = "CreatedTime",
            updatable = false,
            columnDefinition = "datetime COMMENT '创建时间'"
    )
    protected Date createdTime;

    @Column(
            name = "CreatedDate",
            updatable = false,
            columnDefinition = "varchar(10) COMMENT '创建日期'"
    )
    protected String createdDate;

    @LastModifiedBy
    @Column(
            name = "lastModifiedUser",
            columnDefinition = "varchar(36) COMMENT '最后修改者'"
    )
    protected String lastModifiedUser;
    @Column(
            name = "lastModifiedUserName",
            columnDefinition = "varchar(50) COMMENT '最后修改者姓名'"
    )
    protected String lastModifiedUserName;

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @LastModifiedDate
    @Column(
            name = "LastModifiedTime",
            columnDefinition = "datetime COMMENT '最后更新时间'"
    )
    protected Date lastModifiedTime;

    @Column(
            name = "LastModifiedDate",
            columnDefinition = "varchar(10) COMMENT '最后更新日期'"
    )
    protected String lastModifiedDate;

    public BaseEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Boolean getValid() {
        return this.valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreatedUser() {
        return this.createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedUser() {
        return this.lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    public Date getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedUserName() {
        return this.createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getLastModifiedUserName() {
        return this.lastModifiedUserName;
    }

    public void setLastModifiedUserName(String lastModifiedUserName) {
        this.lastModifiedUserName = lastModifiedUserName;
    }

    @PrePersist
    public void initPropertyValue() {
        if (isNull(this.createdDate)) {
            this.createdDate = DateUtil.parseString(DateUtil.getNow(), "yyyy-MM-dd");
            this.lastModifiedDate = this.createdDate;
        }

        if (null == this.getValid()) {
            this.setValid(true);
        }
    }
    public static boolean isNull(Object str) {
        return str == null || "null".equalsIgnoreCase(str.toString()) || "".equals(str) || "".equals(str.toString().trim());
    }

    @PreUpdate
    public void updatePropertyValue() {
        this.lastModifiedDate = DateUtil.parseString(DateUtil.getNow(), "yyyy-MM-dd");
        if (null == this.getValid()) {
            this.setValid(true);
        }
    }
}
