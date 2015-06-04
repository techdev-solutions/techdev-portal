package de.techdev.portal.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;

import java.util.List;

import static java.util.Arrays.asList;

@Controller
public class ListUserController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping(value = "/listUsers", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView listUsers() {
        ModelAndView modelAndView = new ModelAndView("domain/users/listUsers");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<User> users = jdbcTemplate.query("SELECT username, enabled FROM users", (rs, rowNum) ->
                        new User(rs.getString("username"), "EMPTY", rs.getBoolean("enabled"), true, true, true, asList())
        );
        modelAndView.addObject("users", users);
        return modelAndView;
    }
}
