package com.dtvn.springbootproject.repositories;

import com.dtvn.springbootproject.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

}
