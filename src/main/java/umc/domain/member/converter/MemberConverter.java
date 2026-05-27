package umc.domain.member.converter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import umc.domain.member.dto.MemberReqDTO;
import umc.domain.member.dto.MemberResDTO;
import umc.domain.member.entity.Member;
import umc.domain.member.enums.Gender;
import umc.domain.member.enums.MemberStatus;
import umc.domain.mission.entity.Mission;
import umc.global.security.dto.OAuthDTO;

public class MemberConverter {

	public static MemberResDTO.GetInfo toGetInfo(Member member) {
		return MemberResDTO.GetInfo.builder()
			.email(member.getEmail())
			.name(member.getName())
			.point(member.getPoint())
			.phoneNumber(member.getPhoneNumber())
			.isPhoneVerified(member.getIsPhoneVerified())
			.profileUrl(member.getProfileUrl())
			.build();
	}

	public static MemberResDTO.MissionDetailDTO toMissionDetailDTO(Mission mission) {
		return MemberResDTO.MissionDetailDTO.builder()
			.missionId(mission.getId())
			.storeName(mission.getStore().getName())
			.category(mission.getStore().getFoodCategory().getName())
			.missionContent(mission.getMissionCondition())
			.point(mission.getSuccessPoint())
			.deadline(mission.getDeadline())
			.build();
	}

	public static MemberResDTO.MissionListDTO toMissionListDTO(Page<Mission> missionPage) {
		List<MemberResDTO.MissionDetailDTO> details = missionPage.getContent()
			.stream()
			.map(MemberConverter::toMissionDetailDTO)
			.collect(Collectors.toList());


		return MemberResDTO.MissionListDTO.builder()
			.missionList(details)
			.hasNext(missionPage.hasNext())
			.build();
	}

	public static MemberResDTO.HomeResponseDTO toHomeResponseDTO(
		String regionName,
		Integer totalPoints,
		Integer completedCount,
		Page<Mission> missionPage
	) {
		MemberResDTO.MissionProgressDTO missionProgressDTO = MemberResDTO.MissionProgressDTO.builder()
			.completeCount(completedCount)
			.goalCount(10)
			.rewardPoints(1000)
			.build();

		return MemberResDTO.HomeResponseDTO.builder()
			.regionName(regionName)
			.totalPoints(totalPoints)
			.missionProgress(missionProgressDTO)
			.missionList(toMissionListDTO(missionPage))
			.build();
	}
	public static Member toMember(MemberReqDTO.JoinDTO request, String encodedPassword) {
		return Member.builder()
			.email(request.getEmail())
			.password(encodedPassword)
			.name(request.getName())
			.gender(request.getGender())
			.birth(request.getBirth())
			.address(request.getAddress())
			.build();
	}
	public static MemberResDTO.JoinResultDTO toJoinDTO(Member member) {
		return MemberResDTO.JoinResultDTO.builder()
			.memberId(member.getId())
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static Member toMember(OAuthDTO oAuthDTO) {
		return Member.builder()
			.name(oAuthDTO.getName())
			.email(oAuthDTO.getSocialEmail())
			.socialUid(oAuthDTO.getSocialUid())
			.socialType(oAuthDTO.getSocialType())
			.gender(Gender.NONE)
			.memberStatus(MemberStatus.ACTIVE)
			.build();
	}

	public static MemberResDTO.LoginResultDTO toLogin(String accessToken) {
		return MemberResDTO.LoginResultDTO.builder()
			.accessToken(accessToken)
			.build();
	}
}
