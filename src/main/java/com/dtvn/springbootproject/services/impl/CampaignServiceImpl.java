package com.dtvn.springbootproject.services.impl;
import com.dtvn.springbootproject.constants.AppConstants;
import com.dtvn.springbootproject.constants.HttpConstants;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Campaign;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.repositories.CampaignRepository;
import com.dtvn.springbootproject.services.CampaignService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {
    @Autowired
    private CampaignRepository campaignRepository;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Page<CampaignDTO> getCampaign(String name, Pageable pageable) {
        if(name == null || name.isEmpty()){
            Page<Campaign> listCampaign = campaignRepository.getAllCampaign(pageable);
            return listCampaign.map(campaign -> mapper.map(campaign, CampaignDTO.class ));
        } else {
            Page<Campaign> allCampaign = campaignRepository.findByName(name,pageable);
            return allCampaign.map(campaign -> mapper.map(campaign, CampaignDTO.class ));
        }
    }
    @Override
    public void deleteCampaign(int campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException(AppConstants.CAMPAGIN_NOT_FOUND));
        campaign.setDeleteFlag(true);
        campaignRepository.save(campaign);
    }

    @Override
    public CampaignDTO updateCampagin(Integer campaginId, CampaignDTO campaignDTO) {
        return null;
    }

    @Override
    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        return null;
    }

    @Override
    public boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
