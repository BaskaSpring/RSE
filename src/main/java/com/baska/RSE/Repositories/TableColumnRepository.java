package com.baska.RSE.Repositories;

import com.baska.RSE.Models.TableColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableColumnRepository extends JpaRepository<TableColumn,Long> {
}
