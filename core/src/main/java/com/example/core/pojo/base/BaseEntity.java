package com.example.core.pojo.base;


import com.example.core.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;


@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEntity implements Serializable {
    @Id
    @GenericGenerator(name = "snowFlakeIdGenerator", strategy = "com.example.core.utils.SnowFlakeIdGenerator")
    @GeneratedValue(generator = "snowFlakeIdGenerator")
    @Column(name = "id", length = 18)
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


    public String getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

        if (StringUtils.isEmpty(this.createdUser)) {
            this.createdUser = this.convertCurrentOperatorName();
            this.lastModifiedUser = this.convertCurrentOperatorName();
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
        if (!StringUtils.hasText(lastModifiedUser) || !lastModifiedUser.equals(this.convertCurrentOperatorName())) {
            lastModifiedUser = this.convertCurrentOperatorName();
        }


    }

    private Object getPrincipal() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        return ctx != null && ctx.getAuthentication() != null && ctx.getAuthentication().getPrincipal() != null ? ctx.getAuthentication().getPrincipal() : null;
    }

    public LoginUser convertCurrentOperator() {
        Object principal = this.getPrincipal();
        return principal instanceof LoginUser ? (LoginUser) principal : null;
    }

    public String convertCurrentOperatorName() {
        LoginUser operator = this.convertCurrentOperator();
        return operator == null ? null : operator.getUser().getUserName();
    }
}
