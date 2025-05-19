package athletia.service;

import athletia.model.User;
import athletia.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));

        return buildUserDetails(user);
    }
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.id())
                .password(user.password())
                .roles("USER")
                .build();
    }
}
