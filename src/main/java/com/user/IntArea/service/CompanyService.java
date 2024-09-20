package com.user.IntArea.service;

import com.user.IntArea.common.utils.ImageUtil;
import com.user.IntArea.common.utils.SecurityUtil;
import com.user.IntArea.dto.company.*;
import com.user.IntArea.dto.image.ImageDto;
import com.user.IntArea.dto.member.MemberDto;
import com.user.IntArea.dto.member.MemberResponseDto;
import com.user.IntArea.entity.Company;
import com.user.IntArea.entity.Image;
import com.user.IntArea.entity.Member;
import com.user.IntArea.entity.enums.Role;
import com.user.IntArea.repository.CompanyRepository;
import com.user.IntArea.repository.ImageRepository;
import com.user.IntArea.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final ImageUtil imageUtil;

    private Company getCompanyOfMember() {
        MemberDto memberDto = SecurityUtil.getCurrentMember().orElseThrow(() -> new NoSuchElementException(""));
        String email = memberDto.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(""));
        Company company = companyRepository.getCompanyByMember(member).orElseThrow(() -> new NoSuchElementException(""));
        return company;
    }

    @Transactional
    public void create(CompanyRequestDto companyRequestDto) {

        MemberDto memberDto = SecurityUtil.getCurrentMember()
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 없습니다. 다시 시도해주세요."));
        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("잘못된 이메일 주소입니다."));

        Company saveCompany = companyRequestDto.toEntity(member);

        UUID refId = companyRepository.save(saveCompany).getId();
        member.setRole(Role.ROLE_SELLER);
        memberRepository.save(member);

        if (!companyRequestDto.getImage().isEmpty()) {
            ImageDto imageDto = imageUtil.uploadS3(companyRequestDto.getImage(), refId, 0)
                    .orElseThrow(() -> new NoSuchElementException("S3 오류"));

            imageRepository.save(imageDto.toImage());
        }
    }

    @Transactional
    public void update(CompanyRequestDto companyRequestDto) {

        Company company = getCompanyOfMember();
        company.setCompanyName(companyRequestDto.getCompanyName());
        company.setDescription(companyRequestDto.getDescription());
        company.setPhone(companyRequestDto.getPhone());
        company.setAddress(companyRequestDto.getAddress());
        company.setDetailAddress(companyRequestDto.getDetailAddress());
        companyRepository.save(company);

        // 이미지가 있으면 기존 이미지 삭제 & 저장
        if (!companyRequestDto.getImage().isEmpty()) {
            imageRepository.findByRefId(company.getId())
                    .ifPresent(imageRepository::delete);

            ImageDto imageDto = imageUtil.uploadS3(companyRequestDto.getImage(), company.getId(), 0)
                    .orElseThrow(() -> new NoSuchElementException("S3 오류"));

            imageRepository.save(imageDto.toImage());
        }
    }

    public CompanyPortfolioDetailDto getCompanyById(UUID id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회사가 없습니다."));

        String imageUrl = imageRepository.findByRefId(id)
                .orElseGet(Image::new).getUrl();

        return new CompanyPortfolioDetailDto(company, imageUrl);
    }

    public CompanyPortfolioDetailDto getCompanyInfo() {

        Company company = getCompanyOfMember();

        Optional<Image> image = imageRepository.findByRefId(company.getId());
        String url = image.map(Image::getUrl).orElse(null);

        return new CompanyPortfolioDetailDto(company, url);
    }

    public Page<UnAppliedCompanyDto> getUnApply(Pageable pageable) {

        return companyRepository.getCompanyByIsApplied(false, pageable)
                .map(company -> {
                    Optional<Image> optionalImage = imageRepository.findByRefId(company.getId());

                    return optionalImage.map(image ->
                                    new UnAppliedCompanyDto(company.getMember(), company, image.getUrl()))
                            .orElseGet(() -> new UnAppliedCompanyDto(company.getMember(), company));
                });
    }

    @Transactional
    public void apply(UUID id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("잘못된 회사 번호입니다."));
        company.setIsApplied(true);

        Member member = company.getMember();
        member.setRole(Role.ROLE_SELLER);

        companyRepository.save(company);
    }

    // 회사 전체 리스트 출력
    public List<CompanyResponseDto> getAllCompanies(CompanyRequestDto companyRequestDto) {
        List<Company> companies = companyRepository.findAll();

        return companies.stream()
                .map(this::converToDto)
                .toList();
    }

    private CompanyResponseDto converToDto(Company company) {
        return new CompanyResponseDto(
                company.getId(),
                company.getCompanyName(),
                company.getDescription(),
                company.getPhone(),
                company.getAddress(),
                company.getDetailAddress(),
                company.getIsApplied(),
                company.getCreatedAt());
    }

    public Page<CompanyResponseDto> getAllCompany(Pageable pageable) {
        return companyRepository.findAll(pageable).map(this::converToDto);
    }

    public Page<CompanyResponseDto> getCompanyListByFilter(Pageable pageable, Optional<String> filterColumn, Optional<String> filterValue) {
        if (filterValue.isPresent() && filterColumn.isPresent()) {
            switch (filterColumn.get()) {
                case "companyName" -> {
                    return companyRepository.findAllByCompanyNameContains(filterValue.get(), pageable).map(CompanyResponseDto::new);
                }
                case "description" -> {
                    return companyRepository.findAllByDescriptionContains(filterValue.get(), pageable).map(CompanyResponseDto::new);
                }
                case "phone" -> {
                    return companyRepository.findAllByPhoneContains(filterValue.get(), pageable).map(CompanyResponseDto::new);
                }
                case "address" -> {
                    return companyRepository.findAllByAddressContains(filterValue.get(), pageable).map(CompanyResponseDto::new);
                }
                case "detailAddress" -> {
                    return companyRepository.findAllByDetailAddressContains(filterValue.get(), pageable).map(CompanyResponseDto::new);
                }
                case "updatedAt" -> {
                    return companyRepository.findAllByUpdatedAtContains(filterValue.get(), pageable).map(CompanyResponseDto::new);
                }
                case "isApplied" -> {
                    if (filterValue.get().equals("true")) {
                        return companyRepository.findAllByIsAppliedIs(true, pageable).map(CompanyResponseDto::new);
                    } else {
                        return companyRepository.findAllByIsAppliedIs(false, pageable).map(CompanyResponseDto::new);
                    }
                }
            }
        } else {
            return companyRepository.findAll(pageable).map(CompanyResponseDto::new);
        }
        throw new RuntimeException("getCompanyListByFilter");
    }

    @Transactional
    public void softDeleteCompanies(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        companyRepository.softDeleteByIds(ids);
    }

    @Transactional
    public void hardDeleteCompanies(List<String> idList) {
        List<UUID> ids = idList.stream().map(UUID::fromString).toList();
        companyRepository.deleteAllById(ids);
    }

    @Transactional
    public void editCompanyForAdmin(EditCompanyDto editCompanyDto) {
        Optional<Company> company = companyRepository.findById(editCompanyDto.getId());
        if (company.isPresent()) {
            company.get().setCompanyName(editCompanyDto.getCompanyName());
            company.get().setAddress(editCompanyDto.getAddress());
            company.get().setPhone(editCompanyDto.getPhone());
            company.get().setIsApplied(editCompanyDto.isApplied());
            company.get().setDescription(editCompanyDto.getDescription());
            company.get().setDetailAddress(editCompanyDto.getDetailAddress());
        } else {
            throw new RuntimeException("editCompany");
        }
    }

    @Transactional
    public List<CompanyWithImageDto> findTopCompaniesByQuotationCount(int count) {
        Pageable pageable = PageRequest.ofSize(count);
        return companyRepository.findTopCompaniesByQuotationCount(pageable).stream()
                .map(company -> {
                    Optional<Image> optionalImage = imageRepository.findByRefId(company.getId());
                    return optionalImage.map(image -> new CompanyWithImageDto(company,image))
                            .orElse(new CompanyWithImageDto(company));
                }).toList();
    }

}
