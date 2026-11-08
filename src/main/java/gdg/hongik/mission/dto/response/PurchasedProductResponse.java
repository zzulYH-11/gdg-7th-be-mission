package gdg.hongik.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PurchasedProductResponse(
        @JsonProperty("itemName") String name,
        int count,
        int price
) {
}
