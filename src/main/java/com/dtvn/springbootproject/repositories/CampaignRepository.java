package com.dtvn.springbootproject.repositories;

import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.entities.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

    public interface CampaignRepository extends JpaRepository<Campaign, Integer> {

        @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = 0 " +
                "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
        Page<Campaign> findByName(@Param("name") String name, Pageable pageable);

    @Query("Select a From Campaign a Where a.deleteFlag = 0")
    Page<Campaign> getAllCampaign(Pageable pageable);

}
