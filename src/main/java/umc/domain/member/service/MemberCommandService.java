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
import umc.domain.member.exception.MemberException;
import umc.domain.member.exception.code.MemberErrorCode;
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
						.orElseThrow(()-> new MemberException(MemberErrorCode.TERM_NOT_FOUND));

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
