package gdg.hongik.mission.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    CLOTHES("의류"),
    ELECTRONICS("전자기기"),
    COSMETICS("화장품"),
    FOOD("식품");

    @JsonValue // JSON 직렬화/역직렬화 시 상수명 대신 한글 라벨("의류" 등)을 사용
    private final String label;
}
