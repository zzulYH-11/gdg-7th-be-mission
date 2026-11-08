package gdg.hongik.mission.dto.response;

import gdg.hongik.mission.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        int price,
        int stock
) {
    // Entity -> DTO 변환 편의 메서드
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}
