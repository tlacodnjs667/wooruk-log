package org.itwill.springboot4.dto;


import lombok.Data;

@Data
public class PostSearchRequestDto {
    private String category = "";
    private String keyword;
    private int curPage; // 검색 결과의 페이지 번호 (0부터 시작)
    // 기본 생성자는 필드를 해당 타입의 기본값으로 만들어주고, int로 타입을 정하면 값이 0으로 됨.
}
