package com.apitest.mytest.domainstate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DocumentRevisionTest {

    @Test
    @DisplayName("DRAFT 상태에서 addSection()을 호출하면 성공하고 sections가 늘어난다")
    void addSection_onDraft_succeeds() {
        // 정답:
        // DocumentRevision revision = DocumentRevision.newInstance(1L, 1L);
        // revision.addSection("intro", "hello");
        // assertThat(revision.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("섹션이 하나도 없는 상태에서 publish()를 호출하면 IllegalStateException")
    void publish_withoutSections_throws() {
        // 정답:
        // DocumentRevision revision = DocumentRevision.newInstance(1L, 1L);
        // assertThatThrownBy(revision::publish).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("섹션을 하나 추가한 뒤 publish()를 호출하면 status가 PUBLISHED로 바뀐다")
    void publish_withSection_changesStatus() {
        // 정답:
        // DocumentRevision revision = DocumentRevision.newInstance(1L, 1L);
        // revision.addSection("intro", "hello");
        // revision.publish();
        // assertThat(revision.getStatus()).isEqualTo(DocumentStatus.PUBLISHED);
    }

    @Test
    @DisplayName("PUBLISHED 상태에서 addSection()을 호출하면 IllegalStateException")
    void addSection_onPublished_throws() {
        // 정답:
        // DocumentRevision revision = DocumentRevision.newInstance(1L, 1L);
        // revision.addSection("intro", "hello");
        // revision.publish();
        // assertThatThrownBy(() -> revision.addSection("more", "content"))
        //         .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("PUBLISHED 상태에서 archive()를 호출하면 ARCHIVED로 바뀐다")
    void archive_onPublished_changesStatus() {
        // 정답:
        // DocumentRevision revision = DocumentRevision.newInstance(1L, 1L);
        // revision.addSection("intro", "hello");
        // revision.publish();
        // revision.archive();
        // assertThat(revision.getStatus()).isEqualTo(DocumentStatus.ARCHIVED);
    }

    @Test
    @DisplayName("ARCHIVED 상태에서 archive()를 다시 호출해도 예외 없이 그대로 ARCHIVED다 (idempotent)")
    void archive_onArchived_isIdempotent() {
        // 정답:
        // DocumentRevision revision = DocumentRevision.newInstance(1L, 1L);
        // revision.addSection("intro", "hello");
        // revision.publish();
        // revision.archive();
        // revision.archive();
        // assertThat(revision.getStatus()).isEqualTo(DocumentStatus.ARCHIVED);
    }

    @Test
    @DisplayName("DRAFT 상태에서 바로 archive()를 호출하면 IllegalStateException")
    void archive_onDraft_throws() {
        // 정답:
        // DocumentRevision revision = DocumentRevision.newInstance(1L, 1L);
        // assertThatThrownBy(revision::archive).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("PUBLISHED 상태 리비전의 DocumentSection.updateContent()는 IllegalStateException")
    void updateContent_onPublishedRevision_throws() {
        // 정답:
        // DocumentRevision revision = DocumentRevision.newInstance(1L, 1L);
        // revision.addSection("intro", "hello");
        // revision.publish();
        // DocumentSection section = revision.getSections().get(0);
        // assertThatThrownBy(() -> section.updateContent("changed"))
        //         .isInstanceOf(IllegalStateException.class);
    }
}
