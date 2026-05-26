package umc.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.domain.member.converter.MemberConverter;
import umc.domain.member.dto.MemberReqDTO;
import umc.domain.member.dto.MemberResDTO;
import umc.domain.member.entity.Member;
import umc.domain.member.exception.MemberException;
import umc.domain.member.exception.code.MemberErrorCode;
import umc.domain.member.repository.MemberRepository;
import umc.domain.mission.entity.Mission;
import umc.domain.mission.enums.MissionStatus;
import umc.domain.mission.repository.MemberMissionRepository;
import umc.domain.mission.repository.MissionRepository;
import umc.domain.store.exception.StoreException;
import umc.domain.store.exception.code.StoreErrorCode;
import umc.domain.store.repository.RegionRepository;
import umc.global.security.entity.AuthMember;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

	private final MemberRepository memberRepository;
	private final MemberMissionRepository memberMissionRepository;
	private final RegionRepository regionRepository;
	private final MissionRepository missionRepository;

	@Transactional(readOnly = true)
	public MemberResDTO.GetInfo getInfo(AuthMember authMember) {

		Member member =  memberRepository.findById(authMember.getMember().getId())
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		return MemberConverter.toGetInfo(member);
	}

	// 홈화면
	@Transactional(readOnly = true)
	public MemberResDTO.HomeResponseDTO getHomeData(Long memberId, String regionName, Integer page) {

		Member member =  memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		if (!regionRepository.existsByName(regionName)) {
			throw new StoreException(StoreErrorCode.REGION_NOT_FOUND);
		}

		Integer completeCount = memberMissionRepository.countCompletedMissions(memberId, MissionStatus.SUCCESS);

		Page<Mission> missionPage = missionRepository.findAvailableMissionsByRegion(memberId, regionName, PageRequest.of(page, 10));

		return MemberConverter.toHomeResponseDTO(
			regionName,
			member.getPoint(),
			completeCount,
			missionPage
		);

	}

}
