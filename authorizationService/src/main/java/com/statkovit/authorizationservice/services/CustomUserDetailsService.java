package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.entities.Account;
import com.statkovit.authorizationservice.entities.Role;
import com.statkovit.authorizationservice.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.getByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User %s does not exist!", email))
                );
    }

    private final static class CustomUserDetails implements UserDetails {

        private Role role;
        private String email;
        private String password;
        private boolean enabled;

        public CustomUserDetails(Account account) {
            this.role = account.getRole();
            this.email = account.getEmail();
            this.password = account.getPassword();
            this.enabled = account.isEnabled();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return new ArrayList<>(Collections.singletonList(role));
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            //Login by email, not by username
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }
}
