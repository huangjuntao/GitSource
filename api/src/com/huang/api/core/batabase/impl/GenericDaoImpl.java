package com.huang.api.core.batabase.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.huang.api.core.batabase.GenericDao;
import com.huang.api.core.util.PageResults;
import com.huang.api.core.util.RowMapper;

@Repository("genericDao")
public class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK>
{
    @Autowired
    private SessionFactory sessionFactory;

    protected Class<T> entityClass;

    public GenericDaoImpl()
    {

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Class getEntityClass()
    {
        if (entityClass == null)
        {
            entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return entityClass;
    }

    protected Session getSession()
    {
        // 需要开启事物，才能得到CurrentSession
        return sessionFactory.getCurrentSession();
    }

    public void save(T t)
    {
        this.getSession().save(t);
    }

    public void saveOrUpdate(T t)
    {
        this.getSession().saveOrUpdate(t);
    }

    @SuppressWarnings("unchecked")
    public T load(PK id)
    {
        return (T) getSession().load(getEntityClass(), id);
    }

    @SuppressWarnings("unchecked")
    public T get(PK id)
    {
        return (T) getSession().get(getEntityClass(), id);
    }

    public boolean contains(T t)
    {
        return getSession().contains(t);
    }

    public void delete(T t)
    {
        getSession().delete(t);
    }

    public boolean deleteById(PK Id)
    {
        T t = get(Id);
        if (t == null)
        {
            return false;
        }
        delete(t);
        return true;
    }

    public void deleteAll(Collection<T> entities)
    {
        for (Object entity : entities)
        {
            this.getSession().delete(entity);
        }
    }

    public void queryHql(String hqlString, Object... values)
    {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.executeUpdate();
    }

    public void querySql(String sqlString, Object... values)
    {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.executeUpdate();
    }

    public T getByHQL(String hqlString, Object... values)
    {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    public T getBySQL(String sqlString, Object... values)
    {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    public List<T> getListByHQL(String hqlString, Object... values)
    {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return query.list();
    }

    public List<T> getListBySQL(String sqlString, Object... values)
    {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return query.list();
    }

    public List<T> findListBySql(final String sql, final RowMapper map, final Object... values)
    {
        final List list = new ArrayList();
        // 执行JDBC的数据批量保存
        Work jdbcWork = new Work()
        {
            public void execute(Connection connection) throws SQLException
            {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try
                {
                    ps = connection.prepareStatement(sql);
                    for (int i = 0; i < values.length; i++)
                    {
                        setParameter(ps, i, values[i]);
                    }
                    rs = ps.executeQuery();
                    int index = 0;
                    while (rs.next())
                    {
                        Object obj = map.mapRow(rs, index++);
                        list.add(obj);
                    }
                } finally
                {
                    if (rs != null)
                    {
                        rs.close();
                    }
                    if (ps != null)
                    {
                        ps.close();
                    }
                }
            }
        };
        this.getSession().doWork(jdbcWork);
        return list;
    }

    public void refresh(T t)
    {
        getSession().refresh(t);
    }

    @Override
    public void update(T t)
    {
        getSession().update(t);
    }

    @Override
    public Long countByHql(String hql, Object... values)
    {
        Query query = this.getSession().createQuery(hql);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return (Long) query.uniqueResult();
    }

    @Override
    public PageResults<T> findPageByFetchedHql(String hql, String countHql, int pageNo, int pageSize, Object... values)
    {
        PageResults<T> retValue = new PageResults<T>();
        Query query = this.getSession().createQuery(hql);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        int currentPage = pageNo > 1 ? pageNo : 1;
        retValue.setCurrentPage(currentPage);
        retValue.setPageSize(pageSize);
        if (countHql == null)
        {
            ScrollableResults results = query.scroll();
            results.last();
            retValue.setTotalCount(results.getRowNumber() + 1);// 设置总记录数
        } else
        {
            Long count = countByHql(countHql, values);
            retValue.setTotalCount(count.intValue());
        }
        retValue.resetPageNo();
        List<T> itemList = query.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
        if (itemList == null)
        {
            itemList = new ArrayList<T>();
        }
        retValue.setResults(itemList);

        return retValue;
    }

    private void setParameter(PreparedStatement ps, int pos, Object data) throws SQLException
    {
        if (data == null)
        {
            ps.setNull(pos + 1, Types.VARCHAR);
            return;
        }
        Class dataCls = data.getClass();
        if (String.class.equals(dataCls))
        {
            ps.setString(pos + 1, (String) data);
        } else if (boolean.class.equals(dataCls))
        {
            ps.setBoolean(pos + 1, ((Boolean) data));
        } else if (int.class.equals(dataCls))
        {
            ps.setInt(pos + 1, (Integer) data);
        } else if (double.class.equals(dataCls))
        {
            ps.setDouble(pos + 1, (Double) data);
        } else if (Date.class.equals(dataCls))
        {
            Date val = (Date) data;
            ps.setTimestamp(pos + 1, new Timestamp(val.getTime()));
        } else if (BigDecimal.class.equals(dataCls))
        {
            ps.setBigDecimal(pos + 1, (BigDecimal) data);
        } else
        {
            // 未知类型
            ps.setObject(pos + 1, data);
        }

    }

}
