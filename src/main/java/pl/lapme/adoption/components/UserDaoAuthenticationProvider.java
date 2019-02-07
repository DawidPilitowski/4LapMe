package pl.lapme.adoption.components;
import pl.lapme.adoption.model.ApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import pl.lapme.adoption.model.EventFactory;
import pl.lapme.adoption.repository.ApplicationEventRepository;
import pl.lapme.adoption.service.UserService;
import org.springframework.security.core.Authentication;


import javax.servlet.http.HttpServletRequest;

public class UserDaoAuthenticationProvider extends DaoAuthenticationProvider {
    @Autowired
    private ApplicationEventRepository applicationEventRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Authentication authenticate(Authentication authentication)throws AuthenticationException{
        AuthenticationException caughtException=null;
        Authentication authenticationResult=null;

        try {
            authenticationResult = super.authenticate(authentication);
        } catch (AuthenticationException authenticationException) {
            caughtException = authenticationException;
        }
        if (caughtException != null) {
            logAuthenticationAttempt(authentication.getName(), authentication.getCredentials().toString(), authentication);
            throw caughtException;
        }
        if(authenticationResult == null){
            logAuthenticationAttempt(authentication.getName(), authentication.getCredentials().toString(), authentication);
            return null;
        }
        logAuthenticationAttempt(authentication.getName(), authentication.getCredentials().toString(), authenticationResult);
        return authenticationResult;
    }
    private void logAuthenticationAttempt(String name, String password, Authentication authentication) {
        ApplicationEvent applicationEvent;
        if(authentication.isAuthenticated()){
            applicationEvent = applicationEventRepository.saveAndFlush(EventFactory.loginSuccess(name, request.getRemoteAddr()));
        }
        else {
            applicationEvent = applicationEventRepository.saveAndFlush(EventFactory.loginFailed(name, password, request.getRemoteAddr()));
        }
        userService.logAuthenticationAttempt(name, applicationEvent);
    }
}
