package com.dtvn.springbootproject.services.impl;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.repositories.CampaignRepository;
import com.dtvn.springbootproject.services.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {
    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public Page<CampaignDTO> getCampaign(String name, Pageable pageable) {
        if(name == null || name.isEmpty()){
            Page<CampaignDTO> listCampaign = campaignRepository.getAllCampaign(pageable);
            return listCampaign;

        } else {
            Page<CampaignDTO> allCampaign = campaignRepository.findByName(name,pageable);
            return allCampaign;
        }
    }
    @Override
    public void deleteCampaign(String campaignId) {

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
