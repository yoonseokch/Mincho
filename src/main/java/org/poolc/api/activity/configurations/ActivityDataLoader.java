package org.poolc.api.activity.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.ActivityTag;
import org.poolc.api.activity.dto.AttendanceRequest;
import org.poolc.api.activity.dto.SessionCreateRequest;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.activity.service.SessionService;
import org.poolc.api.activity.vo.AttendanceValues;
import org.poolc.api.activity.vo.SessionCreateValues;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
//@Profile("activityTest")
@RequiredArgsConstructor
public class ActivityDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final ActivityService activityService;
    private final SessionService sessionService;

    @Override
    public void run(String... args) {
        Member member = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("MEMBER_ID")
                .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD"))
                .email("example@email.com")
                .phoneNumber("010-4444-4444")
                .name("MEMBER_NAME")
                .department("exampleDepartment")
                .studentID("2021147593")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("")
                .isExcepted(false)
                .roles(new HashSet<>() {{
                    add(MemberRole.MEMBER);
                }})
                .build();
        Member member2 = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("MEMBER_ID2")
                .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD2"))
                .email("example@email.com2")
                .phoneNumber("010-4444-4442")
                .name("MEMBER_NAME2")
                .department("exampleDepartment2")
                .studentID("2021147521")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("")
                .isExcepted(false)
                .roles(new HashSet<>() {{
                    add(MemberRole.MEMBER);
                }})
                .build();
        Member member3 = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("MEMBER_ID3")
                .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD3"))
                .email("example@email.com3")
                .phoneNumber("010-4444-4443")
                .name("MEMBER_NAME3")
                .department("exampleDepartment3")
                .studentID("2021147522")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("")
                .isExcepted(false)
                .roles(new HashSet<>() {{
                    add(MemberRole.MEMBER);
                    add(MemberRole.ADMIN);
                }})
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        LocalDate date = LocalDate.now();
        LocalDate date2 = LocalDate.of(2020, 9, 11);
        Activity activity = new Activity("정윤석의 c++ ", "dsds", member, date, "dsds", false, 30l, 3l, true);
        activity.getTags().add(new ActivityTag(activity, "꿀잼보장"));
        activityRepository.save(activity);
        Activity activity2 = new Activity("정윤석의 c++ 2", "dsds", member, date2, "dsds", false, 1l, 2l, false);
        Activity activity3 = new Activity("정윤석의 c++ 3", "dsds", member, date, "dsds", false, 1l, 2l, false);
        Activity activity4 = new Activity("정윤석의 c++ 4", "dsds", member, date, "dsds", false, 40l, 2l, true);

        activity2.getTags().add(new ActivityTag(activity2, "꿀잼보장"));
        activity2.getTags().add(new ActivityTag(activity2, "ㄹㅇ로"));
        activityRepository.save(activity2);
        activityRepository.save(activity3);
        activityRepository.save(activity4);

        sessionService.createSession(memberRepository.findByLoginID("MEMBER_ID").get(), new SessionCreateValues(new SessionCreateRequest(1l, 1l, LocalDate.now(), "1")));
        activityService.apply(1l, memberRepository.findByLoginID("MEMBER_ID2").get().getUUID());
        activityService.apply(1l, memberRepository.findByLoginID("MEMBER_ID3").get().getUUID());
        List<String> list = new ArrayList<>();
        list.add("MEMBER_ID2");
        list.add("MEMBER_ID3");
        sessionService.createSession(memberRepository.findByLoginID("MEMBER_ID").get(), new SessionCreateValues(new SessionCreateRequest(1l, 2l, LocalDate.now(), "1")));
        sessionService.attend(memberRepository.findByLoginID("MEMBER_ID").get().getUUID(), new AttendanceValues(new AttendanceRequest(8l, list)));
        List<String> list2 = new ArrayList<>();
        list2.add("MEMBER_ID3");
        activityService.openActivity(3l);
        activityService.apply(3l, memberRepository.findByLoginID("MEMBER_ID2").get().getUUID());
        sessionService.attend(memberRepository.findByLoginID("MEMBER_ID").get().getUUID(), new AttendanceValues(new AttendanceRequest(9l, list2)));
        sessionService.createSession(memberRepository.findByLoginID("MEMBER_ID").get(), new SessionCreateValues(new SessionCreateRequest(3l, 1l, LocalDate.now(), "1")));
        List<String> list3 = new ArrayList<>();
        list3.add("MEMBER_ID2");
        sessionService.attend(memberRepository.findByLoginID("MEMBER_ID").get().getUUID(), new AttendanceValues(new AttendanceRequest(10l, list3)));


    }
}
