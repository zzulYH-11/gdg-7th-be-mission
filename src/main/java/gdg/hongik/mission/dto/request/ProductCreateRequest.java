package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import gdg.hongik.mission.entity.Category;

public record ProductCreateRequest(
        @JsonProperty("itemName") String name,
        int price,
        @JsonProperty("quantity") int stock,
        Category category
) {
}
