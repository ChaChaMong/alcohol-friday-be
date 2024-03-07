package com.drunkenlion.alcoholfriday.domain.admin.notice.api;

import com.drunkenlion.alcoholfriday.domain.admin.customerservice.notice.api.AdminNoticeController;
import com.drunkenlion.alcoholfriday.domain.auth.enumerated.ProviderType;
import com.drunkenlion.alcoholfriday.domain.customerservice.notice.dao.NoticeRepository;
import com.drunkenlion.alcoholfriday.domain.customerservice.notice.entity.Notice;
import com.drunkenlion.alcoholfriday.domain.member.dao.MemberRepository;
import com.drunkenlion.alcoholfriday.domain.member.entity.Member;
import com.drunkenlion.alcoholfriday.global.user.WithAccount;
import com.drunkenlion.alcoholfriday.global.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import static com.drunkenlion.alcoholfriday.domain.member.enumerated.MemberRole.ADMIN;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@Transactional
@WithAccount
@SpringBootTest
public class AdminNoticeControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    @Transactional
    void beforeEach() {
        Member member = Member.builder()
                .email("smileby95@nate.com")
                .provider(ProviderType.KAKAO)
                .name("김태섭")
                .nickname("seop")
                .role(ADMIN)
                .phone(1041932693L)
                .certifyAt(null)
                .agreedToServiceUse(true)
                .agreedToServicePolicy(true)
                .agreedToServicePolicyUse(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .build();

        memberRepository.save(member);

        Notice notice = noticeRepository.save(
                Notice.builder()
                        .title("test title")
                        .content("test content")
                        .member(member)
                        .build());
        noticeRepository.save(notice);
    }

    @AfterEach
    @Transactional
    void afterEach() {
        noticeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("공지사항 목록 조회 성공")
    @Test
    void getNoticesTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/v1/admin/notices")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(AdminNoticeController.class))
                .andExpect(handler().methodName("getNotices"))
                .andExpect(jsonPath("$.data", instanceOf(List.class)))
                .andExpect(jsonPath("$.data.length()", is(1)))
                .andExpect(jsonPath("$.data[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data[0].member", notNullValue()))
                .andExpect(jsonPath("$.data[0].title", notNullValue()))
                .andExpect(jsonPath("$.data[0].content", notNullValue()))
                .andExpect(jsonPath("$.data[0].createdAt", matchesPattern(TestUtil.DATETIME_PATTERN)))
                .andExpect(jsonPath("$.data[0].updatedAt", matchesPattern(TestUtil.DATETIME_PATTERN)))
                .andExpect(jsonPath("$.data[0].deletedAt", anyOf(is(matchesPattern(TestUtil.DATETIME_PATTERN)), is(nullValue()))))
                .andExpect(jsonPath("$.pageInfo", instanceOf(LinkedHashMap.class)))
                .andExpect(jsonPath("$.pageInfo.size", notNullValue()))
                .andExpect(jsonPath("$.pageInfo.count", notNullValue()));
    }

    @DisplayName("공지사항 상세 조회 성공")
    @Test
    void getNoticeTest() throws Exception {
        // given
        // noticeRepo 에서 첫 번째 아이템을 찾아와 notice 변수에 저장
        Notice notice = noticeRepository.findAll().get(0);

        // when
        // MockMvc "/v1/admin/notices/{notice.id}" 경로로 GET 요청을 보내고 resultActions 변수에 저장
        ResultActions resultActions = mvc
                .perform(get("/v1/admin/notices/" + notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(AdminNoticeController.class))
                .andExpect(handler().methodName("getNotice"))
                .andExpect(jsonPath("$", instanceOf(LinkedHashMap.class)))
                .andExpect(jsonPath("$.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.member", notNullValue()))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.createdAt", matchesPattern(TestUtil.DATETIME_PATTERN)))
                .andExpect(jsonPath("$.updatedAt", matchesPattern(TestUtil.DATETIME_PATTERN)))
                .andExpect(jsonPath("$.deletedAt", anyOf(is(matchesPattern(TestUtil.DATETIME_PATTERN)), is(nullValue()))));
    }

    @DisplayName("공지사항 등록 성공")
    @Test
    void saveNoticeTest() throws Exception {

        ResultActions resultActions = mvc
                .perform(post("/v1/admin/notices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "test title",
                                  "content": "test content",
                                  "memberId": 1
                                }
                                """)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(AdminNoticeController.class))
                .andExpect(handler().methodName("saveNotice"))
                .andExpect(jsonPath("$.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.member", notNullValue()))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.createdAt", matchesPattern(TestUtil.DATETIME_PATTERN)))
                .andExpect(jsonPath("$.updatedAt", matchesPattern(TestUtil.DATETIME_PATTERN)))
                .andExpect(jsonPath("$.deletedAt", anyOf(is(matchesPattern(TestUtil.DATETIME_PATTERN)), is(nullValue()))));
    }

    @DisplayName("공지사항 수정 성공")
    @Test
    void modifyNoticeTest() throws Exception {

        Notice notice = noticeRepository.findAll().get(0);

        ResultActions resultActions = mvc
                .perform(put("/v1/admin/notices/" + notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "test title modified",
                                  "content": "test content modified"
                                }
                                """)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(AdminNoticeController.class))
                .andExpect(handler().methodName("modifyNotice"))
                .andExpect(jsonPath("$.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.member", notNullValue()))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.content", notNullValue()))
                .andExpect(jsonPath("$.createdAt", matchesPattern(TestUtil.DATETIME_PATTERN)))
                .andExpect(jsonPath("$.updatedAt", matchesPattern(TestUtil.DATETIME_PATTERN)))
                .andExpect(jsonPath("$.deletedAt", anyOf(is(matchesPattern(TestUtil.DATETIME_PATTERN)), is(nullValue()))));
    }

    @DisplayName("공지사항 삭제 성공")
    @Test
    void deleteNoticeTest() throws Exception {

        Notice notice = noticeRepository.findAll().get(0);

        ResultActions resultActions = mvc
                .perform(delete("/v1/admin/notices/" + notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isNoContent())
                .andExpect(handler().handlerType(AdminNoticeController.class))
                .andExpect(handler().methodName("deleteNotice"));
    }
}
