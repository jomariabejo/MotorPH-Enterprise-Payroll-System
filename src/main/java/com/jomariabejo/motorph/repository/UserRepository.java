package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.User;

public class UserRepository extends _AbstractHibernateRepository<User, Integer> {
    public UserRepository() {
        super(User.class);
    }
}
