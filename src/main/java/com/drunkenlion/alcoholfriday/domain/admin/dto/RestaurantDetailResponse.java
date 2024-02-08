package com.drunkenlion.alcoholfriday.domain.admin.dto;

import com.drunkenlion.alcoholfriday.domain.restaurant.entity.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "매장 상세 조회 항목")
public class RestaurantDetailResponse {
    @Schema(description = "고유 아이디")
    private Long id;

    @Schema(description = "회원 고유 아이디")
    private Long memberId;

    @Schema(description = "회원 별명")
    private String memberNickname;

    @Schema(description = "매장 이름")
    private String name;

    @Schema(description = "매장 카테고리")
    private String category;

    @Schema(description = "매장 주소")
    private String address;

    @Schema(description = "매장 위치 (위도, 경도)")
    private Point location;

    @Schema(description = "매장 연락처")
    private Long contact;

    @Schema(description = "메뉴 목록")
    private Map<String, Object> menu;

    @Schema(description = "영업시간")
    private Map<String, Object> time;

    @Schema(description = "편의시설 목록")
    private Map<String, Object> provision;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "마지막 수정일시")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제일시")
    private LocalDateTime deletedAt;

// TODO: 판매하는 술정보 추가 필요
//    private List<Item> item;

    public static RestaurantDetailResponse of(Restaurant restaurant) {
        return RestaurantDetailResponse.builder()
                .id(restaurant.getId())
                .memberId(restaurant.getMembers().getId())
                .memberNickname(restaurant.getMembers().getNickname())
                .name(restaurant.getName())
                .category(restaurant.getCategory())
                .address(restaurant.getAddress())
                .location(restaurant.getLocation())
                .contact(restaurant.getContact())
                .menu(restaurant.getMenu())
                .time(restaurant.getTime())
                .provision(restaurant.getProvision())
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .deletedAt(restaurant.getDeletedAt())
                .build();
    }
}
