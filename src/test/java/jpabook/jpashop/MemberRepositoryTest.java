package jpabook.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(true) //테스트 종료 후 데이터를 롤백하지 않음
    public void testMember() throws Exception{
        Member member = new Member();
        member.setUsername("user1");

        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(),member.getUsername());
        assertEquals(findMember,member);

    }

}