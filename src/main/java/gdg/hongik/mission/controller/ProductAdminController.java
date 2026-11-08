package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    // 1. 상품 등록
    @PostMapping
    public ProductResponse registerProduct(@RequestBody ProductCreateRequest request) {
        return productService.registerProduct(request);
    }

    // 2. 재고 추가 (상품명 기준)
    @PatchMapping("/stock")
    public ProductResponse addStock(@RequestBody StockUpdateRequest request) {
        return productService.addStock(request);
    }

    // 3. 상품 삭제 (상품명 기준)
    @DeleteMapping
    public List<ProductResponse> deleteProduct(@RequestBody ProductDeleteRequest request) {
        return productService.deleteProduct(request);
    }
}
