package umc.domain.mission.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.domain.member.entity.Member;
import umc.domain.member.exception.MemberException;
import umc.domain.member.exception.code.MemberErrorCode;
import umc.domain.member.repository.MemberRepository;
import umc.domain.mission.converter.MissionConverter;
import umc.domain.mission.dto.MissionResDTO;
import umc.domain.mission.entity.Mission;
import umc.domain.mission.entity.mapping.MemberMission;
import umc.domain.mission.enums.MissionStatus;
import umc.domain.mission.exception.MissionException;
import umc.domain.mission.exception.code.MissionErrorCode;
import umc.domain.mission.repository.MemberMissionRepository;
import umc.domain.mission.repository.MissionRepository;
import umc.domain.store.entity.Store;
import umc.domain.store.exception.StoreException;
import umc.domain.store.exception.code.StoreErrorCode;
import umc.domain.store.repository.StoreRepository;

@Service
@RequiredArgsConstructor
public class MissionQueryService {

	private final MemberMissionRepository memberMissionRepository;
	private final MemberRepository memberRepository;
	private final MissionConverter missionConverter;
	private final MissionRepository missionRepository;
	private final StoreRepository storeRepository;

	// 미션 조회
	@Transactional(readOnly = true)
	public MissionResDTO.MissionListDTO getMissions(Long memberId, MissionStatus status, Integer page) {

		Member member =  memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));


		Pageable pageable = PageRequest.of(page, 10);

		Page<MemberMission> memberMissions = memberMissionRepository.findMyMissions(memberId, status, pageable);

		return missionConverter.toMissionListDTO(memberMissions);
	}

	// 가게 미션 조회
	public MissionResDTO.Pagination<MissionResDTO.GetMission> getStoreMissions(
		Long storeId,
		Integer pageSize,
		String cursor,
		String query
	) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(()-> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
		Sort sortInfo;



		PageRequest pageRequest = PageRequest.of(0, pageSize);

		long idCursor;
		Slice<Mission> missionList;
		String nextCursor;

		if(!cursor.equals("-1")) {
			String[] cursorSplit = cursor.split(":");
			switch(query.toLowerCase()) {
				case "id":
					Long prevCursor = Long.parseLong(cursorSplit[0]);
					idCursor = Long.parseLong(cursorSplit[1]);

					missionList = missionRepository.findMissionsByStore_IdAndIdLessThanOrderByIdDesc(
						storeId,
						idCursor,
						pageRequest
					);
					break;
			default:
				throw new MissionException(MissionErrorCode.MISSION_NOT_FOUND);
			}

		}

		missionList = missionRepository.findMissionsByStore_IdOrderByIdDesc(storeId, pageRequest);
		nextCursor = missionList.getContent().getLast().getId() + ":" + missionList.getContent().getLast().getId();

		return MissionConverter.toPagination(
			missionList.map(MissionConverter::toGetMission).toList(),
			missionList.hasNext(),
			nextCursor,
			missionList.getSize()
		);
	}
}
