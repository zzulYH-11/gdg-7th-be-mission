package gdg.hongik.mission.dto.response;

public record PurchasedProductResponse(
        Long id,
        String name,
        int totalPrice,
        int quantity
) {
}
