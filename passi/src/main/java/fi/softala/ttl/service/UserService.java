/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.service;

import fi.softala.ttl.model.User;

public interface UserService {
	
    void save(User user);

    User findByUsername(String username);
}