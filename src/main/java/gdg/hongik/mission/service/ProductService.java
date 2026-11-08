package gdg.hongik.mission.service;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.dto.response.PurchasedProductResponse;
import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // [관리자] 상품 등록
    @Transactional
    public ProductResponse registerProduct(ProductCreateRequest request) {
        if (productRepository.findByName(request.name()) != null) {
            throw new RuntimeException("이미 존재하는 상품입니다.");
        }

        Product newProduct = new Product(
                request.name(),
                request.price(),
                request.stock(),
                request.category()
        );

        return ProductResponse.from(productRepository.save(newProduct));
    }

    // [관리자] 재고 추가 (상품명 기준)
    @Transactional
    public ProductResponse addStock(StockUpdateRequest request) {
        Product product = productRepository.findByName(request.name());
        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }

        // @Transactional에 의해 메서드 종료 시 변경 감지(Dirty Checking)로 DB에 자동 반영됨
        product.setStock(product.getStock() + request.stock());
        return ProductResponse.from(product);
    }

    // [관리자] 상품 삭제 (상품명 기준)
    @Transactional
    public List<ProductResponse> deleteProduct(ProductDeleteRequest request) {
        Product product = productRepository.findByName(request.name());
        if (product == null) {
            throw new RuntimeException("삭제할 상품을 찾을 수 없습니다.");
        }

        productRepository.delete(product);
        return getAllProducts();
    }

    // [소비자] 전체 상품 목록 조회
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    // [소비자] 상품명으로 단건 검색
    public ProductResponse getProductByName(String name) {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }

        return ProductResponse.from(product);
    }

    // [소비자] 장바구니 상품 일괄 구매
    @Transactional
    public PurchaseResponse purchaseProducts(List<PurchaseItemRequest> requests) {
        int totalPrice = 0;
        List<PurchasedProductResponse> items = new ArrayList<>();

        for (PurchaseItemRequest request : requests) {
            Product product = productRepository.findById(request.id())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

            if (product.getStock() < request.count()) {
                throw new RuntimeException("재고가 부족합니다.");
            }

            product.setStock(product.getStock() - request.count());

            int itemTotalPrice = product.getPrice() * request.count();
            totalPrice += itemTotalPrice;

            items.add(new PurchasedProductResponse(
                    product.getName(),
                    request.count(),
                    itemTotalPrice
            ));
        }

        return new PurchaseResponse(totalPrice, items);
    }
}
