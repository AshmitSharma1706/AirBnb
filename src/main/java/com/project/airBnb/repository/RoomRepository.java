package com.project.airBnb.repository;

import com.project.airBnb.entity.Inventory;
import com.project.airBnb.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
}
