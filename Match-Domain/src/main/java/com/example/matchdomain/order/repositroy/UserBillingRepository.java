package com.example.matchdomain.order.repositroy;

import com.example.matchdomain.order.entity.UserBillingCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBillingRepository extends JpaRepository<UserBillingCard,Long> {
}
