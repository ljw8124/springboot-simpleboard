package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.sb.entity.Member;
import org.zerock.sb.entity.MemberRole;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1,100).forEach(i -> {

            Set<MemberRole> memberRoleSet = new HashSet<>();
            memberRoleSet.add(MemberRole.USER);
            if (i >= 50) {
                memberRoleSet.add(MemberRole.STORE);
            }
            if(i >= 80) {
                memberRoleSet.add(MemberRole.ADMIN);
            }

            Member member = Member.builder()
                    .mid("user" + i)
                    .mpw("1111")
                    .mname("사용자" + i)
                    .roleSet(memberRoleSet)
                    .build();

            memberRepository.save(member);

        });
    }

    @Test
    public void updateMemberTest() {

        List<Member> memberList = memberRepository.findAll();

        memberList.forEach(member -> {
            member.changePassword(passwordEncoder.encode("1111"));

            memberRepository.save(member);
        });
    }

    @Test
    public void findByIdWithRoleTest() {

        String mid = "user100";

        Optional<Member> memberOptional = memberRepository.getMemberEager(mid);

        log.info(memberOptional);

    }


}
