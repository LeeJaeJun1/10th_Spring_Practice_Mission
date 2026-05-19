package umc.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.domain.store.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
