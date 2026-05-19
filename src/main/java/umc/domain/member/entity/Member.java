package umc.domain.member.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.domain.member.entity.mapping.MemberFood;
import umc.domain.member.enums.Gender;
import umc.domain.member.enums.MemberStatus;
import umc.domain.member.enums.SocialType;
import umc.domain.mission.entity.mapping.MemberMission;
import umc.domain.mission.enums.Address;
import umc.global.apiPayload.entity.BaseEntity;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true, length = 50)
	private String name;

	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = true)
	private LocalDate birth;

	@Column(nullable = false, length = 200)
	@Enumerated(EnumType.STRING)
	private Address address;

	@Column(nullable = false, length = 100)
	private String email;

	@Builder.Default
	@Column(nullable = false, length = 20)
	private String phoneNumber = "";

	@Builder.Default
	private Boolean isPhoneVerified = false;

	@Builder.Default
	private Integer point = 0;

	@Column(nullable = true, length = 100)
	private String profileUrl;

	@Column(nullable = true, length = 30)
	@Enumerated(EnumType.STRING)
	private SocialType socialType;

	@Builder.Default
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private MemberStatus memberStatus = MemberStatus.ACTIVE;

	private String password;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MemberMission> memberMissionList = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MemberFood> memberFoodList = new ArrayList<>();

}
