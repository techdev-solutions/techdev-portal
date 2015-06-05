package de.techdev.portal.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Repository
public class UserRepository {

    private static final String SELECT_ALL_USERS_WITH_ROLES = "SELECT username, enabled, ARRAY(SELECT authority FROM authorities a WHERE a.username = u.username) as roles FROM users u";

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void setUpJdbcTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS_WITH_ROLES, (rs, rowNum) ->
            new User(rs.getString("username"), "", rs.getBoolean("enabled"), true, true, true, toCollection(rs.getArray("roles")))
        );
    }

    private Collection<? extends GrantedAuthority> toCollection(Array sqlRoles) throws SQLException{
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Object[] roles = (Object[]) sqlRoles.getArray();
        for (Object role : roles) {
            authorities.add(new SimpleGrantedAuthority((String) role));
        }
        return authorities;
    }

}
