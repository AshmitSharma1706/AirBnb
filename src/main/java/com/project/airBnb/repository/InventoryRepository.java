package com.project.airBnb.repository;

import com.project.airBnb.entity.Hotel;
import com.project.airBnb.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}
