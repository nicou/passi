/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.service;

import fi.softala.ttl.model.User;

public interface UserService {
	
    public void save(User user);

    public User findByUsername(String username);
    
    public boolean setPasswordResetToken(String email, String token);
    
    public boolean resetUserPassword(String token, String password);
    
}