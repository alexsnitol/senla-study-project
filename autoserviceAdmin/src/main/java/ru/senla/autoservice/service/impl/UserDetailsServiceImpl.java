package ru.senla.autoservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.senla.autoservice.model.Role;
import ru.senla.autoservice.model.User;
import ru.senla.autoservice.repo.IUserRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Slf4j
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl extends AbstractServiceImpl<User, IUserRepository> implements UserDetailsService {

    private IUserRepository userRepository;


    @PostConstruct
    public void init() {
        setClazz(User.class);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            String message = String.format("User with username %s not found", username);
            log.error(message);
            throw new UsernameNotFoundException(message);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                getGrantedAuthoritiesByUser(user)
        );
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthoritiesByUser(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Collection<Role> roles = user.getRoles();

        // Adding all roles of user
        authorities.addAll(roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList()));

        // Adding all authorities of roles
        roles.forEach(r -> r.getAuthorities()
                .forEach(a -> authorities.add(new SimpleGrantedAuthority(a.getName()))));

        return authorities;
    }

}
