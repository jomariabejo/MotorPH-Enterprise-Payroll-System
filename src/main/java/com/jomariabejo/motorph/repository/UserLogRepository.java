package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.UserLog;

public class UserLogRepository extends _AbstractHibernateRepository<UserLog, Integer> {
    public UserLogRepository() {
        super(UserLog.class);
    }
}
