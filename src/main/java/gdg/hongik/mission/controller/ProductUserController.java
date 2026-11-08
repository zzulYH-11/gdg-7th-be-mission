package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductUserController {

    private final ProductService productService;

    public ProductUserController(ProductService productService) {
        this.productService = productService;
    }

    // 1. 상품 단건 조회
    @GetMapping
    public ProductResponse getProductByName(@RequestParam String name) {
        return productService.getProductByName(name);
    }

    // 2. 상품 일괄 구매
    @PostMapping("/purchase")
    public PurchaseResponse purchaseProducts(@RequestBody List<PurchaseItemRequest> requests) {
        return productService.purchaseProducts(requests);
    }
}
