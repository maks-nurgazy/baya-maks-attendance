package project.manas.attendance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.manas.attendance.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return new
                UserDetailsImpl(userService.findOneByUserName(userName)
                .orElseThrow(IllegalArgumentException::new));
    }


//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Optional<User> u = userService.findOneByUserName(userName);
//        if (!u.isPresent()) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        User user = u.get();
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        Role role = user.getRole();
//        grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
//
//        UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getUserName(), user.getHashPassword(), grantedAuthorities);
//
//        return userDetails;
//    }

}
