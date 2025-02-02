package com.drunkenlion.alcoholfriday.domain.admin.restaurant.cart.dao;

import com.drunkenlion.alcoholfriday.domain.admin.restaurant.cart.entity.RestaurantOrderCartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantOrderCartDetailRepository extends JpaRepository<RestaurantOrderCartDetail, Long>, RestaurantOrderCartDetailCustomRepository {
}
