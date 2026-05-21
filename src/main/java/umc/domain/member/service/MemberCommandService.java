package umc.domain.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import umc.domain.member.converter.MemberConverter;
import umc.domain.member.dto.MemberReqDTO;
import umc.domain.member.entity.Member;
import umc.domain.member.entity.Term;
import umc.domain.member.entity.mapping.MemberFood;
import umc.domain.member.entity.mapping.MemberTerm;
import umc.domain.member.enums.TermStatus;
import umc.domain.member.exception.MemberException;
import umc.domain.member.exception.TermException;
import umc.domain.member.exception.code.MemberErrorCode;
import umc.domain.member.exception.code.TermErrorCode;
import umc.domain.member.repository.MemberFoodRepository;
import umc.domain.member.repository.MemberRepository;
import umc.domain.member.repository.MemberTermRepository;
import umc.domain.member.repository.TermRepository;
import umc.domain.store.entity.Food;
import umc.domain.store.exception.StoreException;
import umc.domain.store.exception.code.StoreErrorCode;
import umc.domain.store.repository.FoodRepository;

@Service
@RequiredArgsConstructor
public class MemberCommandService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final FoodRepository foodRepository;
	private final MemberFoodRepository memberFoodRepository;
	private final TermRepository termRepository;
	private final MemberTermRepository memberTermRepository;

	@Transactional
	public Member joinMember(MemberReqDTO.JoinDTO request) {

		// 1. 이메일 중복 검증
		if(memberRepository.existsByEmail(request.getEmail())) {
			throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
		}

		// 2. DB에서 필수 약관 ID 목록 조회
		List<Long> requiredTermIds = termRepository.findAllByStatus(TermStatus.REQUIRED).stream()
			.map(Term::getId)
			.toList();

		// 사용자가 동의(isAgree == true)한 약관 ID 목록 추출 및 필수 약관 검증
		List<Long> agreedTermIds = (request.getTerms() == null) ? List.of() : request.getTerms().stream()
			.filter(MemberReqDTO.TermDTO::getIsAgree) // true(동의) 한 것만 필터링
			.map(MemberReqDTO.TermDTO::getTermId)
			.toList();

		// 만약 DB의 필수 약관 ID들 중, 사용자가 동의한 ID 목록에 없는 게 하나라도 있다면 예외 처리
		if (!agreedTermIds.containsAll(requiredTermIds)) {
			throw new TermException(TermErrorCode.REQUIRED_TERM_NOT_AGREED);
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());
		Member newMember = MemberConverter.toMember(request, encodedPassword);

		Member savedMember = memberRepository.save(newMember);

		if(request.getFavoriteFoods() != null && request.getFavoriteFoods().size() > 0) {
			List<MemberFood> memberFoodList = request.getFavoriteFoods().stream()
				.map(foodId -> {
					Food food = foodRepository.findById(foodId)
						.orElseThrow(()-> new StoreException(StoreErrorCode.FOOD_NOT_FOUND));

					return MemberFood.builder()
						.member(savedMember)
						.food(food)
						.build();
				}).collect(Collectors.toList());

			memberFoodRepository.saveAll(memberFoodList);
		}

		if(request.getTerms() != null && request.getTerms().size() > 0) {
			List<MemberTerm> memberTermList = request.getTerms().stream()
				.map(termDTO -> {
					Term term = termRepository.findById(termDTO.getTermId())
						.orElseThrow(()-> new TermException(TermErrorCode.TERM_NOT_FOUND));

					return MemberTerm.builder()
						.member(savedMember)
						.term(term)
						.isAgreed(termDTO.getIsAgree())
						.build();
				}).collect(Collectors.toList());

			memberTermRepository.saveAll(memberTermList);
		}
		return savedMember;
	}
}
