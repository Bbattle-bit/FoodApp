package com.bbattle.foodapp.repository;
import com.bbattle.foodapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//JpaRepository<User, Long> dice a Spring: “User è l’entity, Long è il tipo della chiave primaria”.
//findByEmail è un esempio di query custom che useremo per login/autenticazione.
public interface UserRepository extends  JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);
}
