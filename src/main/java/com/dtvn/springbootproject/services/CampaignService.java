package com.dtvn.springbootproject.services;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignAndCreativesDTO;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampaignService {
    public Page<CampaignDTO> getCampaign(String name, Pageable pageable);
    public void deleteCampaign(int campaignId);
    public CampaignDTO updateCampagin(Integer campaginId, CampaignDTO campaignDTO);
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, Account account);
    public boolean isInteger(String number);
    public Campaign maptoEntity(CampaignDTO campaignDTO);
}
