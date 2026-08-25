package com.apitest.mytest.domainstate;

// TODO: 왜 ORDINAL이 아니라 STRING으로 저장해야 하는지 한 줄로 정리해보세요. (01단계 복습)
//   답: ORDINAL은 enum 순서(0,1,2..)를 저장하는데, 나중에 enum 값 순서가 바뀌거나
//       중간에 새 값이 추가되면 이미 DB에 저장된 숫자의 의미가 틀어져서 데이터가 깨진다.
//       STRING은 이름 그대로 저장하므로 순서가 바뀌어도 안전하다.
public enum DocumentStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED
}
