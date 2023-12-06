package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.constants.AppConstants;
import com.dtvn.springbootproject.constants.HttpConstants;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.services.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.dtvn.springbootproject.constants.HttpConstants.HTTP_BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService  campaignService;

    @GetMapping("/getCampaign")
    public ResponseEntity<ResponseMessage<Page<CampaignDTO> >> getCampaign(
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
}
