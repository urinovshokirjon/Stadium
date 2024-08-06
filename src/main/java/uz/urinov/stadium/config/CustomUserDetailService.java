package uz.urinov.stadium.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.urinov.stadium.Prifile.ProfileEntity;
import uz.urinov.stadium.Prifile.ProfileRepository;

import java.util.Optional;


@Service
public class CustomUserDetailService implements UserDetailsService {
@Autowired
private ProfileRepository profileRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByPhoneAndVisibleTrue(username);

        if (profileEntityOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        ProfileEntity employee = profileEntityOptional.get();

        return new CustomUserDetail(employee);

    }
}
