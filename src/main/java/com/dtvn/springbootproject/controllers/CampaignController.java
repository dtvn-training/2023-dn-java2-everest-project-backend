package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.constants.AppConstants;
import com.dtvn.springbootproject.constants.HttpConstants;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.entities.Campaign;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.repositories.CampaignRepository;
import com.dtvn.springbootproject.services.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.dtvn.springbootproject.constants.HttpConstants.HTTP_BAD_REQUEST;
import static com.dtvn.springbootproject.constants.HttpConstants.HTTP_OK;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService  campaignService;
    private final CampaignRepository campaignRepository;
    @GetMapping("/getCampaign")
    public ResponseEntity<ResponseMessage<Page<CampaignDTO>>> getCampaign(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) String strPageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) String strPageSize){
        if(!campaignService.isInteger(strPageNo))
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.PAGENO_INVALID, HTTP_BAD_REQUEST));
        else if(!campaignService.isInteger(strPageSize)){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.PAGESIZE_INVALID, HTTP_BAD_REQUEST));
        }
        int pageNo = Integer.parseInt(strPageNo);
        int pageSize = Integer.parseInt(strPageSize);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage<>(AppConstants.CAMPAIGN_GET_SUCCESS, HttpConstants.HTTP_OK,campaignService.getCampaign(name,pageable)));
    }

    @PatchMapping("/deleteCampagign")
    public ResponseEntity<ResponseMessage<CampaignDTO>> deleteCampagin(
            @RequestParam(value = "id", required = true) String strCampaginId){
        try{
            Integer campaignId = Integer.parseInt(strCampaginId);
            Optional<Campaign> campaign = campaignRepository.findById(campaignId);
            if(campaign.isPresent()){
                if(campaign.get().isDeleteFlag() == true){
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(AppConstants.CAMPAGIN_IS_DELETED, HTTP_BAD_REQUEST));
                } else {
                    campaignService.deleteCampaign(campaignId);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(AppConstants.CAMPAGIN_DELETE_SUCCESS, HTTP_BAD_REQUEST));
                }
            }else{
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(AppConstants.CAMPAGIN_NOT_FOUND, HTTP_BAD_REQUEST));
            }
        } catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.CAMPAGIN_ID_INVALID, HTTP_BAD_REQUEST));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_DELETE_FAILD, HTTP_BAD_REQUEST));
        }
    }

    @PutMapping("/updateCampagin")
    public ResponseEntity<ResponseMessage<ResponseEntity>> updateCampaign(
            @PathVariable String strCampaignId,
            @RequestBody CampaignDTO newCamapaign){
        if(campaignService.isInteger(strCampaignId)){
            Integer campaignId = Integer.parseInt(strCampaignId);
            if(campaignRepository.existsByName(newCamapaign.getName())){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(AppConstants.CAMPAGIN_ALREADY_EXISTS, HTTP_BAD_REQUEST));
            } else{
                campaignService.updateCampagin(campaignId, newCamapaign);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(AppConstants.CAMPAGIN_UPDATE_SUCCESS, HTTP_OK));
            }
        } else{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.CAMPAGIN_ID_INVALID, HTTP_BAD_REQUEST));
        }
    }

}
