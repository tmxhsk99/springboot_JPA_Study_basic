package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    //버전에 따라 의존성을 주입하려면 @PersistenceContext 애노테이션을 써야한다, 현재는 springboot jpa가 @Autowired 해도 의존성 주입이 되게하기때문에 사용하도록 변경 

    private final EntityManager em;

    /**
     * Member 단건 저장
     *
     * @param member
     */
    public void save(Member member) {
        em.persist(member);
    }

    /**
     * Member 단건 조회
     *
     * @param memberId
     * @return
     */
    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }

    /**
     * Member 전체 조회
     *
     * @return List<Member>
     */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    /**
     * 회원 이름으로 Member 검색
     * @param name
     * @return
     */
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :username", Member.class)
                .setParameter("username", name)
                .getResultList();
    }

}
