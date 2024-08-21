package com.crypto07.SpringSecurityMain.Repository;

import com.crypto07.SpringSecurityMain.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserDetails findByEmail(String username);
}
