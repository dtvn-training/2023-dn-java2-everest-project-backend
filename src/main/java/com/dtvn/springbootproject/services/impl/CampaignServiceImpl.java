package com.dtvn.springbootproject.services.impl;
import com.dtvn.springbootproject.constants.AppConstants;
import com.dtvn.springbootproject.constants.HttpConstants;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignAndCreativesDTO;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.dto.responseDtos.Creative.CreativeDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Campaign;
import com.dtvn.springbootproject.entities.Creatives;
import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.repositories.CampaignRepository;
import com.dtvn.springbootproject.repositories.CreativeRepository;
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
    @Autowired
    private CreativeRepository creativeRepository;

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
        Campaign campaign = campaignRepository.findByIdAndDeleteFlagIsFalse(campaignId)
                .orElseThrow(() -> new RuntimeException(AppConstants.CAMPAIGN_NOT_FOUND));
        campaign.setDeleteFlag(true);
        campaignRepository.save(campaign);
    }

    @Override
    public CampaignAndCreativesDTO updateCampagin(Integer campaginId,CampaignAndCreativesDTO campaignAndCreativesDTO ) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        try{
            Optional<Campaign> oldCampaign = campaignRepository.findByIdAndDeleteFlagIsFalse(campaginId);
            Optional<Creatives> oldCreate = creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(oldCampaign)  ;
        if(oldCampaign.isPresent()){
            //update campaign
            oldCampaign.get().setStatus(campaignDTO.getStatus());
            oldCampaign.get().setBudget(campaignDTO.getBudget());
            oldCampaign.get().setBidAmount(campaignDTO.getBidAmount());
            oldCampaign.get().setStartDate(campaignDTO.getStartDate());
            oldCampaign.get().setEndDate(campaignDTO.getEndDate());
            //update creative
            oldCreate.get().setTitle(creativeDTO.getTitle());
            oldCreate.get().setDescription(creativeDTO.getDescription());
            oldCreate.get().setImageUrl(creativeDTO.getImageUrl());
            oldCreate.get().setFinalUrl(creativeDTO.getFinalUrl());
            campaignRepository.save(oldCampaign.get());
            creativeRepository.save(oldCreate.get());
            return campaignAndCreativesDTO;
        } else throw new ErrorException(AppConstants.CAMPAGIGN_UPDATE_FAILED, HttpConstants.HTTP_FORBIDDEN);

        } catch (Exception e){
                throw new ErrorException(AppConstants.CAMPAGIGN_UPDATE_FAILED, HttpConstants.HTTP_FORBIDDEN);
        }
    }
    @Override
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, Account account) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        Campaign campaignCreated =  new Campaign();
        campaignCreated = mapper.map(campaignDTO, Campaign.class);
        campaignCreated.setAccountId(account);
        campaignRepository.save(campaignCreated);
        Creatives creatives = new Creatives();
        creatives = mapper.map(creativeDTO, Creatives.class);
        System.out.println(campaignCreated);
        campaignRepository.save(campaignCreated);
        creatives.setCampaignId(campaignCreated);
        creatives.setDeleteFlag(false);
        Creatives creativesCreated = creativeRepository.save(creatives);
        return campaignAndCreativesDTO;
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

    @Override
    public Campaign maptoEntity(CampaignDTO campaignDTO) {
        return null;
    }


}
