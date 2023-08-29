/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.config;

import group2.client.entities.Casher;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author hokim
 */
public class CasherConfig implements UserDetails{
    private Casher casher;

    public CasherConfig(Casher casher) {
        this.casher = casher;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        HashSet<SimpleGrantedAuthority> set = new HashSet<SimpleGrantedAuthority>();
//        set.add(new SimpleGrantedAuthority(user.getRole()));
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(casher.getRole());
        return Arrays.asList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return casher.getPassword();
    }

    @Override
    public String getUsername() {
        return casher.getEmail();
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
        return true;
    }
}

