// adapters/out/persistence/menuitem/repository/MenuItemJpaRepository.java
package com.fiap.restaurant_api.adapters.out.persistence.menuitem.repository;

import com.fiap.restaurant_api.adapters.out.persistence.menuitem.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemJpaRepository
        extends JpaRepository<MenuItemJpaEntity, Long> {

    List<MenuItemJpaEntity> findByRestaurantId(Long restaurantId);
}