package com.anshao.session;

import com.anshao.utils.JedisUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtils jedisUtils;

    private final String shiro_session_perfix = "ceshi_session:";

    private byte[] getKey(String key) {
        return (shiro_session_perfix+key).getBytes();
    }

    private void saveSession(Session session) {
        if (session !=null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtils.set(key,value);
            //设置超时时间
            jedisUtils.expire(key,600);
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        //先获取sessionId
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("ceshi  session");
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtils.get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null && session.getId() == null) {
            return;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtils.del(key);
    }

    /**
     * 获取所有活动的session
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtils.keys(shiro_session_perfix);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for (byte[] key: keys) {
            Session session = (Session) SerializationUtils.deserialize(jedisUtils.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
