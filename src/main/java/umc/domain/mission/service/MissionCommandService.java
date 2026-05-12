package umc.domain.mission.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.domain.mission.converter.MissionConverter;
import umc.domain.mission.dto.MissionReqDTO;
import umc.domain.mission.entity.Mission;
import umc.domain.mission.repository.MissionRepository;
import umc.domain.store.entity.Store;
import umc.domain.store.exception.StoreException;
import umc.domain.store.exception.code.StoreErrorCode;
import umc.domain.store.repository.StoreRepository;

@Service
@RequiredArgsConstructor
public class MissionCommandService {

	private final MissionRepository missionRepository;
	private final StoreRepository storeRepository;

	// 미션 생성
	@Transactional
	public Void createMission(Long storeId, MissionReqDTO.CreateMissionDTO req) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(()-> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

		Mission newMission = MissionConverter.toMission(store, req);

		missionRepository.save(newMission);

		return null;
	}
}
