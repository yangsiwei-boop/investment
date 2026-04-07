package com.investment.service;

import com.investment.common.exception.BusinessException;
import com.investment.common.exception.ErrorCode;
import com.investment.dto.request.profile.*;
import com.investment.dto.response.profile.*;
import com.investment.entity.*;
import com.investment.enums.UserType;
import com.investment.enums.VerificationStatus;
import com.investment.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户资料服务
 *
 * @author Investment Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final InvestorProfileRepository investorProfileRepository;
    private final EntrepreneurProfileRepository entrepreneurProfileRepository;
    private final UserVerificationRepository verificationRepository;
    private final PrivacySettingRepository privacySettingRepository;

    /**
     * 获取用户基本信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public UserProfileResponse getUserProfile(Long userId) {
        log.info("Getting user profile: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return convertToUserProfileResponse(user);
    }

    /**
     * 更新用户基本信息
     *
     * @param userId  用户ID
     * @param request 请求
     * @return 用户信息
     */
    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UserProfileUpdateRequest request) {
        log.info("Updating user profile: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 更新基本信息
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        userRepository.save(user);

        return convertToUserProfileResponse(user);
    }

    /**
     * 获取投资人资料
     *
     * @param userId 用户ID
     * @return 投资人资料
     */
    public InvestorProfileResponse getInvestorProfile(Long userId) {
        log.info("Getting investor profile: {}", userId);

        InvestorProfile profile = investorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        return convertToInvestorProfileResponse(profile);
    }

    /**
     * 更新投资人资料
     *
     * @param userId  用户ID
     * @param request 请求
     * @return 投资人资料
     */
    @Transactional
    public InvestorProfileResponse updateInvestorProfile(Long userId, InvestorProfileUpdateRequest request) {
        log.info("Updating investor profile: {}", userId);

        InvestorProfile profile = investorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        // 更新资料
        if (request.getInstitutionName() != null) {
            profile.setInstitutionName(request.getInstitutionName());
        }
        if (request.getPosition() != null) {
            profile.setPosition(request.getPosition());
        }
        if (request.getInterestedIndustries() != null) {
            profile.setInvestmentIndustries(String.join(",", request.getInterestedIndustries()));
        }
        if (request.getInterestedStages() != null) {
            profile.setInvestmentStage(String.join(",", request.getInterestedStages()));
        }
        if (request.getMinInvestmentAmount() != null) {
            profile.setInvestmentRangeMin(request.getMinInvestmentAmount());
        }
        if (request.getMaxInvestmentAmount() != null) {
            profile.setInvestmentRangeMax(request.getMaxInvestmentAmount());
        }
        if (request.getInterestedRegions() != null) {
            profile.setInvestmentRegion(String.join(",", request.getInterestedRegions()));
        }
        if (request.getInvestmentPhilosophy() != null) {
            profile.setInvestmentPhilosophy(request.getInvestmentPhilosophy());
        }
        if (request.getInvestmentCases() != null) {
            profile.setNotableInvestments(request.getInvestmentCases());
        }

        investorProfileRepository.save(profile);

        return convertToInvestorProfileResponse(profile);
    }

    /**
     * 获取融资用户资料
     *
     * @param userId 用户ID
     * @return 融资用户资料
     */
    public EntrepreneurProfileResponse getEntrepreneurProfile(Long userId) {
        log.info("Getting entrepreneur profile: {}", userId);

        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        return convertToEntrepreneurProfileResponse(profile);
    }

    /**
     * 更新融资用户资料
     *
     * @param userId  用户ID
     * @param request 请求
     * @return 融资用户资料
     */
    @Transactional
    public EntrepreneurProfileResponse updateEntrepreneurProfile(Long userId, EntrepreneurProfileUpdateRequest request) {
        log.info("Updating entrepreneur profile: {}", userId);

        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        // 更新资料
        if (request.getCompanyName() != null) {
            profile.setCompanyName(request.getCompanyName());
        }
        if (request.getIndustry() != null) {
            // industry in entity is IndustryType enum, request is String
            try {
                profile.setIndustry(com.investment.enums.IndustryType.valueOf(request.getIndustry()));
            } catch (IllegalArgumentException ignored) {
                // Invalid industry value, skip
            }
        }
        if (request.getFinancingStage() != null) {
            // companyStage in entity is FinancingStage enum, request is String
            try {
                profile.setCompanyStage(com.investment.enums.FinancingStage.valueOf(request.getFinancingStage()));
            } catch (IllegalArgumentException ignored) {
                // Invalid financing stage value, skip
            }
        }
        if (request.getLocation() != null) {
            profile.setLocation(request.getLocation());
        }
        if (request.getCompanyIntroduction() != null) {
            profile.setIntroduction(request.getCompanyIntroduction());
        }
        if (request.getCoreBusiness() != null) {
            profile.setProductDescription(request.getCoreBusiness());
        }
        if (request.getWebsite() != null) {
            profile.setWebsiteUrl(request.getWebsite());
        }
        if (request.getContactPhone() != null) {
            profile.setContactPhone(request.getContactPhone());
        }
        if (request.getContactEmail() != null) {
            profile.setContactEmail(request.getContactEmail());
        }

        entrepreneurProfileRepository.save(profile);

        return convertToEntrepreneurProfileResponse(profile);
    }

    /**
     * 提交实名认证申请
     *
     * @param userId  用户ID
     * @param request 请求
     * @return 认证ID
     */
    @Transactional
    public Long submitVerification(Long userId, VerificationRequest request) {
        log.info("Submitting verification for user: {}", userId);

        // 检查是否已有待审核的认证
        if (verificationRepository.existsByUserIdAndVerificationStatus(userId, VerificationStatus.PENDING)) {
            throw new BusinessException(ErrorCode.VERIFICATION_PENDING);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 创建认证记录
        UserVerification verification = UserVerification.builder()
                .user(user)
                .realName(request.getRealName())
                .idCardNumber(request.getIdCardNumber())
                .idCardFrontUrl(request.getIdCardFrontUrl())
                .idCardBackUrl(request.getIdCardBackUrl())
                .verificationType(request.getVerificationType())
                .businessLicenseUrl(request.getBusinessLicenseUrl())
                .companyName(request.getCompanyName())
                .verificationStatus(VerificationStatus.PENDING)
                .build();

        verificationRepository.save(verification);

        return verification.getId();
    }

    /**
     * 获取认证状态
     *
     * @param userId 用户ID
     * @return 认证状态
     */
    public String getVerificationStatus(Long userId) {
        return verificationRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .map(v -> v.getVerificationStatus().name())
                .orElse("NOT_SUBMITTED");
    }

    /**
     * 获取隐私设置
     *
     * @param userId 用户ID
     * @return 隐私设置
     */
    public PrivacySetting getPrivacySetting(Long userId) {
        return privacySettingRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPrivacySetting(userId));
    }

    /**
     * 更新隐私设置
     *
     * @param userId  用户ID
     * @param request 请求
     * @return 隐私设置
     */
    @Transactional
    public PrivacySetting updatePrivacySetting(Long userId, PrivacySettingUpdateRequest request) {
        log.info("Updating privacy setting for user: {}", userId);

        PrivacySetting setting = privacySettingRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPrivacySetting(userId));

        // 更新设置
        if (request.getAllowShowCompanyName() != null) {
            setting.setAllowShowCompanyName(request.getAllowShowCompanyName());
        }
        if (request.getAllowShowFoundedTime() != null) {
            setting.setAllowShowFoundedTime(request.getAllowShowFoundedTime());
        }
        if (request.getAllowShowOfficeAddress() != null) {
            setting.setAllowShowOfficeAddress(request.getAllowShowOfficeAddress());
        }
        if (request.getAllowShowContactInfo() != null) {
            setting.setAllowShowContactInfo(request.getAllowShowContactInfo());
        }
        if (request.getAllowShowFinancialData() != null) {
            setting.setAllowShowFinancialData(request.getAllowShowFinancialData());
        }
        if (request.getAllowShowFinancingHistory() != null) {
            setting.setAllowShowFinancingHistory(request.getAllowShowFinancingHistory());
        }
        if (request.getAllowShowFounderDetails() != null) {
            setting.setAllowShowFounderDetails(request.getAllowShowFounderDetails());
        }
        if (request.getAllowShowTeamInfo() != null) {
            setting.setAllowShowTeamInfo(request.getAllowShowTeamInfo());
        }
        if (request.getAllowPublicQa() != null) {
            setting.setAllowPublicQa(request.getAllowPublicQa());
        }
        if (request.getDefaultQaPublic() != null) {
            setting.setDefaultQaPublic(request.getDefaultQaPublic());
        }
        if (request.getQuestionLibraryQaPublic() != null) {
            setting.setQuestionLibraryQaPublic(request.getQuestionLibraryQaPublic());
        }
        if (request.getRequireBpApproval() != null) {
            setting.setRequireBpApproval(request.getRequireBpApproval());
        }
        if (request.getRequireContactApproval() != null) {
            setting.setRequireContactApproval(request.getRequireContactApproval());
        }
        if (request.getAllowViewQaRecords() != null) {
            setting.setAllowViewQaRecords(request.getAllowViewQaRecords());
        }
        if (request.getAllowReceiveQuestions() != null) {
            setting.setAllowReceiveQuestions(request.getAllowReceiveQuestions());
        }
        if (request.getAutoReplyTemplate() != null) {
            setting.setAutoReplyTemplate(request.getAutoReplyTemplate());
        }

        return privacySettingRepository.save(setting);
    }

    /**
     * 创建默认隐私设置
     */
    private PrivacySetting createDefaultPrivacySetting(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return PrivacySetting.builder()
                .user(user)
                .allowShowCompanyName(true)
                .allowShowFoundedTime(true)
                .allowShowOfficeAddress(false)
                .allowShowContactInfo(false)
                .allowShowFinancialData(false)
                .allowShowFinancingHistory(true)
                .allowShowFounderDetails(false)
                .allowShowTeamInfo(true)
                .allowPublicQa(true)
                .defaultQaPublic(true)
                .questionLibraryQaPublic(true)
                .requireBpApproval(true)
                .requireContactApproval(true)
                .allowViewQaRecords(true)
                .allowReceiveQuestions(true)
                .build();
    }

    /**
     * 转换为用户信息响应
     */
    private UserProfileResponse convertToUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .nickname(user.getRealName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .userType(user.getUserType() != null ? user.getUserType().name() : null)
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .isVerified(user.getIsVerified())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * 转换为投资人资料响应
     */
    private InvestorProfileResponse convertToInvestorProfileResponse(InvestorProfile profile) {
        return InvestorProfileResponse.builder()
                .id(profile.getId())
                .institutionName(profile.getInstitutionName())
                .position(profile.getPosition())
                .interestedIndustries(convertStringToList(profile.getInvestmentIndustries()))
                .interestedStages(convertStringToList(profile.getInvestmentStage()))
                .minInvestmentAmount(profile.getInvestmentRangeMin())
                .maxInvestmentAmount(profile.getInvestmentRangeMax())
                .interestedRegions(convertStringToList(profile.getInvestmentRegion()))
                .investmentPhilosophy(profile.getInvestmentPhilosophy())
                .investmentCases(profile.getNotableInvestments())
                .verificationStatus(profile.getIsVerified() != null && profile.getIsVerified() ? "VERIFIED" : "PENDING")
                .completeness(calculateCompleteness(profile))
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    /**
     * 将逗号分隔的字符串转换为列表
     */
    private List<String> convertStringToList(String str) {
        if (str == null || str.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return java.util.Arrays.asList(str.split(","));
    }

    /**
     * 转换为融资用户资料响应
     */
    private EntrepreneurProfileResponse convertToEntrepreneurProfileResponse(EntrepreneurProfile profile) {
        return EntrepreneurProfileResponse.builder()
                .id(profile.getId())
                .companyName(profile.getCompanyName())
                .industry(profile.getIndustry() != null ? profile.getIndustry().name() : null)
                .financingStage(profile.getCompanyStage() != null ? profile.getCompanyStage().name() : null)
                .location(profile.getLocation())
                .companyIntroduction(profile.getIntroduction())
                .coreBusiness(profile.getProductDescription())
                .website(profile.getWebsiteUrl())
                .contactPhone(profile.getContactPhone())
                .contactEmail(profile.getContactEmail())
                .completeness(calculateEntrepreneurCompleteness(profile))
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    /**
     * 计算资料完整度
     */
    private int calculateCompleteness(InvestorProfile profile) {
        int total = 10;
        int filled = 0;

        if (profile.getInstitutionName() != null && !profile.getInstitutionName().isEmpty()) filled++;
        if (profile.getPosition() != null && !profile.getPosition().isEmpty()) filled++;
        if (profile.getInvestmentIndustries() != null && !profile.getInvestmentIndustries().isEmpty()) filled++;
        if (profile.getInvestmentStage() != null && !profile.getInvestmentStage().isEmpty()) filled++;
        if (profile.getInvestmentRangeMin() != null) filled++;
        if (profile.getInvestmentRangeMax() != null) filled++;
        if (profile.getInvestmentRegion() != null && !profile.getInvestmentRegion().isEmpty()) filled++;
        if (profile.getInvestmentPhilosophy() != null && !profile.getInvestmentPhilosophy().isEmpty()) filled++;
        if (profile.getNotableInvestments() != null && !profile.getNotableInvestments().isEmpty()) filled++;
        if (profile.getIsVerified() != null) filled++;

        return (filled * 100) / total;
    }

    /**
     * 计算融资用户资料完整度
     */
    private int calculateEntrepreneurCompleteness(EntrepreneurProfile profile) {
        int total = 10;
        int filled = 0;

        if (profile.getCompanyName() != null && !profile.getCompanyName().isEmpty()) filled++;
        if (profile.getIndustry() != null) filled++;
        if (profile.getCompanyStage() != null) filled++;
        if (profile.getLocation() != null && !profile.getLocation().isEmpty()) filled++;
        if (profile.getTeamSize() != null) filled++;
        if (profile.getIntroduction() != null && !profile.getIntroduction().isEmpty()) filled++;
        if (profile.getProductDescription() != null && !profile.getProductDescription().isEmpty()) filled++;
        if (profile.getWebsiteUrl() != null && !profile.getWebsiteUrl().isEmpty()) filled++;
        if (profile.getContactPhone() != null && !profile.getContactPhone().isEmpty()) filled++;
        if (profile.getContactEmail() != null && !profile.getContactEmail().isEmpty()) filled++;

        return (filled * 100) / total;
    }
}
