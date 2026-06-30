
package com.fiap.restaurant_api.adapters.out.persistence.menuitem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "dine_in_only", nullable = false)
    private boolean dineInOnly;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    public MenuItemJpaEntity() {
    }

    public MenuItemJpaEntity(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean dineInOnly,
            String imagePath,
            Long restaurantId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInOnly = dineInOnly;
        this.imagePath = imagePath;
        this.restaurantId = restaurantId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isDineInOnly() { return dineInOnly; }
    public void setDineInOnly(boolean dineInOnly) { this.dineInOnly = dineInOnly; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}