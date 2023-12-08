package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.config.JwtService;
import com.dtvn.springbootproject.constants.AppConstants;
import com.dtvn.springbootproject.constants.AuthConstants;
import com.dtvn.springbootproject.constants.HttpConstants;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignAndCreativesDTO;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.dto.responseDtos.Creative.CreativeDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Campaign;
import com.dtvn.springbootproject.entities.Creatives;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.repositories.CampaignRepository;
import com.dtvn.springbootproject.repositories.CreativeRepository;
import com.dtvn.springbootproject.services.CampaignService;
import com.dtvn.springbootproject.services.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.dtvn.springbootproject.constants.HttpConstants.HTTP_BAD_REQUEST;
import static com.dtvn.springbootproject.constants.HttpConstants.HTTP_OK;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService  campaignService;
    private final CampaignRepository campaignRepository;
    private final CreativeRepository creativeRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final FirebaseService firebaseService;
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
            Optional<Campaign> campaign = campaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);
            if(campaign.isPresent()){
                if(campaign.get().isDeleteFlag() == true){
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(AppConstants.CAMPAGIGN_IS_DELETED, HTTP_BAD_REQUEST));
                } else {
                    campaignService.deleteCampaign(campaignId);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(AppConstants.CAMPAIGN_DELETE_SUCCESS, HTTP_BAD_REQUEST));
                }
            }else{
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(AppConstants.CAMPAIGN_NOT_FOUND, HTTP_BAD_REQUEST));
            }
        } catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.CAMPAIGN_ID_INVALID, HTTP_BAD_REQUEST));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_DELETE_FAILD, HTTP_BAD_REQUEST));
        }
    }

    @PutMapping("/updateCampagin")
    public ResponseEntity<ResponseMessage<CampaignAndCreativesDTO>> updateCampaign(
            @RequestParam(value = "id", required = true) String strCampaignId,
            @RequestBody CampaignAndCreativesDTO campaignAndCreativesDTO){

        if(campaignService.isInteger(strCampaignId)){
            Integer campaignId = Integer.parseInt(strCampaignId);
            CreativeDTO creatives = campaignAndCreativesDTO.getCreativesDTO();
            String creativesName = creatives.getTitle();
            Optional<Campaign> oldCampaign = campaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);

            //check campagin is present
            if(oldCampaign.isPresent()){
                //if campagin present: true
                    Optional<Creatives> oldCreate = creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(oldCampaign);
                    //check if creatives is present
                    if(oldCreate.isPresent()){
                        //check if creatives is present: true
                        if(creatives.getTitle().equals(oldCreate.get().getTitle())
                                || !creativeRepository.existsByTitleAndDeleteFlagIsFalse(creativesName)){
                            CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
                            if (campaignDTO.getEndDate().toInstant().isAfter(campaignDTO.getStartDate().toInstant())) {
                                // endDateTime after startDateTime
                                campaignService.updateCampagin(campaignId,campaignAndCreativesDTO);
                                return ResponseEntity.status(HttpStatus.OK)
                                        .body(new ResponseMessage<>(AppConstants.CAMPAGIGN_UPDATE_SUCCESS, HTTP_OK, campaignAndCreativesDTO));
                            } else {
                                // endDateTime not after startDateTime
                                return ResponseEntity.status(HttpStatus.OK)
                                        .body(new ResponseMessage<>(AppConstants.STARTDATE_IS_AFTER_ENDDATE, HTTP_BAD_REQUEST));
                            }
                        } else {
                            return ResponseEntity.status(HttpStatus.OK)
                                    .body(new ResponseMessage<>(AppConstants.CREATIVES_ALREADY_EXISTS, HTTP_BAD_REQUEST));
                        }
                        //check if creatives is present: false
                    } else {
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseMessage<>(AppConstants.CREATIVES_NOT_FOUND, HTTP_BAD_REQUEST));
                    }
                //if campagin present: false
            } else{
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(AppConstants.CAMPAIGN_NOT_FOUND, HTTP_BAD_REQUEST));
            }
        } else{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.CAMPAIGN_ID_INVALID, HTTP_BAD_REQUEST));
        }
    }

    @PostMapping(value = "/createCampagin",consumes = { "multipart/form-data" })
    public ResponseEntity<ResponseMessage<CampaignAndCreativesDTO>> createCamapagin(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") CampaignAndCreativesDTO campaignAndCreativesDTO,
            @RequestHeader("Authorization") String bearerToken) throws IOException {

        //Delete "bearer" in token
        bearerToken = bearerToken.replace(AuthConstants.BEARER_PREFIX, "");
        final String currenAccount = jwtService.extractUsername(bearerToken);
        Pageable pageable = PageRequest.of(Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER), Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE));
        Page<Account> accountPage;
        try{
            accountPage = accountRepository.findAccountByEmailOrName(currenAccount, pageable);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_NOT_FOUND, HTTP_BAD_REQUEST));
        }

        List<Account> accounts = accountPage.getContent();
        Account account = accounts.get(0);
        //check if campagin exist
        boolean isExistCampaign = campaignRepository.existsByNameAndDeleteFlagIsFalse(campaignAndCreativesDTO.getCampaignDTO().getName());
        if(!isExistCampaign){
            boolean isExistCreative = creativeRepository.existsByTitleAndDeleteFlagIsFalse(campaignAndCreativesDTO.getCreativesDTO().getTitle());
            if(!isExistCreative){
                CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
                if (campaignDTO.getEndDate().toInstant().isAfter(campaignDTO.getStartDate().toInstant())) {
                    // endDateTime after startDateTime
                    String imgurl = firebaseService.uploadFile(file);
                    campaignAndCreativesDTO.getCreativesDTO().setImageUrl(imgurl);
                    campaignService.createCampaign(campaignAndCreativesDTO, account);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(AppConstants.CAMPAIGN_CREATE_SUCCESS, HTTP_OK,campaignAndCreativesDTO));
                } else {
                    // endDateTime not after startDateTime
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseMessage<>(AppConstants.STARTDATE_IS_AFTER_ENDDATE, HTTP_BAD_REQUEST));
                }

            } else{
                return  ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(AppConstants.CREATIVES_ALREADY_EXISTS, HTTP_BAD_REQUEST));
            }
        } else{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.CAMPAGIGN_ALREADY_EXISTS, HTTP_BAD_REQUEST));
        }
    }



}
