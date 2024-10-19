package com.example.core.utils;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * JPA 雪花算法ID生成器
 */
public class SnowFlakeIdGenerator implements IdentifierGenerator, Configurable {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object o) throws HibernateException {
        return SnowFlake.getBean().nextIdString();
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
    }
}

