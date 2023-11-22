package com.study.hibernate.converter;

import org.hibernate.HibernateException;
import org.hibernate.collection.spi.PersistentArrayHolder;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.collection.spi.PersistentList;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.metamodel.CollectionClassification;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListStringType implements UserCollectionType {

    @Override
    public CollectionClassification getClassification() {
        return CollectionClassification.LIST;
    }

    @Override
    public Class<?> getCollectionClass() {
        return List.class;
    }

    @Override
    public PersistentCollection<?> instantiate(SharedSessionContractImplementor session, CollectionPersister persister) throws HibernateException {
        return new PersistentList<String>();
    }

    @Override
    public PersistentCollection<?> wrap(SharedSessionContractImplementor session, Object collection) {
        if (collection == null) {
            return null;
        }
        String[] values = (String[]) collection;
        PersistentList<String> list = new PersistentList<>();
        for (String value : values) {
            list.add(value);
        }
        return list;
    }

    @Override
    public Iterator<?> getElementsIterator(Object collection) {
        return ((List<?>) collection).iterator();
    }

    @Override
    public boolean contains(Object collection, Object entity) {
        return ((List<?>) collection).contains(entity);
    }

    @Override
    public Object indexOf(Object collection, Object entity) {
        return ((List<?>) collection).indexOf(entity);
    }

    @Override
    public Object replaceElements(Object original, Object target, CollectionPersister persister, Object owner, Map copyCache, SharedSessionContractImplementor session) throws HibernateException {
        List<String> originalList = (List<String>) original;
        List<String> targetList = (List<String>) target;

        originalList.clear();
        originalList.addAll(targetList);

        return original;
    }

    @Override
    public Object instantiate(int anticipatedSize) {
        return new ArrayList<>(anticipatedSize);
    }
}