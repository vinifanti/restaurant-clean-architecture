
package com.fiap.restaurant_api.adapters.out.persistence.menuitem.mapper;

import com.fiap.restaurant_api.adapters.out.persistence.menuitem.entity.MenuItemJpaEntity;
import com.fiap.restaurant_api.domain.model.MenuItem;
import com.fiap.restaurant_api.domain.model.Restaurant;

public class MenuItemPersistenceMapper {

    private MenuItemPersistenceMapper() {
    }

    public static MenuItemJpaEntity toEntity(MenuItem menuItem) {

        return new MenuItemJpaEntity(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isDineInOnly(),
                menuItem.getImagePath(),
                menuItem.getRestaurant().getId()
        );
    }

    public static MenuItem toDomain(
            MenuItemJpaEntity entity,
            Restaurant restaurant
    ) {

        return new MenuItem(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.isDineInOnly(),
                entity.getImagePath(),
                restaurant
        );
    }
}