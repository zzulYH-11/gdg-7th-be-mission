package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductUserController {

    private final ProductService productService;

    public ProductUserController(ProductService productService) {
        this.productService = productService;
    }

    // 1. 전체 상품 목록 조회
    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    // 2. 상품명 단건 검색 (params 지정으로 전체 조회와 경로 충돌 방지)
    @GetMapping(params = "itemName")
    public ProductResponse getProductByName(@RequestParam("itemName") String name) {
        return productService.getProductByName(name);
    }

    // 3. 장바구니 상품 일괄 구매
    @PostMapping("/purchase")
    public PurchaseResponse purchaseProducts(@RequestBody List<PurchaseItemRequest> requests) {
        return productService.purchaseProducts(requests);
    }
}
