package com.qiaosheng.common.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**

 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractPersistent<T> implements Serializable {

    @org.springframework.data.annotation.Transient
    protected Long id;

    public AbstractPersistent() {
    }

    public AbstractPersistent(Long id) {
        this.id = id;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute
    public Long getId() { return id; }
    public T setId(Long id) { this.id = id; return (T) this; }
}
